package net.rebworks.lunchy.domain.parsers.util;

import java.util.Optional;
import java.util.regex.Pattern;

public class SwedishTitleDescriptionSplitter {

    public static class TitleAndDescription {
        private final String prefix;
        private final String title;
        private final String description;

        private TitleAndDescription(final String prefix, final String title, final String description) {
            this.prefix = prefix;
            this.title = title;
            this.description = description;
        }

        private TitleAndDescription(final String prefix, final String title) {
            this(prefix, title, null);
        }

        public String getTitle() {
            return title;
        }

        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        public Optional<String> getFormattedDescription() {
            return getDescription().map(d -> prefix + " " + d);
        }

        public static TitleAndDescription from(final String prefix, final String[] split) {
            if (split.length == 1) {
                return new TitleAndDescription(prefix, split[0].trim());
            }
            return new TitleAndDescription(prefix, split[0].trim(), split[1].trim());
        }

    }

    private static class Splitter {
        private final String patternText;
        private final Pattern pattern;

        private Splitter(final String patternText) {
            this.patternText = patternText;
            this.pattern = Pattern.compile(patternText);
        }

        public TitleAndDescription split(final String title) {
            final String[] split = pattern.split(title, 2);
            final String prefix = patternText.trim(); // TODO: Capitalise
            return TitleAndDescription.from(prefix, split);
        }
    }

    private static final String MED_PATTERN = " med ";
    private static final String SERVERAS_MED_PATTERN = " serveras med ";
    private static final Splitter MED_SPLITTER = new Splitter(MED_PATTERN);
    private static final Splitter SERVERAS_MED_SPLITTER = new Splitter(SERVERAS_MED_PATTERN);

    public TitleAndDescription split(final String title) {

        final TitleAndDescription med = MED_SPLITTER.split(title);
        final TitleAndDescription serverasMed = SERVERAS_MED_SPLITTER.split(title);

        if (med.getDescription().isPresent() && !serverasMed.getDescription().isPresent()) return med;
        if (serverasMed.getDescription().isPresent() && !med.getDescription().isPresent()) return serverasMed;
        if (med.getTitle().length() < serverasMed.getTitle().length()) return med;
        return serverasMed;
    }

}
