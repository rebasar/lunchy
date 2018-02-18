package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.domain.parsers.util.SwedishTitleDescriptionSplitter;
import net.rebworks.lunchy.domain.parsers.util.SwedishTitleDescriptionSplitter.TitleAndDescription;
import net.rebworks.lunchy.dto.ImmutableLunch.Builder;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BangkokKitchen implements Parser {

    private final DateCalculator dateCalculator;
    private final SwedishTitleDescriptionSplitter titleDescriptionSplitter;
    private final Pattern pricePattern = Pattern.compile(".*Endast (\\d+) kr.*");

    @Inject
    public BangkokKitchen(final DateCalculator dateCalculator, final SwedishTitleDescriptionSplitter titleDescriptionSplitter) {
        this.dateCalculator = dateCalculator;
        this.titleDescriptionSplitter = titleDescriptionSplitter;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final List<Lunch> lunches = new ArrayList<>(5);
        final Document document = Jsoup.parse(input);
        final Elements contentBlocks = document.select("#block-yui_3_17_2_8_1442340801696_21429 div.sqs-block-content");
        if (contentBlocks.isEmpty()) return lunches;
        final Element contentBlock = contentBlocks.first();
        final OptionalInt price = extractPrice(document);
        Builder builder = null;
        for (final Element child : contentBlock.children()) {
            if (isLunchHeader(child)) {
                commit(lunches, builder);
                builder = initializeLunchBuilder(child);
            } else if (builder != null && isLunchItem(child)) {
                parseLunchItem(price, child).ifPresent(builder::addItems);
            }
        }
        commit(lunches, builder);
        return lunches;
    }

    private void commit(final List<Lunch> lunches, final Builder builder) {
        if (builder != null) {
            lunches.add(builder.build());
        }
    }

    private boolean isLunchHeader(final Element child) {
        return "h3".equals(child.tagName());
    }

    private boolean isLunchItem(final Element child) {
        return "p".equals(child.tagName()) && child.hasText();
    }

    private Optional<LunchItem> parseLunchItem(final OptionalInt price, final Element child) {
        final String[] parts = child.text().split("\\.", 2);
        if (parts.length != 2) return Optional.empty();
        final TitleAndDescription titleAndDescription = titleDescriptionSplitter.split(parts[0]);
        final String description = parts[1];
        return Optional.of(LunchItem.builder()
                                    .title(titleAndDescription.getTitle())
                                    .description(titleAndDescription.getFormattedDescription().map(d -> d + ". " + description).orElse(description))
                                    .price(price)
                                    .build());
    }

    private Builder initializeLunchBuilder(final Element child) {
        final Builder builder;
        final DayOfWeek dayOfWeek = dateCalculator.parseDayOfWeekOrToday(child.text());
        final LocalDate validFrom = dateCalculator.getDayOfWeek(dayOfWeek);
        final LocalDate validUntil = validFrom.plusDays(1);
        builder = Lunch.builder().validFrom(validFrom).validUntil(validUntil);
        return builder;
    }

    private OptionalInt extractPrice(final Document document) {
        final Elements priceCandidates = document.body().select("h3.text-align-center");
        return priceCandidates.stream()
                              .map(Element::text)
                              .map(pricePattern::matcher)
                              .filter(Matcher::matches)
                              .mapToInt(match -> Integer.parseInt(match.group(1)))
                              .findAny();
    }
}
