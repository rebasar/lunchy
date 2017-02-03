package net.rebworks.lunchy.domain;

import net.rebworks.lunchy.dto.Lunch;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Immutable
public interface Lunches {

    static ImmutableLunches.Builder builder() {
        return ImmutableLunches.builder();
    }

    static Lunches empty() {
        return builder().expires(LocalDateTime.MIN).build();
    }

    LocalDateTime getExpires();

    List<Lunch> getLunches();

    default Optional<Lunch> forDate(LocalDate date) {
        return getLunches().stream()
                           .filter(lunch -> lunch.isValidAt(date))
                           .findAny();
    }

    default boolean exists() {
        return !getLunches().isEmpty();
    }
}
