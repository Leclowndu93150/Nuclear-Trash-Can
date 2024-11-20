package com.supermartijn642.trashcans.packet;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.trashcans.TrashCanBlockEntity;
import com.supermartijn642.trashcans.filter.ItemFilter;
import com.supermartijn642.trashcans.filter.NuclearTrashCanFilters;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class PacketChangeNuclearFilter extends BlockEntityBasePacket<TrashCanBlockEntity> {
    private int filterSlot;
    private ItemFilter filter;

    public PacketChangeNuclearFilter(BlockPos pos, int filterSlot, ItemFilter filter){
        super(pos);
        this.filterSlot = filterSlot;
        this.filter = filter;
    }

    public PacketChangeNuclearFilter(){
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        super.write(buffer);
        buffer.writeInt(this.filterSlot);
        buffer.writeNbt(NuclearTrashCanFilters.write(this.filter));
    }

    @Override
    public void read(FriendlyByteBuf buffer){
        super.read(buffer);
        this.filterSlot = buffer.readInt();
        this.filter = NuclearTrashCanFilters.read(buffer.readNbt());
    }

    @Override
    public boolean verify(PacketContext context){
        return this.filterSlot >= 0 && this.filterSlot < 9;
    }

    @Override
    protected void handle(TrashCanBlockEntity entity, PacketContext context){
        if(entity.nuclears){
            entity.nuclearFilter.set(this.filterSlot, this.filter);
            entity.dataChanged();
        }
    }
}
