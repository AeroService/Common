package de.notion.common.localization.context.def;

import de.notion.common.localization.MessageProvider;
import de.notion.common.localization.context.ContextMessageProvider;
import de.notion.common.localization.context.Contextualizer;

import java.util.UUID;

public class UUIDMessageProvider extends ContextMessageProvider<UUID> {

    public UUIDMessageProvider(MessageProvider messageProvider, Contextualizer<UUID> contextualizer) {
        super(messageProvider, contextualizer);
    }
}
