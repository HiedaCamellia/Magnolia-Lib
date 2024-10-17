package org.hiedacamellia.magnolialib;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.minecraft.DetectedVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.modscan.ModAnnotation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.hiedacamellia.magnolialib.client.MagnoliaClientConfig;
import org.hiedacamellia.magnolialib.data.PenguinRegistries;
import org.hiedacamellia.magnolialib.data.generator.*;
import org.hiedacamellia.magnolialib.network.packet.MagnoliaPacket;
import org.hiedacamellia.magnolialib.util.IModPlugin;
import org.hiedacamellia.magnolialib.util.registry.Packet;
import org.hiedacamellia.magnolialib.util.registry.Plugin;
import org.hiedacamellia.magnolialib.world.item.MagnoliaItems;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@EventBusSubscriber(modid = MagnoliaLib.MODID, bus = EventBusSubscriber.Bus.MOD)
@Mod(MagnoliaLib.MODID)
public class MagnoliaLib {
    public static final String MODID = "magnolialib";
    public static final String DATABASE_FOLDER = MODID + "/database";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final Type PACKET = Type.getType(Packet.class);
    private static final Type PLUGIN = Type.getType(Plugin.class);
    public static final String CATEGORIES_FOLDER = MODID + "/categories";
    public static final String NOTES_FOLDER = MODID + "/notes";
    private static List<IModPlugin> plugins = new ArrayList<>();

    public MagnoliaLib(IEventBus eventBus, ModContainer modContainer) {
        plugins = getPlugins();
        MagnoliaLib.plugins.forEach(IModPlugin::construct);
        MagnoliaItems.register(eventBus);
        PenguinRegistries.register(eventBus);
        modContainer.registerConfig(ModConfig.Type.CLIENT, MagnoliaClientConfig.create());
        modContainer.registerConfig(ModConfig.Type.COMMON, MagnoliaConfig.create());
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        //Grab any other things that need to be automagically registered ^
        registerPenguinLoaderData(); //Process them and load them
        plugins.forEach(IModPlugin::setup);
        plugins = null; //Kill the plugins
    }

    @SubscribeEvent
    public static void onDataGathering(GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = event.getGenerator().getPackOutput();
        //PackMetadataGenerator
        PenguinBlockTags blocktags = new PenguinBlockTags(output, event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blocktags);
        generator.addProvider(event.includeServer(), new PenguinItemTags(output, event.getLookupProvider(), blocktags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new PenguinBannerTags(output, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new PenguinDatabase(output));
        generator.addProvider(event.includeServer(), new TestNotes(output,event.getLookupProvider().join()));

        //Client
        generator.addProvider(event.includeClient(), new PenguinSpriteSourceProvider(output, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new PenguinLanguage(output));
        generator.addProvider(event.includeClient(), new PenguinItemModels(output, event.getExistingFileHelper()));
        generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
                Component.literal("Resources for Penguin-Lib"),
                DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
                Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
    }

    private static List<Pair<Class<MagnoliaPacket>, PacketFlow>> PACKETS = Lists.newArrayList();

    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
//
//        PACKETS.forEach(pair -> {
//            ResourceLocation ID = ObfuscationReflectionHelper.getPrivateValue(pair.getLeft(), null, "ID");
//            if (ID == null) throw new RuntimeException("Packet " + pair.getLeft().getName() + " has no ID");
//            FriendlyByteBuf.Reader<MagnoliaPacket> reader = (buf) -> {
//                try {
//                    return pair.getLeft().getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
//                } catch (Exception e) {
//                    throw new RuntimeException("Packet " + pair.getLeft().getName() + " has no constructor that takes a FriendlyByteBuf");
//                }
//            };
//
//            //PenguinLib.LOGGER.info("Registering packet " + ID);
//            if (pair.getRight() == PacketFlow.SERVERBOUND) {
//                registrar.play(ID, reader, handler -> handler.server(PenguinNetwork::handlePacket));
//            } else registrar.play(ID, reader, handler -> handler.client(PenguinNetwork::handlePacket));
//        });

        PACKETS = null;
    }

    private static List<IModPlugin> getPlugins() {
        List<IModPlugin> list = new ArrayList<>();
        ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream)
                .filter(a -> PLUGIN.equals(a.annotationType()))
                .filter(a -> ModList.get().isLoaded((String) a.annotationData().get("value")))
                .forEach(a -> {
                    try {
                        Class<?> clazz = Class.forName(a.clazz().getClassName());
                        list.add((IModPlugin) clazz.getDeclaredConstructor().newInstance());
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                             NoSuchMethodException ignored) { } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
        return list;
    }

    @SuppressWarnings("unchecked")
    private static void registerPenguinLoaderData() {
        ModList.get().getAllScanData().stream()
                .map(ModFileScanData::getAnnotations)
                .flatMap(Collection::stream) //Either of the penguin annotation or the packet annotation is ok
                .filter(a -> PACKET.equals(a.annotationType()))//, i trust that i will use the packet one only on packets ;)
                .forEach((a -> {
                    try {
                        Class<?> clazz = Class.forName(a.clazz().getClassName());
                        PACKETS.add(Pair.of((Class<MagnoliaPacket>) clazz, PacketFlow.valueOf(((ModAnnotation.EnumHolder) a.annotationData().get("value")).value())));
                    } catch (ClassNotFoundException ignored) {}
                }));
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path.toLowerCase());
    }

}
