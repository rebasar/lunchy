package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Hildas implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public Hildas(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(String input) {
        final Document document = Jsoup.parse(input);
        final Elements lunchContainers = document.select("div.slick-track");

        if (lunchContainers.isEmpty()) return Collections.emptyList();

        final Element lunchContainer = lunchContainers.first();

        final Elements days = lunchContainer.select("div.slick-slide");

        //days.next()

        String[] lunches = lunchContainer.text().split("<strong>");

        return Arrays.stream(lunches).map(this::parseLunch).collect(Collectors.toList());
    }

    private Lunch parseLunch(final String element) {
        final DayOfWeek lunchDay = parseLunchDay(element);
        final LocalDate validFrom = dateCalculator.getDayOfWeek(lunchDay);
        final ImmutableLunch.Builder lunch = Lunch.builder().validFrom(validFrom).validUntil(validFrom.plusDays(1));

        String lunchItemsString = element.substring(element.indexOf("/>")+1);

        String[] items = lunchItemsString.split("<br />");

        String description = items[0];

        for (int i = 1; i<items.length; i++){
            if (StringUtils.isNoneEmpty(items[i].trim())){
                lunch.addItems(LunchItem.builder().title(items[i]).description(description).build());
            }

        }

        return lunch.build();
    }

    private DayOfWeek parseLunchDay(final String element) {
        System.out.println(element);
        final String dateString = element.substring(0, element.indexOf("<"));
        return dateCalculator.parseDayOfWeekOrToday(dateString.toLowerCase());
    }
}
