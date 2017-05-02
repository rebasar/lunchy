package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch.Builder;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Kok17 implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public Kok17(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final List<Lunch> result = new ArrayList<>(5);
        final Document document = Jsoup.parse(input);
        final Element[] dayHeaders = document.body()
                                             .select("article h1")
                                             .stream()
                                             .filter(element -> dateCalculator.isDayOfWeek(element.text()))
                                             .toArray(Element[]::new);
        for (final Element dayHeader : dayHeaders) {
            final DayOfWeek validDay = dateCalculator.parseDayOfWeekOrToday(dayHeader.text().trim());
            final LocalDate validFrom = dateCalculator.getDayOfWeek(validDay);
            final Builder builder = Lunch.builder().validFrom(validFrom).validUntil(validFrom.plusDays(1));
            parseLunchItems(dayHeader, builder);
            result.add(builder.build());
        }
        return result;
    }

    private void parseLunchItems(final Element dayHeader, final Builder builder) {
        Element lunchItemCandidate = dayHeader.nextElementSibling();
        while (lunchItemCandidate != null && isValidLunchItem(lunchItemCandidate)) {
            builder.addItems(LunchItem.builder().title(lunchItemCandidate.text()).build());
            lunchItemCandidate = lunchItemCandidate.nextElementSibling();
        }
    }

    private boolean isValidLunchItem(final Element lunchItemCandidate) {
        return lunchItemCandidate.tagName().equals("p")
                && lunchItemCandidate.select("br").isEmpty()
                && !lunchItemCandidate.text().replace(" ", "").isEmpty();
    }
}
