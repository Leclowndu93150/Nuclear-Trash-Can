package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansLanguageGenerator extends LanguageGenerator {

    public TrashCansLanguageGenerator(ResourceCache cache){
        super("nucleartrashcan", cache, "en_us");
    }

    @Override
    public void generate(){
        // Blocks
        this.block(TrashCans.nuclear_trash_can, "Nuclear Trash Can");

        this.translation("nucleartrashcan.nuclear_trash_can.info", "Can void nuclear waste and gasses, also contains a filter for up to 9 fluids");

        this.translation("nucleartrashcan.advancement.trash_can.title", "Don't forget to recycle");
        this.translation("nucleartrashcan.advancement.trash_can.description", "Craft a trash can");

        this.translation("nucleartrashcan.gui.nuclear_trash_can.title", "Nuclear Trash Can");
        this.translation("nucleartrashcan.gui.nuclear_trash_can.filter", "Filter");
        this.translation("nucleartrashcan.gui.whitelist.on", "Whitelist");
        this.translation("nucleartrashcan.gui.whitelist.off", "Blacklist");
        this.translation("nucleartrashcan.gui.arrow.left", "Decrease energy limit");
        this.translation("nucleartrashcan.gui.arrow.right", "Increase energy limit");
    }
}
