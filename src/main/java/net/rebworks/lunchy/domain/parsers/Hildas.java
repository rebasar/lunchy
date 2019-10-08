package net.rebworks.lunchy.domain.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.domain.parsers.hildas.Container;
import net.rebworks.lunchy.domain.parsers.hildas.Menu;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hildas implements Parser {

  private final DateCalculator dateCalculator;

  @Inject
  public Hildas(final DateCalculator dateCalculator) {
    this.dateCalculator = dateCalculator;
  }

  @Override
  public List<Lunch> parse(String input) {

    List<Lunch> lunches = new ArrayList<>();


    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode daysMenu = objectMapper.readTree(input);

      if (daysMenu.has("acf")){

        Container dm = objectMapper.treeToValue(daysMenu.get("acf"), Container.class);

        dm.days.forEach(day -> lunches.add(Lunch.builder().
                validFrom(dateCalculator.getDayOfWeek(dateCalculator.parseDayOfWeekOrToday(day.day))).
                validUntil(dateCalculator.getDayOfWeek(dateCalculator.parseDayOfWeekOrToday(day.day)).plusDays(1)).
                addAllItems(getLunchItems(day.menu)).build()));

        return lunches;
      }

    } catch (IOException e) {
      return Collections.emptyList();
    }
    return Collections.emptyList();
  }

  private List<LunchItem> getLunchItems(List<Menu> menus){
    List<LunchItem> items = new ArrayList<>();
    menus.forEach(title -> items.add(LunchItem.builder().title(title.title).description(title.text).build()));
    return items;
  }

}
