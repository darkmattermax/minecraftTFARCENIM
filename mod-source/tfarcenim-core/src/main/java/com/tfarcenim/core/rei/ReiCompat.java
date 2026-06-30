package com.tfarcenim.core.rei;

import com.tfarcenim.core.TfarcenimCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/**
 * ReiCompat — REI (Roughly Enough Items) compatibility integration.
 *
 * <p>Handles REI API integration on the client side:
 * <ul>
 *   <li>Registers custom recipe categories if needed</li>
 *   <li>Ensures TFARCENIM recipes are visible in REI</li>
 *   <li>Provides a toggle to show/hide REI overlay</li>
 * </ul>
 *
 * <p>REI is an optional dependency — if not present, this class safely
 * skips registration.
 */
public class ReiCompat {

    /** Whether REI is loaded and available. */
    private static boolean reiLoaded = false;

    /**
     * Initializes REI compatibility. Called from {@code TfarcenimCoreClient}.
     */
    public static void initialize() {
        reiLoaded = FabricLoader.getInstance().isModLoaded("roughlyenoughitems");

        if (!reiLoaded) {
            TfarcenimCore.LOGGER.info("REI not detected — skipping REI compatibility.");
            return;
        }

        TfarcenimCore.LOGGER.info("REI detected — initializing compatibility...");

        // Register REI plugin entries
        registerRecipes();

        // Register the toggle keybinding
        ReiToggleBinding.registerToggle();

        TfarcenimCore.LOGGER.info("REI compatibility initialized successfully.");
    }

    /**
     * Registers custom recipe entries with REI.
     * Ensures all TFARCENIM recipes are visible in the recipe browser.
     */
    private static void registerRecipes() {
        // REI recipe registration is handled via the REI API entrypoint.
        // The actual entry is registered in fabric.mod.json under
        // "roughlyenoughitems" entrypoint if REI is present.
        //
        // For now, we rely on REI's default recipe discovery which
        // automatically picks up data-driven recipes from the datapack.
        TfarcenimCore.LOGGER.info("REI recipe registration delegated to default plugin.");
    }

    /**
     * Returns whether REI is loaded.
     *
     * @return {@code true} if REI is available
     */
    public static boolean isReiLoaded() {
        return reiLoaded;
    }
}
