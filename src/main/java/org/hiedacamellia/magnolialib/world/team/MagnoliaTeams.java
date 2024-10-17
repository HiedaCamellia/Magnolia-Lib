package org.hiedacamellia.magnolialib.world.team;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.UsernameCache;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.PenguinTeamsClient;
import org.hiedacamellia.magnolialib.event.TeamChangedEvent;
import org.hiedacamellia.magnolialib.network.PenguinNetwork;
import org.hiedacamellia.magnolialib.network.packet.ChangeTeamPacket;
import org.hiedacamellia.magnolialib.network.packet.SyncPlayerTagPacket;
import org.hiedacamellia.magnolialib.network.packet.SyncTeamMembersPacket;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@EventBusSubscriber(modid = MagnoliaLib.MODID)
public class MagnoliaTeams extends SavedData {
    private static final String DATA_NAME = "penguin_teams";
    private final Map<UUID, UUID> memberOf = new HashMap<>(); //Player ID > Team ID
    private final Map<UUID, MagnoliaTeam> teams = new HashMap<>(); // Team ID > Data
    private final Map<String, MagnoliaTeam> teamsByName = new HashMap<>(); //TeamName > Team (not saved)

    public static MagnoliaTeams get(ServerLevel world) {
        return world.getServer().overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(MagnoliaTeams::new, MagnoliaTeams::load), DATA_NAME);
    }

    public boolean nameExists(String name) {
        return teamsByName.containsKey(name);
    }

    public Collection<MagnoliaTeam> teams() {
        return teams.values();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MagnoliaTeams teams = get((ServerLevel) player.level());
            PenguinNetwork.sendToClient(player,
                    getTeamForPlayer(player).getSyncPacket(),
                    new SyncPlayerTagPacket("PenguinStatuses", player),
                    new SyncTeamMembersPacket(teams.memberOf),
                    new SyncPlayerTagPacket("Notes", player) {
            });
        }
    }

    public int getMemberCount(UUID owner_id) {
        UUID team = memberOf.get(owner_id);
        return teams.get(team).members().size();
    }

    public void changeTeam(CommandContext<CommandSourceStack> ctx, UUID newTeam, Consumer<MagnoliaTeam> consumer) throws CommandSyntaxException {
        changeTeam(ctx.getSource().getLevel(), ctx.getSource().getPlayerOrException().getUUID(), newTeam, consumer);
    }

    public void changeTeam(ServerLevel world, UUID player, UUID newUUID) {
        changeTeam(world, player, newUUID, (pt) -> {});
    }

    public void changeTeam(ServerLevel world, UUID player, UUID newUUID, Consumer<MagnoliaTeam> function) {
        UUID oldUUID = memberOf.getOrDefault(player, player);
        memberOf.put(player, newUUID);
        MagnoliaTeam oldTeam = teams.get(oldUUID);
        if (oldTeam != null) {
            oldTeam.members().remove(player);
            oldTeam.onChanged(world);
        }

        if (!teams.containsKey(newUUID)) {
            teams.put(newUUID, new MagnoliaTeam(newUUID));
        }

        MagnoliaTeam newTeam = teams.get(newUUID);
        newTeam.members().add(player);
        teamsByName.remove(newTeam.getName(), newTeam); //Remove the old name
        if (player.equals(newUUID))
            newTeam.setName(UsernameCache.getLastKnownUsername(player));
        function.accept(newTeam);
        teamsByName.put(newTeam.getName(), newTeam); //Add the new name
        newTeam.onChanged(world);
        NeoForge.EVENT_BUS.post(new TeamChangedEvent(world, player, oldUUID, newUUID));
        PenguinNetwork.sendToEveryone(new ChangeTeamPacket(player, oldUUID, newUUID));
        setDirty();
    }

    public MagnoliaTeam getTeam(UUID team) {
        return teams.get(team);
    }

    public CompoundTag getTeamData(UUID team) {
        return teams.get(team).getData();
    }

    public Collection<UUID> getTeamMembers(UUID team) {
        return teams.get(team).members();
    }

    public static MagnoliaTeam getTeamFromID(ServerLevel world, UUID team) {
        return get(world).teams.get(team);
    }

    public static MagnoliaTeam getTeamForPlayer(ServerLevel world, UUID uuid) {
        MagnoliaTeams data = get(world); //Load the serverdata
        if (!data.memberOf.containsKey(uuid)) {
            data.changeTeam(world, uuid, uuid);
            data.setDirty();
        }

        return data.teams.get(data.memberOf.get(uuid));
    }

    public static MagnoliaTeam getTeamForPlayer(Player player) {
        if (player.level().isClientSide) return PenguinTeamsClient.getInstance(); //Client data
        return getTeamForPlayer((ServerLevel) player.level(), player.getUUID());
    }

    public static UUID getTeamUUIDForPlayer(Player player) {
        return getTeamForPlayer(player).getID();
    }

    public static MagnoliaTeam getTeamFromContext(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        MagnoliaTeams teams = getTeamsFromContext(ctx);
        return teams.getTeam(teams.memberOf.get(ctx.getSource().getPlayerOrException().getUUID()));
    }

    public static MagnoliaTeams getTeamsFromContext(CommandContext<CommandSourceStack> ctx) {
        return get(ctx.getSource().getLevel());
    }

    //Used in Shopaholic
    @SuppressWarnings("unused")
    public static CompoundTag getPenguinStatuses(Player player) {
        CompoundTag data = getTeamForPlayer(player).getData();
        if (!data.contains("PenguinStatuses"))
            data.put("PenguinStatuses", new CompoundTag());
        return data.getCompound("PenguinStatuses");
    }

    public static MagnoliaTeams load(@Nonnull CompoundTag nbt) {
        MagnoliaTeams teamData = new MagnoliaTeams();
        ListTag data = nbt.getList("Teams", 10);
        for (int i = 0; i < data.size(); i++) {
            CompoundTag tag = data.getCompound(i);
            MagnoliaTeam team = new MagnoliaTeam(tag);
            teamData.teams.put(team.getID(), team);
            teamData.teamsByName.put(team.getName(), team);
            team.members().forEach(member -> teamData.memberOf.put(member, team.getID())); //Add the quick reference for members
        }
        return teamData;
    }

    @Nonnull
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compound, HolderLookup.Provider provider) {
        ListTag data = new ListTag();
        for (Map.Entry<UUID, MagnoliaTeam> entry : teams.entrySet()) {
            data.add(entry.getValue().serializeNBT(provider));
        }

        compound.put("Teams", data);

        return compound;
    }

    public MagnoliaTeam getTeamByName(String name) {
        return teamsByName.get(name);
    }

}