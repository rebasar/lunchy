package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Barabicu implements Parser {

    private static final String ITEM_SEPARATOR = " • ";
    private final DateCalculator dateCalculator;

    @Inject
    public Barabicu(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);
        final Elements elements = document.body().select("#todays-lunch .slides li");
        return elements.stream().map(this::parseLunch).collect(toList());
    }

    private Lunch parseLunch(final Element element) {
        final DayOfWeek dayOfWeek = getLunchDay(element);
        final LocalDate validFrom = dateCalculator.getDayOfWeek(dayOfWeek);
        final Elements items = element.select("h3");
        final ImmutableLunch.Builder builder = Lunch.builder();
        builder.validFrom(validFrom).validUntil(validFrom.plusDays(1));
        items.stream().flatMap(this::parseLunchItem).forEach(builder::addItems);
        return builder.build();
    }

    private DayOfWeek getLunchDay(final Element element) {
        final String day = element.select("h1").text();
        return dateCalculator.parseDayOfWeekOrToday(day);
    }

    private Stream<LunchItem> parseLunchItem(final Element element) {
        final String[] titleAndPrice = element.text().split(ITEM_SEPARATOR);
        final String title = titleAndPrice[0];
        if (title == null || title.isEmpty()) {
            return Stream.empty();
        }
        final OptionalInt price = titleAndPrice.length > 1 ? parsePrice(titleAndPrice[1]) : OptionalInt.empty();
        final Optional<String> description = parseDescription(element);
        return Stream.of(LunchItem.builder().title(title).price(price).description(description).build());
    }

    private Optional<String> parseDescription(final Element element) {
        return Optional.ofNullable(element.nextElementSibling()).map(Element::text);
    }

    private OptionalInt parsePrice(final String priceString) {
        final String[] priceParts = priceString.split("\\s");
        if (priceParts.length == 0) {
            return OptionalInt.empty();
        }
        try {
            return OptionalInt.of(Integer.parseInt(priceParts[0]));
        } catch (final NumberFormatException e){
            return OptionalInt.empty();
        }
    }
}
