package org.voltagemod.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.voltagemod.Voltagemod;
import org.voltagemod.content.blockentity.*;

public final class ModBlockEntities {
    public static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Voltagemod.MOD_ID);

    public static final RegistryObject<TileEntityType<GeneratorBlockEntity>> GENERATOR = BLOCK_ENTITIES.register("generator",
            () -> TileEntityType.Builder.of(GeneratorBlockEntity::new, ModBlocks.GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<CableBlockEntity>> CABLE = BLOCK_ENTITIES.register("cable",
            () -> TileEntityType.Builder.of(CableBlockEntity::new, ModBlocks.CABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<LoadBlockEntity>> LOAD = BLOCK_ENTITIES.register("load",
            () -> TileEntityType.Builder.of(LoadBlockEntity::new, ModBlocks.LOAD.get()).build(null));
    public static final RegistryObject<TileEntityType<MeterBlockEntity>> METER = BLOCK_ENTITIES.register("meter",
            () -> TileEntityType.Builder.of(MeterBlockEntity::new, ModBlocks.METER.get()).build(null));

    private ModBlockEntities() {}
}
