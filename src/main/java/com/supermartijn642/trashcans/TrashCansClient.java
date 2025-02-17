package com.supermartijn642.trashcans;

import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.trashcans.screen.*;

/**
 * Created 7/11/2020 by SuperMartijn642
 */
public class TrashCansClient {

    public static void registerScreens(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("nucleartrashcan");
        handler.registerContainerScreen(() -> TrashCans.nuclear_trash_can_container, container -> new TrashCanWidgetContainerScreen(new NuclearTrashCanScreen(), container, false));
    }
}
