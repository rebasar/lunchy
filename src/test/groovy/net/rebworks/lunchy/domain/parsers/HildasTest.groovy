package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.LocalDate


class HildasTest extends Specification {
    def "Hildas parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/hildas/valid.json").text
        and: "A Hildas Parser"
        def parser = new Hildas(new DateCalculator(LocalDate.now()))
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting file should contain correct information"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() == 4 })
    }


    def "Hildas parser returns empty list on missing menu element"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/hildas/invalid_missing_menu_element.json").text
        and: "A Hildas Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Hildas(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

    def "Hildas parser returns empty list on lunch items"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/hildas/invalid_array_as_input.json").text
        and: "A Hildas Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Hildas(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }


}
