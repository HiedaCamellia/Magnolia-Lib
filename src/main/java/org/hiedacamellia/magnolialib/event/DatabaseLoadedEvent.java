package org.hiedacamellia.magnolialib.event;

import net.neoforged.bus.api.Event;
import org.hiedacamellia.magnolialib.data.database.Table;

import javax.annotation.Nonnull;
import java.util.Map;

public class DatabaseLoadedEvent extends Event {
    private final Map<String, Table> tables;

    public DatabaseLoadedEvent(Map<String, Table> tables) {
        this.tables = tables;
    }

    @Nonnull
    public Table table(String table) {
        return tables.getOrDefault(table, Table.EMPTY);
    }
}
