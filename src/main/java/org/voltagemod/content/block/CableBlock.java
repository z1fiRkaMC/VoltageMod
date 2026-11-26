package org.voltagemod.content.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.voltagemod.content.blockentity.CableBlockEntity;

import javax.annotation.Nullable;

public class CableBlock extends ElectricalBlock {
    public CableBlock(Properties properties) { super(properties); }
    @Nullable @Override public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new CableBlockEntity(); }
}
