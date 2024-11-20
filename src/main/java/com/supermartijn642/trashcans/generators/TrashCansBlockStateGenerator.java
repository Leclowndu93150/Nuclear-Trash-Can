package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansBlockStateGenerator extends BlockStateGenerator {

    public TrashCansBlockStateGenerator(ResourceCache cache){
        super("nucleartrashcan", cache);
    }

    @Override
    public void generate(){
        this.blockState(TrashCans.nuclear_trash_can).emptyVariant(builder -> builder.model("nuclear_trash_can"));
    }
}
