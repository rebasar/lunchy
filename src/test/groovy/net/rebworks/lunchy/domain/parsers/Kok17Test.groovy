package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.LocalDate


class Kok17Test extends Specification {

    def "Kök17 Parser parses valid input correctly"(){
        given: "A valid menu for Kök17"
        def contents = this.getClass().getResource("/testData/kok17/valid.html").text
        and: "A Kok17 parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Kok17(dateCalculator)
        when: "The input is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain lunches for five days"
        lunches.size() == 5
        and: "Each lunch should have non zero number of items"
        lunches.stream().noneMatch({lunch -> lunch.items.isEmpty()})
        and: "The last paragraph of the last day is excluded"
        lunches.last().items.size() == 3
    }

    def "Existence of multiple article elements does not affect the result"(){
        given: "A menu for Kök17 with multiple article elements"
        def contents = this.getClass().getResource("/testData/kok17/valid_multiple_article_elements.html").text
        and: "A Kok17 parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Kok17(dateCalculator)
        when: "The input is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain lunches for five days"
        lunches.size() == 5
    }

    def "Days with missing content is included"(){
        given: "A menu for Kök17 with no content on Monday"
        def contents = this.getClass().getResource("/testData/kok17/valid_missing_content_in_monday.html").text
        and: "A Kok17 parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Kok17(dateCalculator)
        when: "The input is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain lunches for five days"
        lunches.size() == 5
        and: "Monday lunch should not have any items"
        lunches.first().items.isEmpty()
    }

    def "Weeks with holidays get parsed correctly"(){
        given: "A menu for Kök17 with a holiday on Monday"
        def contents = this.getClass().getResource("/testData/kok17/valid_monday_is_holiday.html").text
        and: "A Kok17 parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Kok17(dateCalculator)
        when: "The input is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain lunches for five days"
        lunches.size() == 5
        and: "Monday lunch should not have any items"
        lunches.first().items.isEmpty()
    }

    def "Parsing of a day stops at the first invalid item"(){
        given: "A menu for Kök17 with an unexpected item on Monday"
        def contents = this.getClass().getResource("/testData/kok17/valid_unexpected_element_between_items.html").text
        and: "A Kok17 parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Kok17(dateCalculator)
        when: "The input is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain lunches for five days"
        lunches.size() == 5
        and: "Monday lunch should not have any items"
        lunches.first().items.size() == 1
    }

}
