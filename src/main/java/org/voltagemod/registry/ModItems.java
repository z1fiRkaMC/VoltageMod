package org.voltagemod.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.voltagemod.Voltagemod;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Voltagemod.MOD_ID);

    public static final RegistryObject<Item> GENERATOR = ITEMS.register("generator",
            () -> new BlockItem(ModBlocks.GENERATOR.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> CABLE = ITEMS.register("cable",
            () -> new BlockItem(ModBlocks.CABLE.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> LOAD = ITEMS.register("load",
            () -> new BlockItem(ModBlocks.LOAD.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> METER = ITEMS.register("meter",
            () -> new BlockItem(ModBlocks.METER.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));

    private ModItems() {}
}
