import net.rebworks.lunchy.domain.date.DateCalculator
import net.rebworks.lunchy.domain.parsers.Aptigarden
import spock.lang.Specification

import java.time.LocalDate

class AptigardenTest extends Specification {

    def "Aptigarden parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/aptigarden/valid.html").text
        and: "A Aptigarden Parser"
        def parser = new Aptigarden(new DateCalculator(LocalDate.now()))
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting file should contain correct information"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() >= 4 })
    }

    def "Aptigarden parser returns empty list on missing root element"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/aptigarden/invalid_missing_menu_element.html").text
        and: "A Aptigarden Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Aptigarden(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

    def "Aptigarden parser returns empty list on lunch items"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/aptigarden/invalid_missing_menu_body.html").text
        and: "A Aptigarden Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Aptigarden(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

}