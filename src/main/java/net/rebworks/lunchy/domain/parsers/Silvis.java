package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch.Builder;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Silvis implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public Silvis(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);
        final Elements lunchItems = document.select("div.lunchRowItem");
        if (lunchItems.isEmpty()) return Collections.emptyList();
        final Builder lunch = Lunch.builder()
                                   .validFrom(dateCalculator.getBeginningOfWeek())
                                   .validUntil(dateCalculator.getEndOfWorkWeek().plusDays(1));
        lunchItems.stream().flatMap(this::parseLunchItem).forEach(lunch::addItems);
        return Collections.singletonList(lunch.build());
    }

    private Stream<LunchItem> parseLunchItem(final Element element) {
        final String rawText = element.text().trim();
        final String[] parts = rawText.split("\\.", 2);
        if (parts.length == 0) return Stream.empty();
        if (parts.length == 1) return Stream.of(LunchItem.builder().title(rawText).build());
        else return Stream.of(LunchItem.builder().title(parts[0].trim()).description(parts[1].trim()).build());
    }
}
