package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.LocalDate


class HildasTest extends Specification {
    def "Hildas parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/hildas/valid.html").text
        and: "A Hildas Parser"
        def parser = new Hildas(new DateCalculator(LocalDate.now()))
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting file should contain correct information"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() == 4 })
        lunches.stream().allMatch({ lunch -> lunch.items.stream().allMatch({ item -> !item.price.present }) })
    }

}
