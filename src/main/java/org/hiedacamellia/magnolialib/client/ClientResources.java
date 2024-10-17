package org.hiedacamellia.magnolialib.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.hiedacamellia.magnolialib.MagnoliaLib;

@EventBusSubscriber(modid = MagnoliaLib.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientResources {
    public static final ResourceLocation SPEECH_BUBBLE = MagnoliaLib.prefix( "extra/speech_bubble");

    @SubscribeEvent
    public static void loadModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.inventory(ClientResources.SPEECH_BUBBLE));
    }
}
