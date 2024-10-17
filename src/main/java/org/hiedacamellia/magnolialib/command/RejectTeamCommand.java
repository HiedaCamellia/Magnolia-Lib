package org.hiedacamellia.magnolialib.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeam;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeams;

import java.util.UUID;

public class RejectTeamCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("reject")
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(ctx -> {
                            MagnoliaTeams teams = MagnoliaTeams.getTeamsFromContext(ctx);
                            CommandSourceStack source = ctx.getSource();
                            UUID playerID = source.getPlayerOrException().getUUID();
                            String name = StringArgumentType.getString(ctx, "name");
                            MagnoliaTeam joining = teams.getTeamByName(name);

                            //If the team doesn't exist
                            if (joining == null) {
                                source.sendFailure(Component.translatable("command.penguinlib.team.reject.not_exist", name));
                                return 0;
                            }

                            if (!joining.isInvited(playerID)) {
                                source.sendFailure(Component.translatable("command.penguinlib.team.reject.not_invited", name));
                                return 0;
                            }

                            source.sendSuccess(() -> Component.translatable("command.penguinlib.team.reject.success", name), false);
                            joining.clearInvite(playerID);
                            return 1;
                        }));
    }
}