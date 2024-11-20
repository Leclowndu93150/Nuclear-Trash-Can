package com.supermartijn642.trashcans.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.gui.ScreenUtils;
import com.supermartijn642.trashcans.TrashCanBlockEntity;
import com.supermartijn642.trashcans.TrashCans;
import com.supermartijn642.trashcans.packet.PacketToggleNuclearWhitelist;

/**
 * Created 7/11/2020 by SuperMartijn642
 */
public class NuclearTrashCanScreen extends TrashCanScreen<NuclearTrashCanContainer> {

    private WhitelistButton whitelistButton;

    public NuclearTrashCanScreen(){
        super("trashcans.gui.nuclear_trash_can.title");
    }

    @Override
    protected void addWidgets(TrashCanBlockEntity entity){
        this.whitelistButton = this.addWidget(new WhitelistButton(175, this.height() - 118, () -> TrashCans.CHANNEL.sendToServer(new PacketToggleNuclearWhitelist(this.container.getBlockEntityPos()))));
        this.whitelistButton.update(entity.nuclearFilterWhitelist);
    }

    @Override
    protected void update(TrashCanBlockEntity entity){
        super.update(entity);
        this.whitelistButton.update(entity.nuclearFilterWhitelist);
    }

    @Override
    protected String getBackground(){
        return "nuclear_screen.png";
    }

    @Override
    protected void drawText(PoseStack poseStack, TrashCanBlockEntity entity){
        ScreenUtils.drawString(poseStack, TextComponents.translation("trashcans.gui.nuclear_trash_can.filter").get(), 8, 52);
    }
}
