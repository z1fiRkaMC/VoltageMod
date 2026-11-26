package org.voltagemod.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.voltagemod.Voltagemod;
import org.voltagemod.content.block.GeneratorBlock;
import org.voltagemod.content.block.CableBlock;
import org.voltagemod.content.block.LoadBlock;
import org.voltagemod.content.block.MeterBlock;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Voltagemod.MOD_ID);

    public static final RegistryObject<Block> GENERATOR = BLOCKS.register("generator",
            () -> new GeneratorBlock(AbstractBlock.Properties.of(Material.METAL).strength(3.5F)));
    public static final RegistryObject<Block> CABLE = BLOCKS.register("cable",
            () -> new CableBlock(AbstractBlock.Properties.of(Material.METAL).strength(2.0F)));
    public static final RegistryObject<Block> LOAD = BLOCKS.register("load",
            () -> new LoadBlock(AbstractBlock.Properties.of(Material.METAL).strength(3.0F)));
    public static final RegistryObject<Block> METER = BLOCKS.register("meter",
            () -> new MeterBlock(AbstractBlock.Properties.of(Material.METAL).strength(3.0F)));

    private ModBlocks() {}
}
