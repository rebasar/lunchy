package net.rebworks.lunchy.domain.parsers.util

import spock.lang.Specification


class SwedishTitleDescriptionSplitterTest extends Specification {
    def "Split returns just a simple title if keywords do not appear"() {
        given: "A splitter"
        def splitter = new SwedishTitleDescriptionSplitter()
        and: "A title text that does not contain the keywords"
        def title = "Veggie of the week: SWEET POTATO BURRITO"
        when: "The title is split"
        def split = splitter.split(title)
        then: "The title should be the same"
        split.title == title
        and: "The description should be empty"
        !split.description.isPresent()
    }

    def "Split returns a title and description if the title contains the keyword ' med '"() {
        given: "A splitter"
        def splitter = new SwedishTitleDescriptionSplitter()
        and: "A title text that does contain the word ' med '"
        def title = "A, Kycklinggryta med massaman curry kokos-mjölk, jordnötter, paprika, & grönsaker"
        when: "The title is split"
        def split = splitter.split(title)
        then: "The title should be the part before ' med '"
        split.title == "A, Kycklinggryta"
        and: "The description should be the rest of the text"
        split.description
             .map({ description -> description == "massaman curry kokos-mjölk, jordnötter, paprika, & grönsaker" })
             .orElse(false)
    }

    def "Split returns a title and description if the title contains the keyword ' serveras med '"() {
        given: "A splitter"
        def splitter = new SwedishTitleDescriptionSplitter()
        and: "A title text that does contain the word ' serveras med '"
        def title = "A, Kycklinggryta serveras med massaman curry kokos-mjölk, jordnötter, paprika, & grönsaker"
        when: "The title is split"
        def split = splitter.split(title)
        then: "The title should be the part before ' serveras med '"
        split.title == "A, Kycklinggryta"
        and: "The description should be the rest of the text"
        split.description
             .map({ description -> description == "massaman curry kokos-mjölk, jordnötter, paprika, & grönsaker" })
             .orElse(false)
    }

    def "Split returns the split with shortest title when ' med ' comes before ' serveras med '"() {
        given: "A splitter"
        def splitter = new SwedishTitleDescriptionSplitter()
        and: "A title text that does contain both keywords, with ' med ' coming first"
        def title = "Palestinsk köttgryta med ärtor i tomatsås serveras med ris och grönsaker"
        when: "The title is split"
        def split = splitter.split(title)
        then: "The title should be the part before first ' med '"
        split.title == "Palestinsk köttgryta"
        and: "The description should be the rest of the text"
        split.description
             .map({ description -> description == "ärtor i tomatsås serveras med ris och grönsaker" })
             .orElse(false)
    }

    def "Split returns the split with shortest title when ' serveras med ' comes before ' med '"() {
        given: "A splitter"
        def splitter = new SwedishTitleDescriptionSplitter()
        and: "A title text that does contain both keywords, with ' med ' coming first"
        def title = "Långkokt högrev serveras med chili­pepparsås serveras med potatis och grönsaker"
        when: "The title is split"
        def split = splitter.split(title)
        then: "The title should be the part before first ' med '"
        split.title == "Långkokt högrev"
        and: "The description should be the rest of the text"
        split.description
             .map({ description -> description == "chili­pepparsås serveras med potatis och grönsaker" })
             .orElse(false)
    }
}
