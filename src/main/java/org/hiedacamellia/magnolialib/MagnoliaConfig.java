package org.hiedacamellia.magnolialib;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MagnoliaConfig {
    public static ModConfigSpec.BooleanValue enableTeamCommands;
    public static ModConfigSpec.BooleanValue enableDatabaseDebugger;

    MagnoliaConfig(ModConfigSpec.Builder builder) {
        enableTeamCommands = builder.define("Enable Penguin Team Commands", true);
        enableDatabaseDebugger = builder.define("Enable Database Debug Output", false);
    }

    public static ModConfigSpec create() {
        return new ModConfigSpec.Builder().configure(MagnoliaConfig::new).getValue();
    }
}
