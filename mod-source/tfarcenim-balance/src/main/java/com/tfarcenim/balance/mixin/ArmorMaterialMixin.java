package com.tfarcenim.balance.mixin;

import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.entity.EquipmentType;
import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides armor defense and durability for the five tiers that gain changes
 * in TFARCENIM: leather, iron, gold, diamond and netherite. Chainmail is left
 * untouched.
 *
 * <p>The turtle shell's <b>defense</b> boost (head slot 2 → 3) is also applied
 * here as a {@code TURTLE} special case, because the per-item defense accessor
 * is inherited and would not scope correctly in {@link TurtleShellMixin}. The
 * turtle shell's <b>durability</b> (275 → 300) is handled separately in
 * {@link TurtleShellMixin} via the item-identity-guarded {@code getMaxDamage}.
 *
 * <p>Defense/durability values are read from {@link BalanceConfig} arrays
 * indexed [feet=0, legs=1, chest=2, head=3], matching the classic
 * {@code ArmorMaterials} array ordering.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> targets {@code ArmorMaterials} via the
 * per-slot accessors {@code getDefenseForType(EquipmentType)} and
 * {@code getDurability(EquipmentType)}. If the 1.21.2+ armor-material registry
 * refactor renamed these (e.g. {@code defense(EquipmentType)} /
 * {@code durability(EquipmentType)}), adjust the {@code method} literals — the
 * dispatched values are unchanged.
 */
@Mixin(ArmorMaterials.class)
public class ArmorMaterialMixin {

    /**
     * Replaces per-slot defense with the TFARCENIM value for the five adjusted
     * tiers; other tiers keep their vanilla return value.
     *
     * @param type the armor slot type being queried.
     * @param cir  the return-value callback carrying the vanilla defense.
     */
    @Inject(method = "getDefenseForType", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyDefense(EquipmentType type,
                                         CallbackInfoReturnable<Integer> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        int idx = armorIndex(type);
        if (idx < 0) {
            return;
        }
        ArmorMaterials self = (ArmorMaterials) (Object) this;
        // Turtle shell is helmet-only: boost its head-slot defense 2 -> 3 here
        // (the per-item TurtleShellMixin cannot scope an inherited defense
        // accessor, so the material-level hook is the correctly-scoped place).
        if (self == ArmorMaterials.TURTLE && idx == 3) {
            cir.setReturnValue(BalanceConfig.TURTLE_SHELL_DEFENSE);
            return;
        }
        int[] defense = defenseArrayFor(self);
        if (defense != null) {
            cir.setReturnValue(defense[idx]);
        }
    }

    /**
     * Replaces per-slot durability with the TFARCENIM value for the five
     * adjusted tiers.
     *
     * @param type the armor slot type being queried.
     * @param cir  the return-value callback carrying the vanilla durability.
     */
    @Inject(method = "getDurability", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyDurability(EquipmentType type,
                                            CallbackInfoReturnable<Integer> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        int idx = armorIndex(type);
        if (idx < 0) {
            return;
        }
        int[] durability = durabilityArrayFor((ArmorMaterials) (Object) this);
        if (durability != null) {
            cir.setReturnValue(durability[idx]);
        }
    }

    /**
     * Maps an {@link EquipmentType} to the [feet, legs, chest, head] array index.
     *
     * @param type the armor slot type.
     * @return 0..3 for armor slots, or {@code -1} for non-armor slots (e.g. body).
     */
    private static int armorIndex(EquipmentType type) {
        if (type == null) {
            return -1;
        }
        // Use name-based comparison so this stays robust to enum reordering.
        return switch (type.asString()) {
            case "feet" -> 0;
            case "legs" -> 1;
            case "chest" -> 2;
            case "head" -> 3;
            default -> -1;
        };
    }

    /**
     * @return the TFARCENIM defense array for the given material, or
     *         {@code null} if the tier is not adjusted (chainmail / turtle).
     */
    private static int[] defenseArrayFor(ArmorMaterials material) {
        return switch (material) {
            case LEATHER -> BalanceConfig.LEATHER_DEFENSE;
            case IRON -> BalanceConfig.IRON_DEFENSE;
            case GOLD -> BalanceConfig.GOLD_DEFENSE;
            case DIAMOND -> BalanceConfig.DIAMOND_DEFENSE;
            case NETHERITE -> BalanceConfig.NETHERITE_DEFENSE;
            default -> null;
        };
    }

    /**
     * @return the TFARCENIM durability array for the given material, or
     *         {@code null} if the tier is not adjusted.
     */
    private static int[] durabilityArrayFor(ArmorMaterials material) {
        return switch (material) {
            case LEATHER -> BalanceConfig.LEATHER_DURABILITY;
            case IRON -> BalanceConfig.IRON_DURABILITY;
            case GOLD -> BalanceConfig.GOLD_DURABILITY;
            case DIAMOND -> BalanceConfig.DIAMOND_DURABILITY;
            case NETHERITE -> BalanceConfig.NETHERITE_DURABILITY;
            default -> null;
        };
    }
}
