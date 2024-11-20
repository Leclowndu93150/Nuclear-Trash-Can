package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.AdvancementGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansAdvancementGenerator extends AdvancementGenerator {

    public TrashCansAdvancementGenerator(ResourceCache cache){
        super("trashcans", cache);
    }

    @Override
    public void generate(){
        this.advancement("trash_can")
            .icon(TrashCans.nuclear_trash_can)
            .background("minecraft", "block/cobblestone")
            .hasItemsCriterion("has_nuclear_trash_can", TrashCans.nuclear_trash_can)
            .requirementGroup("has_nuclear_trash_can");
    }
}
