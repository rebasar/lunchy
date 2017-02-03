package net.rebworks.lunchy.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

import java.util.Optional;
import java.util.OptionalInt;

@Immutable
@JsonDeserialize(as = ImmutableLunchItem.class)
public interface LunchItem {

    static ImmutableLunchItem.Builder builder() {
        return ImmutableLunchItem.builder();
    }

    String getTitle();

    Optional<String> getDescription();

    OptionalInt getPrice();
}
