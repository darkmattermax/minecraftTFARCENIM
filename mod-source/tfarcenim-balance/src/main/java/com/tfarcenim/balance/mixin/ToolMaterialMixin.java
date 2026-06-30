package com.tfarcenim.balance.mixin;

import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Overrides the six vanilla tool tiers (wood / gold / stone / iron / diamond /
 * netherite) for durability, mining speed and attack-damage baseline.
 *
 * <p>Every tier gains the "twine + leather wrapping" durability bump. Netherite
 * additionally gains the "star power" bonuses: +11% mining speed and +2 attack
 * damage baseline (which lifts the netherite sword 8→10 and axe 10→12, since
 * both derive from the material attack-damage baseline in 1.21.x).
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> targets the classic {@code ToolMaterials}
 * accessors {@code getDurability()}, {@code getMiningSpeedMultiplier()} and
 * {@code getAttackDamage()}. If the 1.21.2+ tool-component refactor renamed
 * these (e.g. {@code durability()} / {@code speed()} / {@code attackDamageBaseline()}),
 * adjust the {@code method} literals — the dispatched values are unchanged.
 */
@Mixin(ToolMaterials.class)
public class ToolMaterialMixin {

    /**
     * Replaces per-tier durability with the TFARCENIM values from
     * {@link BalanceConfig}.
     *
     * @param cir the return-value callback carrying the vanilla durability.
     */
    @Inject(method = "getDurability", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyDurability(CallbackInfoReturnable<Integer> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        ToolMaterials self = (ToolMaterials) (Object) this;
        switch (self) {
            case WOOD -> cir.setReturnValue(BalanceConfig.WOOD_DURABILITY);
            case GOLD -> cir.setReturnValue(BalanceConfig.GOLD_DURABILITY);
            case STONE -> cir.setReturnValue(BalanceConfig.STONE_DURABILITY);
            case IRON -> cir.setReturnValue(BalanceConfig.IRON_DURABILITY);
            case DIAMOND -> cir.setReturnValue(BalanceConfig.DIAMOND_DURABILITY);
            case NETHERITE -> cir.setReturnValue(BalanceConfig.NETHERITE_DURABILITY);
            default -> { /* No-op: any future tier keeps vanilla behaviour. */ }
        }
    }

    /**
     * Grants netherite the star-power mining-speed bonus (9.0 → 10.0).
     *
     * @param cir the return-value callback carrying the vanilla mining speed.
     */
    @Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyMiningSpeed(CallbackInfoReturnable<Float> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        ToolMaterials self = (ToolMaterials) (Object) this;
        if (self == ToolMaterials.NETHERITE) {
            cir.setReturnValue(BalanceConfig.NETHERITE_MINING_SPEED);
        }
    }

    /**
     * Grants netherite the star-power attack-damage baseline bonus (4.0 → 6.0),
     * which raises the netherite sword to 10 and the netherite axe to 12.
     *
     * <p>Per-weapon attack <em>speed</em> is applied separately in
     * {@link AttributeModifierMixin} to avoid double-counting damage here.
     *
     * @param cir the return-value callback carrying the vanilla attack damage.
     */
    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyAttackDamage(CallbackInfoReturnable<Float> cir) {
        if (!BalanceConfig.ENABLED) {
            return;
        }
        ToolMaterials self = (ToolMaterials) (Object) this;
        if (self == ToolMaterials.NETHERITE) {
            cir.setReturnValue(BalanceConfig.NETHERITE_ATTACK_DAMAGE);
        }
    }
}
