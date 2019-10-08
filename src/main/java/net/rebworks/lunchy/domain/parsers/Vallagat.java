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
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vallagat implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public Vallagat(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);
        final Elements container = document.body().getElementsByClass("spc");

        final Elements elements = container.select("p");

        Map<DayOfWeek, List<Element>> days = new HashMap<>();


        for (int i = 0; i< elements.size(); i++){
            if (elements.get(i).hasText() && dateCalculator.isDayOfWeek(elements.get(i).text())){
                List<Element> dayMenu = new ArrayList<>();
                for (int j = i+1; j<elements.size(); j++){
                    if (elements.get(j).hasText() && dateCalculator.isDayOfWeek(elements.get(j).text())){
                        break;
                    }
                    dayMenu.add(elements.get(j));
                }
                days.put(dateCalculator.parseDayOfWeekOrToday(elements.get(i).text()), dayMenu);
            }
        }

        return parseDays(days);
    }

    private List<Lunch> parseDays(Map<DayOfWeek, List<Element>> daysMenu){
        List<Lunch> lunches = new ArrayList<>();
        daysMenu.forEach((d, ls) -> lunches.add(parseDay(d, ls)));
        return lunches;
    }

    private Lunch parseDay(DayOfWeek d, List<Element> ls) {
        ImmutableLunch.Builder builder =  Lunch.builder().validFrom(dateCalculator.getDayOfWeek(d)).validUntil(dateCalculator.getDayOfWeek(d).plusDays(1));

        builder.addAllItems(parseLunchItems(ls).collect(Collectors.toList()));

        return builder.build();
    }

    private Stream<LunchItem> parseLunchItems(List<Element> ls) {

        return ls.stream().filter(el -> el.getAllElements().size() == 1).filter(m -> m.hasText() && StringUtils.isNoneEmpty(m.text())).map(e -> LunchItem.builder().title(e.text()).build());

    }
}
