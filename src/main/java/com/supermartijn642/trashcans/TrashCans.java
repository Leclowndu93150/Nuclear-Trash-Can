package com.supermartijn642.trashcans;

import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.gui.BaseContainerType;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.item.ItemProperties;
import com.supermartijn642.core.network.PacketChannel;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import com.supermartijn642.core.registry.RegistryEntryAcceptor;
import com.supermartijn642.trashcans.compat.Compatibility;
import com.supermartijn642.trashcans.filter.FluidFilterManager;
import com.supermartijn642.trashcans.filter.NuclearTrashCanFilters;
import com.supermartijn642.trashcans.generators.*;
import com.supermartijn642.trashcans.packet.*;
import com.supermartijn642.trashcans.screen.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("trashcans")
public class TrashCans {

    public static final PacketChannel CHANNEL = PacketChannel.create("trashcans");

    @RegistryEntryAcceptor(namespace = "trashcans", identifier = "nuclear_trash_can", registry = RegistryEntryAcceptor.Registry.BLOCKS)
    public static BaseBlock nuclear_trash_can;
    @RegistryEntryAcceptor(namespace = "trashcans", identifier = "nuclear_trash_can_tile", registry = RegistryEntryAcceptor.Registry.BLOCK_ENTITY_TYPES)
    public static BaseBlockEntityType<TrashCanBlockEntity> nuclear_trash_can_tile;

    @RegistryEntryAcceptor(namespace = "trashcans", identifier = "nuclear_trash_can_container", registry = RegistryEntryAcceptor.Registry.MENU_TYPES)
    public static BaseContainerType<TrashCanContainer> nuclear_trash_can_container;

    public TrashCans(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);

        CHANNEL.registerMessage(PacketToggleNuclearWhitelist.class, PacketToggleNuclearWhitelist::new, true);
        CHANNEL.registerMessage(PacketChangeNuclearFilter.class, PacketChangeNuclearFilter::new, true);

        TrashCansConfig.init();

        register();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> TrashCansClient::registerScreens);
        registerGenerators();
    }

    public void init(FMLCommonSetupEvent e){
        NuclearTrashCanFilters.register(new FluidFilterManager(), "fluid");
        Compatibility.init();
    }

    private static void register(){
        RegistrationHandler handler = RegistrationHandler.get("trashcans");

        handler.registerBlock("nuclear_trash_can", () -> new TrashCanBlock(() -> nuclear_trash_can_tile, NuclearTrashCanContainer::new));
        handler.registerBlockEntityType("nuclear_trash_can_tile", () -> BaseBlockEntityType.create((pos, state) -> new TrashCanBlockEntity(nuclear_trash_can_tile, pos, state, false, true, false), nuclear_trash_can));
        handler.registerItem("nuclear_trash_can", () -> new BaseBlockItem(nuclear_trash_can, ItemProperties.create().group(CreativeItemGroup.getFunctionalBlocks())));
        handler.registerMenuType("nuclear_trash_can_container", BaseContainerType.create((container, buffer) -> buffer.writeBlockPos(container.getBlockEntityPos()), (player, buffer) -> new NuclearTrashCanContainer(player, buffer.readBlockPos())));

    }

    private static void registerGenerators(){
        GeneratorRegistrationHandler handler = GeneratorRegistrationHandler.get("trashcans");
        handler.addGenerator(TrashCansAdvancementGenerator::new);
        handler.addGenerator(TrashCansModelGenerator::new);
        handler.addGenerator(TrashCansBlockStateGenerator::new);
        handler.addGenerator(TrashCansLanguageGenerator::new);
        handler.addGenerator(TrashCansLootTableGenerator::new);
        handler.addGenerator(TrashCansRecipeGenerator::new);
        handler.addGenerator(TrashCansTagGenerator::new);
    }
}
