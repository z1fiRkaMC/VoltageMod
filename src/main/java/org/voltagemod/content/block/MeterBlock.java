package org.voltagemod.content.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.voltagemod.content.blockentity.MeterBlockEntity;

import javax.annotation.Nullable;

public class MeterBlock extends ElectricalBlock {
    public MeterBlock(Properties properties) { super(properties); }
    @Nullable @Override public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new MeterBlockEntity(); }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof MeterBlockEntity) {
                MeterBlockEntity meter = (MeterBlockEntity) te;
                player.displayClientMessage(new StringTextComponent(String.format("V=%.2f V, I=%.3f A, P=%.2f W, PF=%.3f",
                        meter.getVoltage(), meter.getCurrent(), meter.getPower(), meter.getPowerFactor())), false);
            }
        }
        return ActionResultType.sidedSuccess(world.isClientSide);
    }
}
