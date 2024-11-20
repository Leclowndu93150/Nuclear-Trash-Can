package com.supermartijn642.trashcans.packet;

import com.supermartijn642.core.network.BlockEntityBasePacket;
import com.supermartijn642.core.network.PacketContext;
import com.supermartijn642.trashcans.TrashCanBlockEntity;
import net.minecraft.core.BlockPos;

/**
 * Created 7/8/2020 by SuperMartijn642
 */
public class PacketToggleNuclearWhitelist extends BlockEntityBasePacket<TrashCanBlockEntity> {

    public PacketToggleNuclearWhitelist(BlockPos pos){
        super(pos);
    }

    public PacketToggleNuclearWhitelist(){
    }

    @Override
    protected void handle(TrashCanBlockEntity entity, PacketContext context){
        if(entity.nuclears){
            entity.nuclearFilterWhitelist = !entity.nuclearFilterWhitelist;
            entity.dataChanged();
        }
    }
}
