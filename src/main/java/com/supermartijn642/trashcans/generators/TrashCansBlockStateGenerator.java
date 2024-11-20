package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansBlockStateGenerator extends BlockStateGenerator {

    public TrashCansBlockStateGenerator(ResourceCache cache){
        super("trashcans", cache);
    }

    @Override
    public void generate(){
        this.blockState(TrashCans.liquid_trash_can).emptyVariant(builder -> builder.model("liquid_trash_can"));
    }
}
