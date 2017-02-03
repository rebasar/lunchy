package net.rebworks.lunchy.domain.date

import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

import static java.time.DayOfWeek.FRIDAY
import static java.time.DayOfWeek.MONDAY
import static java.time.DayOfWeek.SATURDAY
import static java.time.DayOfWeek.SUNDAY
import static java.time.DayOfWeek.THURSDAY
import static java.time.DayOfWeek.TUESDAY
import static java.time.DayOfWeek.WEDNESDAY
import static java.time.Month.JANUARY

class DateCalculatorTest extends Specification {
    def "getDayOfWeek calculates the right day"(DayOfWeek dayOfWeek, LocalDate expected) {
        given: "A date calculator frozen in time"
        def dateCalculator = new DateCalculator(LocalDate.of(2017, JANUARY, 4))
        when: "A day of week is calculated"
        def calculated = dateCalculator.getDayOfWeek(dayOfWeek)
        then: "It should give the correct result"
        calculated == expected
        where:
        dayOfWeek | expected
        MONDAY | LocalDate.of(2017, JANUARY, 2)
        TUESDAY | LocalDate.of(2017, JANUARY, 3)
        WEDNESDAY | LocalDate.of(2017, JANUARY, 4)
        THURSDAY | LocalDate.of(2017, JANUARY, 5)
        FRIDAY | LocalDate.of(2017, JANUARY, 6)
        SATURDAY | LocalDate.of(2017, JANUARY, 7)
        SUNDAY | LocalDate.of(2017, JANUARY, 8)
    }

    def "getBeginningOfWeek returns monday of given week"() {
        given: "A date calculator frozen in time"
        def dateCalculator = new DateCalculator(LocalDate.of(2017, JANUARY, 4))
        when: "Beginning of week is calculated"
        def monday = dateCalculator.getBeginningOfWeek()
        then: "It should give the correct monday"
        monday.dayOfWeek == MONDAY
        monday == LocalDate.of(2017, JANUARY, 2)
    }

    def "getEndOfWeek returns friday of given week"() {
        given: "A date calculator frozen in time"
        def dateCalculator = new DateCalculator(LocalDate.of(2017, JANUARY, 4))
        when: "End of the week is calculated"
        def friday = dateCalculator.getEndOfWorkWeek()
        then: "It should give the correct friday"
        friday.dayOfWeek == FRIDAY
        friday == LocalDate.of(2017, JANUARY, 6)
    }

    def "parseDayOfWeek returns the first matching day in the string"(){
        given: "A date calculator frozen in time"
        def dateCalculator = new DateCalculator(LocalDate.of(2017, JANUARY, 4));
        when: "Some lyrics from a The Cure song is parsed"
        def lyrics = """monday you can fall apart
                        tuesday wednesday break my heart,
                        thursday doesn't even start
                        it's friday I'm in love"""
        def result = dateCalculator.parseDayOfWeekOrToday(lyrics);
        then: "First matching day in the string is returned"
        result == MONDAY
    }

    def "parseDayOfWeek returns current day if no day name is matched"(){
        given: "A date calculator frozen in time"
        def dateCalculator = new DateCalculator(LocalDate.of(2017, JANUARY, 4))
        when: "A string with no date in it is parsed"
        def result = dateCalculator.parseDayOfWeekOrToday("Dagens Lunch")
        then: "The result should be current day"
        result == WEDNESDAY
    }
}
