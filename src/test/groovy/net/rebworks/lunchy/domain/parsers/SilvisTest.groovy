package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate


class SilvisTest extends Specification {

    def "Silvis parser parses a valid file successfully"(){
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/silvis/valid.html").text
        and: "A Silvis Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new Silvis(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain correct information"
        lunches.size() == 1
        def lunch = lunches[0]
        lunch.items.size() == 7
        def itemWithDescription = lunch.items[0]
        itemWithDescription.description.present
        def itemWithoutDescription = lunch.items[2]
        !itemWithoutDescription.description.present
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.MONDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.TUESDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.WEDNESDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.THURSDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.FRIDAY))
    }

    def "Silvis parser returns empty list if no items are found"(){
        given: "A test file with no lunch items"
        def contents = this.getClass().getResource("/testData/silvis/invalid_no_items.html").text
        and: "A Silvis Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new Silvis(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain correct information"
        lunches.size() == 0
    }

}
