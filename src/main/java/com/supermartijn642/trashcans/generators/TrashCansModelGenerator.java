package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansModelGenerator extends ModelGenerator {

    public TrashCansModelGenerator(ResourceCache cache){
        super("nucleartrashcan", cache);
    }

    @Override
    public void generate(){
        this.model("nuclear_trash_can").parent("trash_can").texture("all", "trash_can_nuclear");
        this.model("item/nuclear_trash_can").parent("nuclear_trash_can");
    }
}
