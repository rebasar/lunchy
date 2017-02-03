package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class BarabicuTest extends Specification {

    def "Barabicu parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/barabicu/valid.html").text
        and: "A Barabicu Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Barabicu(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain correct information"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() == 4 })
    }

    def "Barabicu parser parses a valid file successfully and skips missing days"() {
        given: "A test file with Wednesday missing"
        def contents = this.getClass().getResource("/testData/barabicu/valid_missingday.html").text
        and: "A Barabicu Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Barabicu(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain all days except Wednesday"
        def wednesday = dateCalculator.getDayOfWeek(DayOfWeek.WEDNESDAY)
        lunches.size() == 4
        lunches.stream().noneMatch({ lunch -> lunch.isValidAt(wednesday) })
    }

    def "Barabicu parser parses a valid file successfully and skips missing price"() {
        given: "A test file with missing prices on Monday"
        def contents = this.getClass().getResource("/testData/barabicu/valid_missing_price_on_monday.html").text
        and: "A Barabicu Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Barabicu(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "In the resulting object monday items should not have prices"
        def monday = dateCalculator.getDayOfWeek(DayOfWeek.MONDAY)
        lunches.size() == 5
        lunches.stream().
                allMatch({ lunch ->
                    lunch.isValidAt(monday) || lunch.items.stream().
                            allMatch({ item -> item.price.present })
                })
        lunches.stream().
                filter({ lunch -> lunch.isValidAt(monday) }).
                allMatch({ lunch -> lunch.items.stream().allMatch({ item -> !item.price.present }) })
    }

    def "Barabicu parser returns empty list on missing root element"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/barabicu/invalid_missing_menu_element.html").text
        and: "A Barabicu Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Barabicu(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

    def "Barabicu parser returns empty list on lunch items"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/barabicu/invalid_missing_menu_body.html").text
        and: "A Barabicu Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Barabicu(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }
}
