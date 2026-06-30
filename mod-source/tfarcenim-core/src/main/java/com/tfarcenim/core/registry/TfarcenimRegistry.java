package com.tfarcenim.core.registry;

import com.tfarcenim.core.TfarcenimCore;
import com.tfarcenim.core.enchantment.StarPowerEnchantment;
import com.tfarcenim.core.item.GuideBookItem;

/**
 * TfarcenimRegistry — unified registration entry point.
 *
 * <p>All custom enchantments, items, and other registry entries are
 * registered here in a single call from {@code TfarcenimCore.onInitialize()}.
 *
 * <p>This keeps the main entry class clean and centralises registration logic.
 */
public class TfarcenimRegistry {

    /**
     * Registers all TFARCENIM custom content.
     * Called once during mod initialization.
     */
    public static void registerAll() {
        TfarcenimCore.LOGGER.info("Registering TFARCENIM custom content...");

        // Register custom enchantments
        StarPowerEnchantment.register();

        // Register custom items
        GuideBookItem.register();

        TfarcenimCore.LOGGER.info("TFARCENIM custom content registered successfully.");
    }
}
