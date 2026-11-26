package org.voltagemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.voltagemod.core.network.manager.ElectricalNetworkManager;
import org.voltagemod.registry.ModBlockEntities;
import org.voltagemod.registry.ModBlocks;
import org.voltagemod.registry.ModItems;

@Mod(Voltagemod.MOD_ID)
public class Voltagemod {
    public static final String MOD_ID = "voltagemod";
    private static final Logger LOGGER = LogManager.getLogger();

    public Voltagemod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("VoltageMod setup");
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isClientSide && event.world instanceof ServerWorld) {
            ElectricalNetworkManager.get((ServerWorld) event.world).tick();
        }
    }
}
