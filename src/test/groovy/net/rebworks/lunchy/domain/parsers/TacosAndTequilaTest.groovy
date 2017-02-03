package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

class TacosAndTequilaTest extends Specification {

    def "Parse successfully parses correct content"(){
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/tacos/valid.html").text
        and: "A Tacos & Tequila Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new TacosAndTequila(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain correct information"
        lunches.size() == 1
        def lunch = lunches[0]
        lunch.items.size() == 5
        lunch.items.stream().allMatch({ item -> item.price == OptionalInt.of(98)})
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.MONDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.TUESDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.WEDNESDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.THURSDAY))
        lunch.isValidAt(calculator.getDayOfWeek(DayOfWeek.FRIDAY))
    }

    def "Parse successfully parses valid file with missing price"(){
        given: "A test file with valid content but missing price"
        def contents = this.getClass().getResource("/testData/tacos/valid_missing_price.html").text
        and: "A Tacos & Tequila Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new TacosAndTequila(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should not contain price"
        lunches.size() == 1
        def lunch = lunches[0]
        lunch.items.size() == 5
        lunch.items.stream().allMatch({ item -> !item.price.present})
    }

    def "Parse produces empty content on missing root element"(){
        given: "A test file with missing root element"
        def contents = this.getClass().getResource("/testData/tacos/invalid_missing_root_element.html").text
        and: "A Tacos & Tequila Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new TacosAndTequila(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

    def "Parse produces empty content if all items are missing"(){
        given: "A test file with missing root element"
        def contents = this.getClass().getResource("/testData/tacos/invalid_changed_styles.html").text
        and: "A Tacos & Tequila Parser"
        def calculator = new DateCalculator(LocalDate.now())
        def parser = new TacosAndTequila(calculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

}
