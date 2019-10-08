package net.rebworks.lunchy.domain.parsers;

import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.ImmutableLunch;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aptigarden implements Parser {

    private final DateCalculator dateCalculator;

    @Inject
    public Aptigarden(final DateCalculator dateCalculator) {
        this.dateCalculator = dateCalculator;
    }

    @Override
    public List<Lunch> parse(final String input) {
        final Document document = Jsoup.parse(input);

        final Elements container = document.body().getElementsByClass("PostContent");

        if (container.size() == 0 || container.get(0) == null || container.get(0).getAllElements() == null
                || container.get(0).getAllElements().get(4) == null){
            return Collections.emptyList();
        }

        final List<Node> nodes = container.get(0).getAllElements().get(4).childNodes();

        Map<DayOfWeek, List<Node>> days = new HashMap<>();


        for (int i = 0; i< nodes.size(); i++){
            if (dateCalculator.isDayOfWeek(nodes.get(i).toString())){
                List<Node> dayMenu = new ArrayList<>();
                for (int j = i+1; j<nodes.size(); j++){
                    if (dateCalculator.isDayOfWeek(nodes.get(j).toString()) || nodes.get(j).toString().contains("PRIS")){
                        break;
                    }
                    dayMenu.add(nodes.get(j));
                }
                days.put(dateCalculator.parseDayOfWeekOrToday(nodes.get(i).toString()), dayMenu);
            }
        }

        return parseDays(days);
    }

    private List<Lunch> parseDays(Map<DayOfWeek, List<Node>> daysMenu){
        List<Lunch> lunches = new ArrayList<>();
        daysMenu.forEach((d, ls) -> lunches.add(parseDay(d, ls)));
        return lunches;
    }

    private Lunch parseDay(DayOfWeek d, List<Node> ls) {
        ImmutableLunch.Builder builder =  Lunch.builder().validFrom(dateCalculator.getDayOfWeek(d)).validUntil(dateCalculator.getDayOfWeek(d).plusDays(1));

        builder.addAllItems(parseLunchItems(ls).collect(Collectors.toList()));

        return builder.build();
    }

    private Stream<LunchItem> parseLunchItems(List<Node> ls) {

        return ls.stream().filter(m -> !m.toString().contains("<br") && StringUtils.isNoneEmpty(m.toString())).map(e -> LunchItem.builder().title(e.toString()).build());

    }
}