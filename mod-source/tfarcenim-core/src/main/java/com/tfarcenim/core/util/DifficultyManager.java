package com.tfarcenim.core.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * DifficultyManager — singleton that tracks the current world difficulty
 * and provides convenience queries used by every enchantment / entity Mixin.
 *
 * <p>Difficulty rules (per V6 design):
 * <ul>
 *   <li><b>EASY</b> — original enchantment effects (no modification)</li>
 *   <li><b>NORMAL</b> — enhanced enchantments (×1.5 multiplier)</li>
 *   <li><b>HARD</b> — reversed enchantment effects</li>
 *   <li><b>HARDCORE</b> — reversed effects + 10 HP limit</li>
 * </ul>
 */
public class DifficultyManager {

    private static DifficultyManager instance;

    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private DifficultyManager() {
        // private constructor for singleton pattern
    }

    /**
     * Returns the singleton instance, creating it if necessary.
     *
     * @return the global {@link DifficultyManager} instance
     */
    public static DifficultyManager getInstance() {
        if (instance == null) {
            instance = new DifficultyManager();
        }
        return instance;
    }

    /**
     * Updates the cached difficulty from the given world.
     * Called periodically (e.g. on server tick or world load).
     *
     * @param world the world to read difficulty from
     */
    public void updateDifficulty(World world) {
        this.currentDifficulty = world.getDifficulty();
    }

    /**
     * Sets the difficulty directly (used for testing or explicit overrides).
     *
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    /**
     * Whether enchantments should be reversed (HARD or HARDCORE).
     *
     * @return {@code true} if enchantment effects should be inverted
     */
    public boolean isReversedEnchantments() {
        return currentDifficulty == Difficulty.HARD || currentDifficulty == Difficulty.HARDCORE;
    }

    /**
     * Whether enchantments should be enhanced with a 1.5× multiplier (NORMAL).
     *
     * @return {@code true} if enchantment effects should be amplified
     */
    public boolean isEnhancedEnchantments() {
        return currentDifficulty == Difficulty.NORMAL;
    }

    /**
     * Whether enchantments should use original vanilla effects (EASY).
     *
     * @return {@code true} if vanilla behaviour should be preserved
     */
    public boolean isOriginalEnchantments() {
        return currentDifficulty == Difficulty.EASY;
    }

    /**
     * Whether the hardcore 10-HP limit is active.
     *
     * @return {@code true} if the current difficulty is HARDCORE
     */
    public boolean isHardcore() {
        return currentDifficulty == Difficulty.HARDCORE;
    }

    /**
     * Whether the current difficulty is EASY.
     *
     * @return {@code true} if EASY
     */
    public boolean isEasy() {
        return currentDifficulty == Difficulty.EASY;
    }

    /**
     * Whether the current difficulty is NORMAL.
     *
     * @return {@code true} if NORMAL
     */
    public boolean isNormal() {
        return currentDifficulty == Difficulty.NORMAL;
    }

    /**
     * Returns the raw {@link Difficulty} enum value.
     *
     * @return the current difficulty
     */
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * The enchantment multiplier applied in NORMAL mode.
     */
    public static final float ENHANCEMENT_MULTIPLIER = 1.5f;
}
