package org.voltagemod.content.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class ElectricalBlock extends Block {
    protected ElectricalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World world, net.minecraft.util.math.BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof org.voltagemod.content.blockentity.ElectricalBlockEntity) {
                ((org.voltagemod.content.blockentity.ElectricalBlockEntity) te).onRemoved();
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}
