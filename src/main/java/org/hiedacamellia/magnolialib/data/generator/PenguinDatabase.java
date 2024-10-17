package org.hiedacamellia.magnolialib.data.generator;

import net.minecraft.data.PackOutput;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.data.TimeUnitRegistry;

import java.util.Arrays;

public class PenguinDatabase extends AbstractDatabaseProvider {
    public PenguinDatabase(PackOutput output) {
        super(output, MagnoliaLib.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        Arrays.stream(TimeUnitRegistry.Defaults.values())
                .forEach(unit -> addTimeUnit(unit.getName(), unit.getValue()));
    }
}