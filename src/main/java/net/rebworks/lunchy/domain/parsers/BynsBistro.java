package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class BynsBistro implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public BynsBistro(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);
        final Elements lunchContainers = document.select("div.lunchItem.lunchWeek0");
        if (lunchContainers.isEmpty()) return Collections.emptyList();
        final Element lunchContainer = lunchContainers.first();
        return lunchContainer.children().stream().map(this::parseLunch).collect(toList());
    }

    private Lunch parseLunch(final Element element) {
        final DayOfWeek lunchDay = parseLunchDay(element);
        final LocalDate validFrom = dateCalculator.getDayOfWeek(lunchDay);
        final ImmutableLunch.Builder lunch = Lunch.builder().validFrom(validFrom).validUntil(validFrom.plusDays(1));
        element.select("strong").stream().map(this::parseLunchItem).forEach(lunch::addItems);
        return lunch.build();
    }

    private LunchItem parseLunchItem(final Element element) {
        final Optional<String> description = Optional.ofNullable(element.nextSibling())
                                                     .map(Node::outerHtml)
                                                     .map(String::trim);
        return LunchItem.builder()
                        .title(element.text().trim())
                        .description(description)
                        .build();
    }

    private DayOfWeek parseLunchDay(final Element element) {
        final Elements dayCandidates = element.select("h3");
        if (dayCandidates.isEmpty()) return dateCalculator.today();
        final Element dayElement = dayCandidates.first();
        final String dateString = dayElement.text();
        return dateCalculator.parseDayOfWeekOrToday(dateString);
    }
}
