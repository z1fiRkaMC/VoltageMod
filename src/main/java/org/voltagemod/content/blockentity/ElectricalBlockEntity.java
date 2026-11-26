package org.voltagemod.content.blockentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import org.voltagemod.core.network.ElectricalNetwork;
import org.voltagemod.core.network.manager.ElectricalNetworkManager;
import org.voltagemod.core.simulation.ac.AcSolution;

import java.util.List;

public abstract class ElectricalBlockEntity extends TileEntity {
    protected boolean inNetwork;

    protected ElectricalBlockEntity(TileEntityType<?> type) {
        super(type);
    }

    public abstract String getComponentId();
    public abstract List<String> getTerminalIds();
    public abstract void addToNetwork(ElectricalNetwork network);
    public abstract void applySolution(AcSolution solution);
    public void clearSolution() {}

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            ElectricalNetworkManager.get((ServerWorld) level).register(this);
            inNetwork = true;
        }
    }

    public void onRemoved() {
        if (level != null && !level.isClientSide) {
            ElectricalNetworkManager.get((ServerWorld) level).unregister(this);
            inNetwork = false;
        }
    }

    @Override
    public void setRemoved() {
        onRemoved();
        super.setRemoved();
    }

    protected void markNetworkDirty() {
        if (level != null && !level.isClientSide) {
            ElectricalNetworkManager.get((ServerWorld) level).markDirty();
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        return super.save(tag);
    }

    @Override
    public void load(net.minecraft.block.BlockState state, CompoundNBT tag) {
        super.load(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public net.minecraft.network.play.server.SUpdateTileEntityPacket getUpdatePacket() {
        return new net.minecraft.network.play.server.SUpdateTileEntityPacket(worldPosition, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SUpdateTileEntityPacket pkt) {
        load(getBlockState(), pkt.getTag());
    }

    protected void syncToClient() {
        if (level != null && !level.isClientSide) {
            net.minecraft.block.BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
        }
    }
}
