package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansLanguageGenerator extends LanguageGenerator {

    public TrashCansLanguageGenerator(ResourceCache cache){
        super("trashcans", cache, "en_us");
    }

    @Override
    public void generate(){
        // Blocks
        this.block(TrashCans.liquid_trash_can, "Fluid Trash Can");

        this.translation("trashcans.liquid_trash_can.info", "Can void liquids and gasses, also contains a filter for up to 9 fluids");

        this.translation("trashcans.advancement.trash_can.title", "Don't forget to recycle");
        this.translation("trashcans.advancement.trash_can.description", "Craft a trash can");

        this.translation("trashcans.gui.liquid_trash_can.title", "Liquid Trash Can");
        this.translation("trashcans.gui.liquid_trash_can.filter", "Filter");
        this.translation("trashcans.gui.whitelist.on", "Whitelist");
        this.translation("trashcans.gui.whitelist.off", "Blacklist");
        this.translation("trashcans.gui.arrow.left", "Decrease energy limit");
        this.translation("trashcans.gui.arrow.right", "Increase energy limit");
    }
}
