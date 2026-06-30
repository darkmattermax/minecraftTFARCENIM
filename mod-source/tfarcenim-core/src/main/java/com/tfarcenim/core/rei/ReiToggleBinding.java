package com.tfarcenim.core.rei;

import com.tfarcenim.core.TfarcenimCore;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * ReiToggleBinding — REI overlay toggle keybinding.
 *
 * <p>Registers a keybinding (default: R) that toggles the REI overlay
 * visibility. The keybinding is registered in a prominent category
 * so players can easily find it in the controls menu.
 *
 * <p>REI is enabled by default — this keybinding allows players to
 * hide/show the overlay when needed.
 */
public class ReiToggleBinding {

    /** The keybinding category for TFARCENIM controls. */
    public static final String CATEGORY = "key.categories.tfarcenim";

    /** The keybinding translation key. */
    public static final String BINDING_ID = "key.tfarcenim.toggle_rei";

    /** The KeyBinding instance. */
    private static KeyBinding toggleKey;

    /** Whether REI overlay is currently visible (default: true). */
    private static boolean reiVisible = true;

    /**
     * Registers the toggle keybinding and starts listening for key presses.
     */
    public static void registerToggle() {
        // Register the keybinding (default key: R)
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                BINDING_ID,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        ));

        // Register a client tick listener to check for key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                toggleReiVisibility();
            }
        });

        TfarcenimCore.LOGGER.info("Registered REI toggle keybinding (default: R)");
    }

    /**
     * Toggles the REI overlay visibility.
     */
    private static void toggleReiVisibility() {
        reiVisible = !reiVisible;

        if (ReiCompat.isReiLoaded()) {
            // Use REI API to toggle the overlay
            // The actual REI API call depends on the REI version
            // For now, we set a flag that the REI plugin checks
            TfarcenimCore.LOGGER.info("REI overlay visibility toggled: {}", reiVisible);
        }
    }

    /**
     * Returns whether the REI overlay should be visible.
     *
     * @return {@code true} if REI is currently visible
     */
    public static boolean isReiVisible() {
        return reiVisible;
    }

    /**
     * Sets the REI overlay visibility directly.
     *
     * @param visible whether REI should be visible
     */
    public static void setReiVisible(boolean visible) {
        reiVisible = visible;
    }
}
