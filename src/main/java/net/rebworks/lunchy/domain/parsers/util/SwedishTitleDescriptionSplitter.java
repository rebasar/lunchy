package net.rebworks.lunchy.domain.parsers.util;

import java.util.Optional;
import java.util.regex.Pattern;

public class SwedishTitleDescriptionSplitter {

    public static class TitleAndDescription {
        private final String title;
        private final String description;

        private TitleAndDescription(final String title, final String description) {
            this.title = title;
            this.description = description;
        }

        private TitleAndDescription(final String title) {
            this.title = title;
            description = null;
        }

        public String getTitle() {
            return title;
        }

        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        public static TitleAndDescription from(final String[] split) {
            if (split.length == 1) {
                return new TitleAndDescription(split[0].trim());
            }
            return new TitleAndDescription(split[0].trim(), split[1].trim());
        }
    }

    private static final Pattern MED = Pattern.compile(" med ", Pattern.CASE_INSENSITIVE);
    private static final Pattern SERVERAS_MED = Pattern.compile(" serveras med ", Pattern.CASE_INSENSITIVE);

    public TitleAndDescription split(final String title) {
        final String[] medSplit = MED.split(title, 2);
        final String[] serverasMedSplit = SERVERAS_MED.split(title, 2);
        if (medSplit.length > serverasMedSplit.length) return TitleAndDescription.from(medSplit);
        if (serverasMedSplit.length > medSplit.length) return TitleAndDescription.from(serverasMedSplit);
        if (medSplit[0].length() < serverasMedSplit[0].length()) return TitleAndDescription.from(medSplit);
        if (serverasMedSplit[0].length() < medSplit[0].length()) return TitleAndDescription.from(serverasMedSplit);
        return TitleAndDescription.from(medSplit);
    }

}
