package net.rebworks.lunchy.domain;

import net.rebworks.lunchy.dto.Lunch;

import java.util.List;

@FunctionalInterface
public interface Parser {

    List<Lunch> parse(final String input);

}
