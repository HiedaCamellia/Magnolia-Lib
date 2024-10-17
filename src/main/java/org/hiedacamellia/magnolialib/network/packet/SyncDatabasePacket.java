package org.hiedacamellia.magnolialib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaConfig;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.data.database.Database;
import org.hiedacamellia.magnolialib.data.database.Table;
import org.hiedacamellia.magnolialib.event.DatabaseLoadedEvent;
import org.hiedacamellia.magnolialib.event.DatabasePopulateEvent;
import org.hiedacamellia.magnolialib.util.registry.Packet;

import java.util.HashMap;
import java.util.Map;

@Packet(value = PacketFlow.CLIENTBOUND)
public class SyncDatabasePacket implements MagnoliaPacket {
    private final Database database;
    private final Map<String, Table> tables = new HashMap<>();

    public static final Type<SyncDatabasePacket> TYPE = new Type<>(MagnoliaLib.prefix("database_sync"));

    public SyncDatabasePacket(Database database) {
        this.database = database;
    }

    public SyncDatabasePacket(FriendlyByteBuf buf) {
        database = new Database();
        int tableCount = buf.readShort();
        for (int i = 0; i < tableCount; i++) {
            String name = buf.readUtf();
            int parts = buf.readShort();
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < parts; j++)
                builder.append(buf.readUtf());
            Database.parseCSV(tables, database.tableData, name, builder.toString());
        }
    }

//    @Override
    public void write(FriendlyByteBuf buf) {
        int tableCount = database.tableData.size();
        buf.writeShort(tableCount);
        for (Map.Entry<String, String> entry : database.tableData.entries()) {
            buf.writeUtf(entry.getKey());
            int parts = (int) Math.ceil((double) entry.getValue().length() / (double) Short.MAX_VALUE);
            buf.writeShort(parts);
            for (int j = 0; j < parts; j++)
                buf.writeUtf(entry.getValue().substring(j * Short.MAX_VALUE, Math.min((j + 1) * Short.MAX_VALUE, entry.getValue().length())));
        }
    }

    @Override
    public void handle(Player player) {
        Database.INSTANCE.set(database);
        if (MagnoliaConfig.enableDatabaseDebugger.get())
            Database.print(tables);
        NeoForge.EVENT_BUS.post(new DatabasePopulateEvent(tables));
        NeoForge.EVENT_BUS.post(new DatabaseLoadedEvent(tables));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}