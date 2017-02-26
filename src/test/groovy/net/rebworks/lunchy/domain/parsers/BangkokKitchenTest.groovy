package net.rebworks.lunchy.domain.parsers

import net.rebworks.lunchy.domain.date.DateCalculator
import spock.lang.Specification

import java.time.LocalDate

class BangkokKitchenTest extends Specification {
    def "Bangkok Kitchen parser parses a valid file successfully"() {
        given: "A test file with valid content"
        def contents = this.getClass().getResource("/testData/bkk/valid.html").text
        and: "A Bangkok Kitchen Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new BangkokKitchen(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The resulting object should contain five days of lunch"
        lunches.size() == 5
        and: "Each lunch should include five items"
        lunches.stream().allMatch({ lunch -> lunch.items.size() == 5 })
        and: "All lunches should be priced the same"
        lunches.stream().
                allMatch({ lunch -> lunch.items.stream().allMatch({ item -> item.price == OptionalInt.of(80) }) })
        and: "All lunches should have a title and description"
        lunches.stream().
                allMatch({ lunch -> lunch.items.stream().allMatch({ item -> item.description.isPresent() }) })
    }

    def "Bangkok Kitchen parser handles change of element IDs gracefully"() {
        given: "A test file where the IDs are wrong"
        def contents = this.getClass().getResource("/testData/bkk/invalid_different_id.html").text
        and: "A Bangkok Kitchen parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new BangkokKitchen(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The result should be an empty list"
        lunches.isEmpty()
    }

    def "Bangkok kitchen parser handles missing price gracefully"() {
        given: "A test file with missing price information"
        def contents = this.getClass().getResource("/testData/bkk/valid_missing_price.html").text
        and: "A Bangkok Kitchen parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new BangkokKitchen(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "Results should exist"
        lunches.size() == 5
        lunches.stream().allMatch({ lunch -> lunch.items.size() == 5 })
        and: "They should not have any price attached"
        lunches.stream().
                allMatch({ lunch -> lunch.items.stream().allMatch({ item -> item.price == OptionalInt.empty() }) })
    }

    def "Bangkok kitchen parser skips items where it cannot extract a title and a description"(){
        given: "A test file where the first item of first lunch is malformed"
        def contents = this.getClass().getResource("/testData/bkk/valid_with_malformed_first_item.html").text
        and: "A Bangkok Kitchen Parser"
        def dateCalculator = new DateCalculator(LocalDate.now())
        def parser = new BangkokKitchen(dateCalculator)
        when: "The file is parsed"
        def lunches = parser.parse(contents)
        then: "The results should start from the second item"
        def firstItem = lunches.first().items.first()
        firstItem.title.startsWith("B, ")
    }
}
