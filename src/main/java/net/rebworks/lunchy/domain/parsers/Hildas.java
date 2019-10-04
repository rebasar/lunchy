package net.rebworks.lunchy.domain.parsers;

import java.util.ArrayList;
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
import org.jsoup.nodes.TextNode;
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
    final Elements lunchContainers = document.select("div.meny");

    if (lunchContainers.isEmpty()) {
      return Collections.emptyList();
    }

    final Element lunchContainer = lunchContainers.first();

    List<Lunch> lunches = new ArrayList<>();

    List<Node> nodes = lunchContainer.childNodes();

    for (int i = 0; i < nodes.size();) {
      Node item = nodes.get(i);
      if (item.nodeName().equals("strong")) {
        i += 2;
        final DayOfWeek lunchDay = dateCalculator
            .parseDayOfWeekOrToday(item.nodeName().toLowerCase());
        String description = ((TextNode) nodes.get(i)).text();
        List<LunchItem> lunchItemList = new ArrayList<>();
        i += 2;
        while (i < nodes.size() && !(nodes.get(i) instanceof Element && ((Element) nodes.get(i))
            .tagName().equals("strong"))) {

          if (!(nodes.get(i) instanceof Element) || !((Element) nodes.get(i)).tagName()
              .equals("br")) {
            lunchItemList
                .add(LunchItem.builder().description(description)
                    .title(((TextNode) nodes.get(i)).text()).build());

          }
          i++;
        }
        lunches.add(Lunch.builder().validFrom(dateCalculator.getDayOfWeek(lunchDay))
            .validUntil(dateCalculator.getDayOfWeek(lunchDay).plusDays(1))
            .addAllItems(lunchItemList).build());


      }
    }

    return lunches;
  }

}
