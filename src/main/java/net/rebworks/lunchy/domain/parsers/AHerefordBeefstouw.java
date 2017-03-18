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
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AHerefordBeefstouw implements Parser {

    private static final Pattern PRICE_PATTERN = Pattern.compile("^(\\d+) Skr\\.");
    private final DateCalculator dateCalculator;

    @Inject
    public AHerefordBeefstouw(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final List<Lunch> lunches = new ArrayList<>(5);
        final Document document = Jsoup.parse(input);
        final Elements itemCandidates = document.body().select("div.span8 h3");
        if (itemCandidates.isEmpty()) return lunches;
        final Builder builder = Lunch.builder()
                                     .validFrom(dateCalculator.getBeginningOfWeek())
                                     .validUntil(dateCalculator.getEndOfWorkWeek());
        itemCandidates.stream().flatMap(this::parseItem).forEach(builder::addItems);
        lunches.add(builder.build());
        return lunches;
    }

    private Stream<LunchItem> parseItem(final Element element) {
        if (!"h3".equalsIgnoreCase(element.tagName())) return Stream.empty();
        final Element descriptionElement = element.nextElementSibling();
        if (descriptionElement == null || !"p".equalsIgnoreCase(descriptionElement.tagName())) return Stream.empty();
        final Element priceTable = descriptionElement.nextElementSibling();
        if (priceTable == null || !"table".equalsIgnoreCase(priceTable.tagName())) return Stream.empty();
        final OptionalInt price = extractPrice(priceTable);
        if (!price.isPresent()) return Stream.empty();
        return Stream.of(LunchItem.builder()
                                  .title(element.text())
                                  .description(descriptionElement.text())
                                  .price(price)
                                  .build());
    }

    private OptionalInt extractPrice(final Element priceTable) {
        final String priceText = priceTable.select("td.text-right").text();
        final Matcher matcher = PRICE_PATTERN.matcher(priceText);
        if (matcher.matches()) {
            return OptionalInt.of(Integer.parseInt(matcher.group(1)));
        }
        return OptionalInt.empty();
    }
}
