package org.hiedacamellia.magnolialib.util.random;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

//@EventBusSubscriber(modid = PenguinLib.MODID) //TODO
public class LootRegistry<I> {
    private final NavigableMap<Double, Pair<I, Double>> lootTable = new TreeMap<>();

    private double total = 0;

    public void add(I value, double weight) {
        total += weight;
        lootTable.put(total, Pair.of(value, weight));
    }

    public LootRegistry<I> merge(LootRegistry<I> l2) {
        LootRegistry<I> l3 = new LootRegistry<>();
        lootTable.forEach((k, v) -> l3.add(v.getLeft(), v.getRight()));
        l2.lootTable.forEach((k, v) -> l3.add(v.getLeft(), v.getRight()));
        return l3;
    }

    @Nullable
    public I get(Random rand) {
        return lootTable.isEmpty() ? null : lootTable.ceilingEntry((rand.nextDouble() * total)).getValue().getLeft();
    }

    @Nullable
    public I get(RandomSource rand) {
        return lootTable.isEmpty() ? null : lootTable.ceilingEntry((rand.nextDouble() * total)).getValue().getLeft();
    }

    /*
    private static final Map<String, LootRegistry<?>> loot_registries = Maps.newHashMap();


    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) { //LOWEST
        event.table("registries").where("type=loot_state|type=loot_item").forEach(row -> {
            String name = row.get("name");
            String type = row.get("type");
            LootRegistry registry = new LootRegistry<>();
            event.table(name + "_" + type).rows().forEach(r -> {
                Object o = toObject(type, r.get("entry"));
                if (o != null) registry.add(o, r.getAsDouble("weight"));
            });

            loot_registries.put(name, registry);
        });

        event.table("registries").where("type=loot_table").forEach(row -> {
            String name = row.get("name");
            LootRegistry registry = new LootRegistry();
            event.table(name + "_loot_table").rows().forEach(r -> {
                LootRegistry<?> sub_registry = loot_registries.get(r.get("loot_table").toString());
                registry.add(sub_registry, r.getAsDouble("weight"));
            });

            loot_registries.put(name, registry);
        });
    }

    @Nullable
    private static Object toObject(String type, String entry) {
        if (type.equals("loot_state")) return StateAdapter.fromString(entry);
        else if (type.equals("loot_item")) return StackHelper.getStackFromString(entry);
        else return null;
    } */
}
