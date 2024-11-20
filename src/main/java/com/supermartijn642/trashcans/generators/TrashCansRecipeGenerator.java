package com.supermartijn642.trashcans.generators;

import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.trashcans.TrashCans;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

/**
 * Created 08/09/2022 by SuperMartijn642
 */
public class TrashCansRecipeGenerator extends RecipeGenerator {

    public TrashCansRecipeGenerator(ResourceCache cache){
        super("nucleartrashcan", cache);
    }

    @Override
    public void generate(){
        this.shaped(TrashCans.nuclear_trash_can)
            .pattern("AAA")
            .pattern("BCB")
            .pattern("BBB")
            .input('A', Tags.Items.STONE)
            .input('B', Tags.Items.COBBLESTONE)
            .input('C', Items.BUCKET)
            .unlockedBy(Items.BUCKET);
    }
}
