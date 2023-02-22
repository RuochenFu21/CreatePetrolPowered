package net.forsteri.createpetrolpowered;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.forsteri.createpetrolpowered.entry.Recipes;
import net.forsteri.createpetrolpowered.entry.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreatePetrolPowered.MODID)
public class CreatePetrolPowered {

    public static final String MODID = "createpetrolpowered";

    @SuppressWarnings("removal")
    public static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(CreatePetrolPowered.MODID);

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreatePetrolPowered() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        Recipes.register(eventBus);
        Registration.register();
    }

    public static NonNullSupplier<CreateRegistrate> registrate() {
        return REGISTRATE;
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
