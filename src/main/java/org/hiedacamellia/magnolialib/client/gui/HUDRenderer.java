package org.hiedacamellia.magnolialib.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.hiedacamellia.magnolialib.MagnoliaLib;
import org.hiedacamellia.magnolialib.client.MagnoliaClientConfig;
import org.hiedacamellia.magnolialib.util.PenguinTags;
import org.hiedacamellia.magnolialib.util.helper.PlayerHelper;
import org.hiedacamellia.magnolialib.util.helper.TimeHelper;

import javax.annotation.Nullable;
import java.util.Objects;

@EventBusSubscriber(value = Dist.CLIENT, modid = MagnoliaLib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class HUDRenderer {
    public static Object2ObjectMap<ResourceKey<Level>, HUDRenderData> RENDERERS = new Object2ObjectOpenHashMap<>();

    public abstract static class HUDRenderData {
        public boolean isEnabled(Minecraft mc) {
            return true;
        }

        @Nullable
        public ResourceLocation getTexture(Minecraft mc) {
            return null;
        }

        public abstract Component getHeader(Minecraft mc);

        public String getFooter(Minecraft mc) {
            String time = formatTime((int) TimeHelper.getTimeOfDay(mc.level.getDayTime()));
            return "(" + TimeHelper.shortName(TimeHelper.getWeekday(mc.level.getDayTime())) + ")" + "  " + time;
        }

        public int getX() {
            return 0;
        }

        public int getY() {
            return 0;
        }

        public int getClockX() {
            return 42;
        }

        public int getClockY() {
            return 23;
        }
    }

    private static String formatTime(int time) {
        int hour = time / 1000;
        int minute = (int) ((double) (time % 1000) / 20 * 1.2);
        if (MagnoliaClientConfig.clockType.get() == MagnoliaClientConfig.ClockType.TWENTY_FOUR_HOUR) {
            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute);
        } else {
            boolean pm = false;
            if (hour > 12) {
                hour = hour - 12;
                pm = true;
            }
            if (hour == 12)
                pm = true;
            if (hour == 0)
                hour = 12;

            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + (pm ? "PM" : "AM");
        }
    }

    private static boolean hasClockInventory;

    private static boolean hasClockInInventory(Player player) {
        if (player.level().getDayTime() % 60 == 0) {
            try {
                hasClockInventory = PlayerHelper.hasInInventory(player, PenguinTags.CLOCKS, 1);
            } catch (Exception ex) {
                hasClockInventory = false;
            }
        }
        return hasClockInventory;
    }

    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(MagnoliaLib.prefix( "hud"), (layer,d) -> {
            if (RENDERERS.isEmpty()) return;
            if (!Minecraft.getInstance().options.hideGui) {
                //Minecraft.getInstance().setupOverlayRenderState(true, false);
                layer.pose().pushPose();
                renderHUD(Minecraft.getInstance(), layer);
                layer.pose().popPose();
            }
        });
    }

    private static void renderHUD(Minecraft mc, GuiGraphics graphics) {
        if (mc.getDebugOverlay().showDebugScreen()) return;
        HUDRenderData hud = RENDERERS.get(mc.level.dimension());
        if (hud != null) {
            PoseStack matrix = graphics.pose();
            RenderSystem.enableBlend();
            int x = hud.getX();
            int y = hud.getY();
            if (hud.isEnabled(mc)) {
                ResourceLocation texture = hud.getTexture(mc);
                if (texture != null) {
                    RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
                    //mc.getTextureManager().bindForSetup(texture);//inMine ? MINE_HUD : season.HUD);
                    graphics.blit(texture, x - 44, y - 35, 0, 0, 256, 110);
                }

                //Enlarge the Day
                matrix.pushPose();
                matrix.scale(1.4F, 1.4F, 1.4F);
                Component header = hud.getHeader(mc);
                graphics.drawString(mc.font, header, (int)(x / 1.4F) + 30, (int)(y / 1.4F) + 7, 0xFFFFFFFF);
                matrix.popPose();
            }

            //Draw the time
            if (MagnoliaClientConfig.displayClockInHUDs.get()) {
                if (!MagnoliaClientConfig.requireClockItemForTime.get() || (MagnoliaClientConfig.requireClockItemForTime.get() && hasClockInInventory(Objects.requireNonNull(mc.player))))
                    graphics.drawString(mc.font, hud.getFooter(mc), x + hud.getClockX(), y + hud.getClockY(), 0xFFFFFFFF);
            }
            RenderSystem.disableBlend();
        }
    }
}