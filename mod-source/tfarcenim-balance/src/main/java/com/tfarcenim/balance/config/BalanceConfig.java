package com.tfarcenim.balance.config;

import java.util.Locale;
import java.util.Map;

/**
 * Centralized balance configuration for the TFARCENIM Balance module.
 *
 * <p>All numerical overrides live in this single class so that balance
 * tweaks require editing one file rather than scattering magic numbers
 * across Mixins. Every Mixin in {@code com.tfarcenim.balance.mixin} reads
 * its values exclusively from here.
 *
 * <p>Values are sourced from the V6 design document
 * ({@code TFARCENIM-complete-changes-v6.md}, §武器 / 工具 / 防具 / 食物数值表).
 * Each constant is annotated with its vanilla baseline for traceability.
 *
 * <p>The class exposes two complementary surfaces:
 * <ul>
 *   <li><b>Static-final constants</b> — referenced directly by Mixins for
 *       maximum readability and zero allocation.</li>
 *   <li><b>Map-backed getters</b> ({@link #getToolDurability(String)} etc.) —
 *       fulfill the architecture contract and allow external/runtime lookup
 *       by material name (used by config screens / REI tooltips).</li>
 * </ul>
 */
public final class BalanceConfig {

    private BalanceConfig() {
        // Utility class — no instances.
    }

    // =========================================================================
    //  Tool material durability (per-material, applied via ToolMaterialMixin)
    //  All six vanilla tiers gain the "twine + leather wrapping" durability bump.
    // =========================================================================
    /** Vanilla 59. */
    public static final int WOOD_DURABILITY = 65;
    /** Vanilla 32. */
    public static final int GOLD_DURABILITY = 35;
    /** Vanilla 131. */
    public static final int STONE_DURABILITY = 145;
    /** Vanilla 250. */
    public static final int IRON_DURABILITY = 275;
    /** Vanilla 1561. */
    public static final int DIAMOND_DURABILITY = 1717;
    /** Vanilla 2031 — sword/pickaxe baseline; star-power durability +18%. */
    public static final int NETHERITE_DURABILITY = 2400;

    // =========================================================================
    //  Tool material mining speed (ToolMaterialMixin)
    //  Only netherite gains the "star power" mining-speed bonus (+11%).
    // =========================================================================
    /** Vanilla 9.0 — netherite pickaxe star-power speed. */
    public static final float NETHERITE_MINING_SPEED = 10.0f;

    // =========================================================================
    //  Tool material attack damage baseline (ToolMaterialMixin#getAttackDamage)
    //  Netherite +2 lifts BOTH sword (8→10) and axe (10→12) because both derive
    //  from the material attack-damage baseline in 1.21.x. Per-weapon attack
    //  SPEED is handled separately in AttributeModifierMixin.
    // =========================================================================
    /** Vanilla 4.0 — star-power +25% weapon damage. */
    public static final float NETHERITE_ATTACK_DAMAGE = 6.0f;
    /** Target totals for documentation / assertions. */
    public static final float NETHERITE_SWORD_DAMAGE = 10.0f; // vanilla 8.0
    public static final float NETHERITE_AXE_DAMAGE = 12.0f;   // vanilla 10.0

    // =========================================================================
    //  Ranged weapons (BowItemMixin / CrossbowItemMixin)
    // =========================================================================
    /** Vanilla 384 — bone-laminated bow (+17%). */
    public static final int BOW_DURABILITY = 450;
    /** Vanilla 326 — bone + leather crossbow (+17%). */
    public static final int CROSSBOW_DURABILITY = 380;

    // =========================================================================
    //  Defensive / utility items
    // =========================================================================
    /** Vanilla 336 — leather-backed shield (+7%). */
    public static final int SHIELD_DURABILITY = 360;
    /** Vanilla 64 — bone-shaft fishing rod (+17%). */
    public static final int FISHING_ROD_DURABILITY = 75;
    /** Vanilla 238 — flint-honed shears (+9%). */
    public static final int SHEARS_DURABILITY = 260;
    /** Vanilla 275 — twine-stitched turtle shell (+9%). */
    public static final int TURTLE_SHELL_DURABILITY = 300;
    /** Vanilla 2 — turtle shell defense +1 (star-power light armor). */
    public static final int TURTLE_SHELL_DEFENSE = 3;

    // =========================================================================
    //  Armor defense / durability arrays
    //  Order = [feet, legs, chest, head]  (matches the classic ArmorMaterials
    //  protection/durability array indexing used by EquipmentType FEET=0..HEAD=3).
    // =========================================================================
    /** Vanilla {1,2,3,1}. */
    public static final int[] LEATHER_DEFENSE = {2, 3, 4, 2};
    /** Vanilla {65,75,80,55}. */
    public static final int[] LEATHER_DURABILITY = {71, 82, 88, 60};

    /** Vanilla {2,5,6,2}. */
    public static final int[] IRON_DEFENSE = {3, 6, 7, 3};
    /** Vanilla {195,225,240,165}. */
    public static final int[] IRON_DURABILITY = {212, 245, 260, 180};

    /** Vanilla {2,5,6,2} (gold mirrors iron protection). */
    public static final int[] GOLD_DEFENSE = {3, 6, 7, 3};
    /** Vanilla {77,105,112,77}. */
    public static final int[] GOLD_DURABILITY = {99, 114, 122, 84};

    /** Vanilla {3,6,8,3} — defense unchanged, durability +10%. */
    public static final int[] DIAMOND_DEFENSE = {3, 6, 8, 3};
    /** Vanilla {429,495,528,363}. */
    public static final int[] DIAMOND_DURABILITY = {470, 545, 580, 400};

    /** Vanilla {3,6,8,3} — star-power defense boost. */
    public static final int[] NETHERITE_DEFENSE = {4, 8, 10, 4};
    /** Vanilla {481,555,592,407}. */
    public static final int[] NETHERITE_DURABILITY = {530, 610, 650, 450};

    // Frequently-referenced single values (documentation + direct refs).
    /** Vanilla 1. */
    public static final int LEATHER_HELMET_DEFENSE = 2;
    /** Vanilla 3. */
    public static final int LEATHER_CHEST_DEFENSE = 4;
    /** Vanilla 3. */
    public static final int NETHERITE_HELMET_DEFENSE = 4;
    /** Vanilla 8. */
    public static final int NETHERITE_CHEST_DEFENSE = 10;

    // =========================================================================
    //  Food — enchanted golden apple (GoldenAppleMixin)
    //  Vanilla enchanted golden apple: nutrition 4 / saturation 9.6.
    //  TFARCENIM: nutrition 8 / saturation 16. The delta is applied on top of
    //  the vanilla FOOD component at finishUsing time.
    // =========================================================================
    public static final int ENCHANTED_GOLDEN_APPLE_NUTRITION = 8;
    public static final float ENCHANTED_GOLDEN_APPLE_SATURATION = 16.0f;
    /** Bonus added on top of vanilla nutrition (8 - 4). */
    public static final int EGA_NUTRITION_BONUS =
            ENCHANTED_GOLDEN_APPLE_NUTRITION - 4;
    /** Bonus added on top of vanilla saturation (16.0 - 9.6). */
    public static final float EGA_SATURATION_BONUS =
            ENCHANTED_GOLDEN_APPLE_SATURATION - 9.6f;

    // =========================================================================
    //  Star-power attribute modifier (AttributeModifierMixin)
    //  Netherite sword attack speed: 1.6 → 1.9 (+0.3, +19%).
    // =========================================================================
    /** Namespace for star-power attribute modifiers (the balance mod id). */
    public static final String STAR_POWER_NAMESPACE = "tfarcenim-balance";
    /** Path component of the star-power attack-speed modifier identifier. */
    public static final String STAR_POWER_SPEED_PATH = "star_power_attack_speed";
    /** Attack-speed bonus added to the netherite sword (4.0 base → 1.6 → 1.9). */
    public static final double STAR_POWER_ATTACK_SPEED_BONUS = 0.3;
    /** Attack-speed target for documentation. */
    public static final float NETHERITE_SWORD_ATTACK_SPEED = 1.9f;

    // =========================================================================
    //  Runtime toggle
    // =========================================================================
    /** Master switch; when false every Mixin short-circuits to vanilla. */
    public static boolean ENABLED = true;

    // =========================================================================
    //  Map-backed getters (architecture contract — lookup by material name).
    // =========================================================================
    private static final Map<String, Integer> TOOL_DURABILITY = Map.of(
            "wood", WOOD_DURABILITY,
            "gold", GOLD_DURABILITY,
            "stone", STONE_DURABILITY,
            "iron", IRON_DURABILITY,
            "diamond", DIAMOND_DURABILITY,
            "netherite", NETHERITE_DURABILITY);

    private static final Map<String, Float> TOOL_SPEED = Map.of(
            "wood", 2.0f,
            "gold", 12.0f,
            "stone", 4.0f,
            "iron", 6.0f,
            "diamond", 8.0f,
            "netherite", NETHERITE_MINING_SPEED);

    /** @return TFARCENIM durability for the named material, or {@code -1} if unknown. */
    public static int getToolDurability(String material) {
        return TOOL_DURABILITY.getOrDefault(normalize(material), -1);
    }

    /** @return TFARCENIM mining speed for the named material, or {@code -1f} if unknown. */
    public static float getToolSpeed(String material) {
        return TOOL_SPEED.getOrDefault(normalize(material), -1.0f);
    }

    /** @return TFARCENIM armor defense for {@code material} at {@code slotIndex}
     *          (0=feet,1=legs,2=chest,3=head), or {@code -1} if unknown. */
    public static int getArmorDefense(String material, int slotIndex) {
        int[] arr = defenseArray(material);
        return (arr != null && slotIndex >= 0 && slotIndex < arr.length)
                ? arr[slotIndex] : -1;
    }

    /** @return TFARCENIM armor durability for {@code material} at {@code slotIndex}. */
    public static int getArmorDurability(String material, int slotIndex) {
        int[] arr = durabilityArray(material);
        return (arr != null && slotIndex >= 0 && slotIndex < arr.length)
                ? arr[slotIndex] : -1;
    }

    /** @return the defense array [feet,legs,chest,head] for {@code material}, or {@code null}. */
    public static int[] defenseArray(String material) {
        return switch (normalize(material)) {
            case "leather" -> LEATHER_DEFENSE;
            case "iron" -> IRON_DEFENSE;
            case "gold" -> GOLD_DEFENSE;
            case "diamond" -> DIAMOND_DEFENSE;
            case "netherite" -> NETHERITE_DEFENSE;
            default -> null;
        };
    }

    /** @return the durability array [feet,legs,chest,head] for {@code material}, or {@code null}. */
    public static int[] durabilityArray(String material) {
        return switch (normalize(material)) {
            case "leather" -> LEATHER_DURABILITY;
            case "iron" -> IRON_DURABILITY;
            case "gold" -> GOLD_DURABILITY;
            case "diamond" -> DIAMOND_DURABILITY;
            case "netherite" -> NETHERITE_DURABILITY;
            default -> null;
        };
    }

    private static String normalize(String material) {
        return material == null ? "" : material.toLowerCase(Locale.ROOT);
    }
}
