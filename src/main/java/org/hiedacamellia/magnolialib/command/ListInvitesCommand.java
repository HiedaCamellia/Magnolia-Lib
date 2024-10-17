package org.hiedacamellia.magnolialib.command;


import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.hiedacamellia.magnolialib.util.helper.StringHelper;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeam;
import org.hiedacamellia.magnolialib.world.team.MagnoliaTeams;

public class ListInvitesCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("invite_list")
                .executes(ctx -> {
                    boolean success = false;
                    for (MagnoliaTeam team: MagnoliaTeams.get(ctx.getSource().getLevel()).teams()) {
                        if (team.isInvited(ctx.getSource().getPlayerOrException().getUUID())) {
                            ctx.getSource().getPlayerOrException().createCommandSourceStack().sendSuccess(() -> Component.translatable("command.penguinlib.team.invite.message",
                                    team.getName(), StringHelper.withClickableCommand(ChatFormatting.GREEN, "/penguin team join %s", team.getName())), false);
                            success = true;
                        }
                    }

                    if (!success) {
                        ctx.getSource().sendFailure(Component.translatable("command.penguinlib.team.invite.none"));
                    }

                    return success ? 1 : 0;
                });
    }
}