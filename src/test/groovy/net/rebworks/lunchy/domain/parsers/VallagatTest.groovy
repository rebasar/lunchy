import net.rebworks.lunchy.domain.date.DateCalculator
import net.rebworks.lunchy.domain.parsers.Vallagat
import spock.lang.Specification

import java.time.LocalDate

class VallagatTest extends Specification {

    def "Vallagat parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/vallagat/vallagat.html").text
        and: "A Vallagat Parser"
        def parser = new Vallagat(new DateCalculator(LocalDate.now()))
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting file should contain correct information"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() >= 1 })
    }

    def "Vallagat parser returns empty list on missing root element"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/vallagat/invalid_missing_menu_element.html").text
        and: "A Vallagat Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Vallagat(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

    def "Vallagat parser returns empty list on lunch items"() {
        given: "A test file with missing content"
        def contents = this.getClass().getResource("/testData/vallagat/invalid_missing_menu_body.html").text
        and: "A Vallagat Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new Vallagat(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.size() == 0
    }

}