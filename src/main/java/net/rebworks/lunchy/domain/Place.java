package net.rebworks.lunchy.domain;

import net.rebworks.lunchy.domain.date.DateCalculator;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.SortedSet;

public interface Place {
    String getName();

    SortedSet<String> getAliases();

    URI getWebsite();

    URI getUri();

    default LocalDateTime getExpiry(LocalDateTime dateTime) {
        return new DateCalculator(dateTime.toLocalDate()).getEndOfWeek().plusDays(1).atStartOfDay();
    }

}
