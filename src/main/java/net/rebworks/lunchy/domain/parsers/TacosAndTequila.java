package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch.Builder;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;

public class TacosAndTequila implements Parser {

    private static final Pattern PRICE_PATTERN = Pattern.compile("^.+is (\\d+) kr.+$");
    private final DateCalculator dateCalculator;

    @Inject
    public TacosAndTequila(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);
        final Elements containters = document.body().select("div.three_fifth.column");
        if (containters.isEmpty()) return Collections.emptyList();
        final Element lunchMenuContainer = containters.first();
        final Element menuItemsContainer = lunchMenuContainer.select("p").first();
        if (menuItemsContainer == null) return Collections.emptyList();
        final Elements menuItems = menuItemsContainer.select("span[style*=\"color: #f7941e\"]");
        if (menuItems.isEmpty()) return Collections.emptyList();
        final OptionalInt price = parsePrice(menuItemsContainer.nextElementSibling());
        final Builder lunch = Lunch.builder()
                                   .validFrom(dateCalculator.getBeginningOfWeek())
                                   .validUntil(dateCalculator.getEndOfWorkWeek().plusDays(1));
        menuItems.stream().map(element -> parseMenuItem(price, element)).forEach(lunch::addItems);
        return Collections.singletonList(lunch.build());
    }

    private OptionalInt parsePrice(final Element element) {
        final String priceLine = element.select("span.accent").text();
        final String priceString = PRICE_PATTERN.matcher(priceLine).replaceAll("$1");
        try {
            return OptionalInt.of(Integer.parseInt(priceString));
        } catch (final NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    private LunchItem parseMenuItem(final OptionalInt price, final Element element) {
        final String title = element.text().replace(":", "");
        final Optional<String> description = Optional.ofNullable(element.nextSibling())
                                                     .map(Node::outerHtml)
                                                     .map(String::trim);
        return LunchItem.builder().title(title).description(description).price(price).build();
    }
}
