package com.supermartijn642.trashcans;

import com.supermartijn642.core.block.BaseBlockEntity;
import com.supermartijn642.core.block.TickableBlockEntity;
import com.supermartijn642.trashcans.compat.Compatibility;
import com.supermartijn642.trashcans.filter.ItemFilter;
import com.supermartijn642.trashcans.filter.LiquidTrashCanFilters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created 7/10/2020 by SuperMartijn642
 */
public class TrashCanBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

    public static final int DEFAULT_ENERGY_LIMIT = 10000, MAX_ENERGY_LIMIT = 10000000, MIN_ENERGY_LIMIT = 1;

    public final IItemHandler ITEM_HANDLER = new IItemHandlerModifiable() {
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack){
        }

        @Override
        public int getSlots(){
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot){
            return ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
            for(ItemStack filter : TrashCanBlockEntity.this.itemFilter){
                if(!filter.isEmpty() && ItemStack.isSameItem(stack, filter))
                    return TrashCanBlockEntity.this.itemFilterWhitelist ? ItemStack.EMPTY : stack;
            }
            return TrashCanBlockEntity.this.itemFilterWhitelist ? stack : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot){
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack){
            for(ItemStack filter : TrashCanBlockEntity.this.itemFilter){
                if(!filter.isEmpty() && ItemStack.isSameItem(stack, filter))
                    return TrashCanBlockEntity.this.itemFilterWhitelist;
            }
            return !TrashCanBlockEntity.this.itemFilterWhitelist;
        }
    };

    public final IFluidHandler FLUID_HANDLER = new IFluidHandler() {
        @Override
        public int getTanks(){
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank){
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank){
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack){
            for(ItemFilter filter : TrashCanBlockEntity.this.liquidFilter){
                if(filter != null && filter.matches(stack))
                    return TrashCanBlockEntity.this.liquidFilterWhitelist;
            }
            return !TrashCanBlockEntity.this.liquidFilterWhitelist;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action){
            for(ItemFilter filter : TrashCanBlockEntity.this.liquidFilter){
                if(filter != null && filter.matches(resource))
                    return TrashCanBlockEntity.this.liquidFilterWhitelist ? resource.getAmount() : 0;
            }
            return TrashCanBlockEntity.this.liquidFilterWhitelist ? 0 : resource.getAmount();
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action){
            return FluidStack.EMPTY;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action){
            return FluidStack.EMPTY;
        }
    };
    public final IItemHandler LIQUID_ITEM_HANDLER = new IItemHandlerModifiable() {
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack){
            TrashCanBlockEntity.this.liquidItem = stack;
        }

        @Override
        public int getSlots(){
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot){
            return TrashCanBlockEntity.this.liquidItem;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
            if(!this.isItemValid(slot, stack) || !TrashCanBlockEntity.this.liquidItem.isEmpty() || stack.isEmpty())
                return stack;
            if(!simulate){
                TrashCanBlockEntity.this.liquidItem = stack.copy();
                TrashCanBlockEntity.this.liquidItem.setCount(1);
                TrashCanBlockEntity.this.dataChanged();
            }
            ItemStack stack1 = stack.copy();
            stack1.shrink(1);
            return stack1;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
            if(amount <= 0 || TrashCanBlockEntity.this.liquidItem.isEmpty())
                return ItemStack.EMPTY;
            ItemStack stack = TrashCanBlockEntity.this.liquidItem.copy();
            stack.setCount(Math.min(amount, stack.getCount()));
            if(!simulate){
                TrashCanBlockEntity.this.liquidItem.shrink(amount);
                TrashCanBlockEntity.this.dataChanged();
            }
            return stack;
        }

        @Override
        public int getSlotLimit(int slot){
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack){
            boolean filtered = !TrashCanBlockEntity.this.liquidFilterWhitelist;
            for(ItemFilter filter : TrashCanBlockEntity.this.liquidFilter){
                if(filter != null && filter.matches(stack)){
                    filtered = TrashCanBlockEntity.this.liquidFilterWhitelist;
                    break;
                }
            }
            if(!filtered)
                return false;

            return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).filter(handler -> {
                for(int tank = 0; tank < handler.getTanks(); tank++)
                    if(!handler.getFluidInTank(tank).isEmpty())
                        return true;
                return false;
            }).isPresent() ||
                Compatibility.MEKANISM.doesItemHaveGasStored(stack);
        }
    };

    public final IEnergyStorage ENERGY_STORAGE = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate){
            return TrashCanBlockEntity.this.useEnergyLimit ? Math.min(maxReceive, TrashCanBlockEntity.this.energyLimit) : maxReceive;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate){
            return 0;
        }

        @Override
        public int getEnergyStored(){
            return 0;
        }

        @Override
        public int getMaxEnergyStored(){
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract(){
            return false;
        }

        @Override
        public boolean canReceive(){
            return true;
        }
    };
    public final IItemHandler ENERGY_ITEM_HANDLER = new IItemHandlerModifiable() {
        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack){
            TrashCanBlockEntity.this.energyItem = stack;
        }

        @Override
        public int getSlots(){
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot){
            return TrashCanBlockEntity.this.energyItem;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
            if(!this.isItemValid(slot, stack) || !TrashCanBlockEntity.this.energyItem.isEmpty() || stack.isEmpty())
                return stack;
            if(!simulate){
                TrashCanBlockEntity.this.energyItem = stack.copy();
                TrashCanBlockEntity.this.energyItem.setCount(1);
                TrashCanBlockEntity.this.dataChanged();
            }
            ItemStack stack1 = stack.copy();
            stack1.shrink(1);
            return stack1;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate){
            if(amount <= 0 || TrashCanBlockEntity.this.energyItem.isEmpty())
                return ItemStack.EMPTY;
            ItemStack stack = TrashCanBlockEntity.this.energyItem.copy();
            stack.setCount(Math.min(amount, stack.getCount()));
            if(!simulate){
                TrashCanBlockEntity.this.energyItem.shrink(amount);
                TrashCanBlockEntity.this.dataChanged();
            }
            return stack;
        }

        @Override
        public int getSlotLimit(int slot){
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack){
            return stack.getCapability(ForgeCapabilities.ENERGY).filter(storage -> storage.canExtract() && storage.getEnergyStored() > 0).isPresent();
        }
    };

    public final boolean items;
    public final ArrayList<ItemStack> itemFilter = new ArrayList<>();
    public boolean itemFilterWhitelist = false;
    public final boolean liquids;
    public final ArrayList<ItemFilter> liquidFilter = new ArrayList<>();
    public boolean liquidFilterWhitelist = false;
    public ItemStack liquidItem = ItemStack.EMPTY;
    public final boolean energy;
    public int energyLimit = DEFAULT_ENERGY_LIMIT;
    public boolean useEnergyLimit = false;
    public ItemStack energyItem = ItemStack.EMPTY;

    public TrashCanBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, boolean items, boolean liquids, boolean energy){
        super(blockEntityType, pos, state);
        this.items = items;
        this.liquids = liquids;
        this.energy = energy;

        for(int i = 0; i < 9; i++){
            this.itemFilter.add(ItemStack.EMPTY);
            this.liquidFilter.add(null);
        }
    }

    @Override
    public void update(){
        if(this.liquids && !this.liquidItem.isEmpty() && this.liquidItem.getItem() != Items.BUCKET){
            this.liquidItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {
                boolean changed = false;
                for(int tank = 0; tank < fluidHandler.getTanks(); tank++)
                    if(!fluidHandler.getFluidInTank(tank).isEmpty()){
                        fluidHandler.drain(fluidHandler.getFluidInTank(tank), IFluidHandler.FluidAction.EXECUTE);
                        changed = true;
                    }
                if(changed){
                    this.liquidItem = fluidHandler.getContainer();
                    this.dataChanged();
                }
            });
            if(Compatibility.MEKANISM.drainGasFromItem(this.liquidItem))
                this.dataChanged();
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
        if(this.liquids){
            if(cap == ForgeCapabilities.FLUID_HANDLER)
                return LazyOptional.of(() -> this.FLUID_HANDLER).cast();
            else if(Compatibility.MEKANISM.isInstalled() && cap == Compatibility.MEKANISM.getGasHandlerCapability()){
                Object handler = Compatibility.MEKANISM.getGasHandler(this.liquidFilter, () -> TrashCanBlockEntity.this.liquidFilterWhitelist);
                return handler == null ? LazyOptional.empty() : LazyOptional.of(() -> handler).cast();
            }
        }
        return LazyOptional.empty();
    }

    @Override
    protected CompoundTag writeData(){
        CompoundTag tag = new CompoundTag();
        if(this.liquids){
            for(int i = 0; i < this.liquidFilter.size(); i++)
                if(this.liquidFilter.get(i) != null)
                    tag.put("liquidFilter" + i, LiquidTrashCanFilters.write(this.liquidFilter.get(i)));
            tag.putBoolean("liquidFilterWhitelist", this.liquidFilterWhitelist);
            if(!this.liquidItem.isEmpty())
                tag.put("liquidItem", this.liquidItem.save(new CompoundTag()));
        }
        return tag;
    }

    @Override
    protected void readData(CompoundTag tag){
        if(this.liquids){
            for(int i = 0; i < this.liquidFilter.size(); i++)
                this.liquidFilter.set(i, tag.contains("liquidFilter" + i) ? LiquidTrashCanFilters.read(tag.getCompound("liquidFilter" + i)) : null);
            this.liquidFilterWhitelist = tag.contains("liquidFilterWhitelist") && tag.getBoolean("liquidFilterWhitelist");
            this.liquidItem = tag.contains("liquidItem") ? ItemStack.of(tag.getCompound("liquidItem")) : ItemStack.EMPTY;
        }
    }
}
