package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.LocalDate

class AHerefordBeefstouwTest extends Specification {

    def "AHB Parser successfully parses a valid file"() {
        given: "A valid menu from AHB"
        def contents = this.getClass().getResource("/testData/ahb/valid.html").text
        and: "An AHB parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new AHerefordBeefstouw(dateCalculator)
        when: "The content is parsed"
        def lunches = parser.parse(contents)
        then: "The result should contain a single lunch"
        lunches.size() == 1
        and: "The lunch should have six items in total"
        def lunch = lunches.first()
        lunch.items.size() == 6
        and: "All items should have prices assigned"
        lunch.items.stream().allMatch({ item -> item.price.isPresent() })
    }

    def "AHB Parser produces empty result if the root element does not exist"() {
        given: "A menu with missing root part"
        def contents = this.getClass().getResource("/testData/ahb/invalid_root_element_does_not_have_class.html").text
        and: "An AHB parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new AHerefordBeefstouw(dateCalculator)
        when: "The content is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be empty"
        lunches.isEmpty()
    }

    def "AHB Parser should validate if the description is in a p tag"() {
        given: "A menu with the first item description using a span instead of p"
        def contents = this.getClass().
                getResource("/testData/ahb/valid_with_first_item_description_in_wrong_tag.html").text
        and: "An AHB parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new AHerefordBeefstouw(dateCalculator)
        when: "The content is parsed"
        def lunches = parser.parse(contents)
        then: "The first item should be skipped"
        def lunch = lunches.first()
        lunch.items.stream().noneMatch({item -> item.title == "Grillad lax"})
    }

}
