package de.natrox.common.console;

import de.natrox.common.console.handler.Toggleable;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Objects;

final class JLine3Completer implements Completer {

    private final JLine3Console console;

    public JLine3Completer(@NotNull JLine3Console console) {
        this.console = console;
    }

    @Override
    public void complete(LineReader reader, @NotNull ParsedLine line, @NotNull List<Candidate> candidates) {
        this.console.tabCompleteHandlers().values().stream()
            .filter(Toggleable::enabled)
            .flatMap(handler -> handler.completeInput(line.line()).stream())
            .filter(Objects::nonNull)
            .map(Candidate::new)
            .forEach(candidates::add);
    }

}
