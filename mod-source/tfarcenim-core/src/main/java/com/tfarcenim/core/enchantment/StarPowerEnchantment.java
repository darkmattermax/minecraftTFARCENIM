package com.tfarcenim.core.enchantment;

import com.tfarcenim.core.TfarcenimCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * StarPowerEnchantment — 星辰之力 custom enchantment.
 *
 * <p>This enchantment represents the "Star Power" mechanic in TFARCENIM.
 * It is applied to diamond items that have been combined with a Nether Star
 * at a crafting table. The enchantment provides:
 * <ul>
 *   <li>Increased attack damage</li>
 *   <li>Increased attack speed</li>
 *   <li>Increased durability</li>
 *   <li>Increased defense (for armor)</li>
 *   <li>Increased knockback resistance</li>
 * </ul>
 *
 * <p>The actual attribute modifiers are applied via a Mixin on the item
 * attribute calculation, not through the enchantment's own methods.
 */
public class StarPowerEnchantment extends Enchantment {

    /** The registry identifier for this enchantment. */
    public static final Identifier ID = new Identifier("tfarcenim", "star_power");

    /**
     * Constructs the Star Power enchantment with default properties.
     * It is applicable to all weapons and tools (DIGGER target).
     */
    public StarPowerEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.DIGGER, new EquipmentSlot[]{
                EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
        });
    }

    /**
     * Returns the minimum enchantment level (always 1).
     */
    @Override
    public int getMinLevel() {
        return 1;
    }

    /**
     * Returns the maximum enchantment level (always 1 — star power is a
     * binary enchantment, either present or not).
     */
    @Override
    public int getMaxLevel() {
        return 1;
    }

    // ========== Attribute Modifier Getters ==========
    // These are queried by the balance mod's AttributeModifierMixin
    // to apply star power bonuses to diamond items.

    /**
     * Returns the attack damage bonus provided by Star Power.
     *
     * @return additional attack damage (in half-hearts)
     */
    public int getDamageModifier() {
        return 2;
    }

    /**
     * Returns the attack speed bonus provided by Star Power.
     *
     * @return attack speed multiplier (0.1 = +10% speed)
     */
    public float getAttackSpeedModifier() {
        return 0.1f;
    }

    /**
     * Returns the durability bonus multiplier provided by Star Power.
     *
     * @return durability multiplier (0.5 = +50% durability)
     */
    public float getDurabilityModifier() {
        return 0.5f;
    }

    /**
     * Returns the defense bonus provided by Star Power (for armor).
     *
     * @return additional defense points
     */
    public int getDefenseModifier() {
        return 2;
    }

    /**
     * Returns the knockback resistance bonus provided by Star Power.
     *
     * @return additional knockback resistance (0-100)
     */
    public int getKnockbackResistanceModifier() {
        return 10;
    }

    /**
     * Registers this enchantment in the vanilla enchantment registry.
     * Called from {@code TfarcenimRegistry}.
     */
    public static void register() {
        Registry.register(Registries.ENCHANTMENT, ID, new StarPowerEnchantment());
        TfarcenimCore.LOGGER.info("Registered Star Power enchantment");
    }
}
