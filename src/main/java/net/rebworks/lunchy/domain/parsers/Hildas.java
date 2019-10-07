package net.rebworks.lunchy.domain.parsers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.rebworks.lunchy.domain.Parser;
import net.rebworks.lunchy.domain.date.DateCalculator;
import net.rebworks.lunchy.dto.Lunch;
import net.rebworks.lunchy.dto.LunchItem;
import org.json.JSONArray;
import org.json.JSONObject;

public class Hildas implements Parser {

  private final DateCalculator dateCalculator;

  @Inject
  public Hildas(final DateCalculator dateCalculator) {
    this.dateCalculator = dateCalculator;
  }

  @Override
  public List<Lunch> parse(String input) {

    List<Lunch> lunches = new ArrayList<>();


    JSONObject jsonObject = new JSONObject(input);


    JSONArray menu =  ((JSONArray) ((JSONObject) jsonObject.get("acf")).get("days"));

    menu.forEach(day -> lunches.add(Lunch.builder().validFrom(LocalDate.now()).validUntil(LocalDate.now().plusDays(1)).
        addAllItems(getLunchItems((JSONObject) day)).build()));

    return lunches;
  }

  private List<LunchItem> getLunchItems(JSONObject dayMenu){
    List<LunchItem> items = new ArrayList<>();
    dayMenu.getJSONArray("menu").forEach(title -> items.add(LunchItem.builder().title(((JSONObject)(title)).get("text").toString()).build()));
    return items;
  }

}
