package org.hiedacamellia.magnolialib.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeam;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeams;

import java.util.UUID;

public class LeaveTeamCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("leave")
                .executes(ctx -> {
                    MagnoliaTeams teams = MagnoliaTeams.getTeamsFromContext(ctx);
                    MagnoliaTeam current = MagnoliaTeams.getTeamFromContext(ctx);
                    CommandSourceStack source = ctx.getSource();
                    UUID playerID = source.getPlayerOrException().getUUID();
                    //If this is a single player team you cannot leave
                    if (current.getID().equals(playerID)) {
                        source.sendFailure(Component.translatable("command.penguinlib.team.leave.cannot"));
                        return 0;
                    }

                    teams.changeTeam(ctx, playerID, (pt) -> {});
                    source.sendSuccess(() -> Component.translatable("command.penguinlib.team.leave.success"), false);
                    return 1;
                });
    }
}