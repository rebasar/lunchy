package net.rebworks.lunchy.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.util.List;

@Immutable
@JsonDeserialize(as = ImmutableLunch.class)
public interface Lunch {
    static ImmutableLunch.Builder builder() {
        return ImmutableLunch.builder();
    }

    LocalDate getValidFrom();

    LocalDate getValidUntil();

    List<LunchItem> getItems();

    default boolean isValidAt(final LocalDate date) {
        return (getValidFrom().isBefore(date) || getValidFrom().isEqual(date)) && getValidUntil().isAfter(date);
    }

}
