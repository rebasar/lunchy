package net.rebworks.lunchy.domain

import net.rebworks.lunchy.dto.ImmutableLunch
import net.rebworks.lunchy.dto.Lunch
import net.rebworks.lunchy.dto.LunchItem
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime


class LunchesTest extends Specification {
    private LocalDateTime now = LocalDateTime.now()
    private LocalDate today = now.toLocalDate()

    def "forDate returns first matching lunch"() {
        given: "Two lunches with matching dates"
        def firstLunch = createLunch(today.minusDays(1), today.plusDays(1))
        def secondLunch = createLunch(today, today.plusDays(2))
        and: "A lunch with a different date"
        def unmatchedLunch = createLunch(today.minusDays(3), today.minusDays(1))
        and: "A Lunches from those items"
        def lunches = Lunches.builder().expires(now).addLunches(unmatchedLunch, firstLunch, secondLunch).build()
        when: "The matching lunches are queried"
        def lunch = lunches.forDate(today)
        then: "The first matching lunch should be returned"
        lunch.map({l -> l.is(firstLunch)}).orElse(false)
    }

    def "forDate returns empty if no lunches match"() {
        given: "Two unmatching lunches"
        def pastLunch = createLunch(today.minusDays(3), today.minusDays(1))
        def futureLunch = createLunch(today.plusDays(2), today.plusDays(4))
        and: "A Lunches from those items"
        def lunches = Lunches.builder().expires(now).addLunches(pastLunch, futureLunch).build()
        when: "The matching lunches are queried"
        def lunch = lunches.forDate(today)
        then: "The first matching lunch should be returned"
        !lunch.present
    }

    def "exists is true if there is at leat one lunch"() {
        given: "Lunches with a single lunch"
        def lunches = Lunches.builder().
                expires(now).
                addLunches(createLunch(today, today.plusDays(1))).build()
        when: "Existence of lunches is queried"
        def exists = lunches.exists()
        then: "The result should be true"
        exists
    }

    def "exists is false if there are no lunches at all"() {
        given: "Empty lunches"
        def lunches = Lunches.empty();
        when: "Existence of lunches is queried"
        def exists = lunches.exists()
        then: "The result should be false"
        !exists
    }

    private ImmutableLunch createLunch(LocalDate validFrom, LocalDate validUntil) {
        Lunch.builder().
                validFrom(validFrom).
                validUntil(validUntil).
                addItems(LunchItem.builder().title("Foo").build()).
                build()
    }
}
