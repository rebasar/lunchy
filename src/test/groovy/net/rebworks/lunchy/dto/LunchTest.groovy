package net.rebworks.lunchy.dto

import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

class LunchTest extends Specification {

    @Shared
    LocalDate now = LocalDate.now()

    def "isValidAt behaves inclusive of the validFrom and exclusive of validUntil"(
            final LocalDate from, final LocalDate to, final boolean expected) {
        given: "A lunch valid for today"
        final Lunch lunch = Lunch.builder().validFrom(from).validUntil(to).build()
        when: "The validity is checked for today"
        def validAt = lunch.isValidAt(now)
        then: "The result should be true"
        validAt == expected
        where:
        from | to | expected
        now | now.plusDays(1) | true
        now.minusDays(1) | now | false
        now.minusDays(2) | now.plusDays(2) | true
        now.plusDays(1) | now.plusDays(3) | false
        now.minusDays(3) | now.minusDays(1) | false
    }
}
