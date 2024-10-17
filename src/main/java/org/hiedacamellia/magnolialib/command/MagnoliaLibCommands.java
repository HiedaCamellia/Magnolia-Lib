package org.hiedacamellia.magnolialib.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.hiedacamellia.magnolialib.MagnoliaConfig;
import org.hiedacamellia.magnolialib.MagnoliaLib;

@EventBusSubscriber(modid = MagnoliaLib.MODID)
public class MagnoliaLibCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        if (MagnoliaConfig.enableTeamCommands.get()) {
            event.getDispatcher().register(
                    LiteralArgumentBuilder.<CommandSourceStack>literal("magnolia")
                            .then(NewDayCommand.register())
                            .then(Commands.literal("team")
                                    .then(CreateTeamCommand.register())
                                    .then(InviteCommand.register())
                                    .then(ListInvitesCommand.register())
                                    .then(JoinTeamCommand.register())
                                    .then(LeaveTeamCommand.register())
                                    .then(RenameTeamCommand.register())
                                    .then(RejectTeamCommand.register()))
            );
        }
    }
}
