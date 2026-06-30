package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.MultishotEnchantment;
import net.minecraft.enchantment.PiercingEnchantment;
import net.minecraft.enchantment.QuickChargeEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for crossbow enchantments (多重射击/穿透/快速装填).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Multishot — fires 1 arrow but consumes 3</li>
 *       <li>Piercing — arrows bounce off targets instead of passing through</li>
 *       <li>Quick Charge — increases reload time</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — all effects enhanced by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(MultishotEnchantment.class)
public class CrossbowEnchantmentsMixin {

    /**
     * Reverses Multishot: reduces projectile count (fires fewer arrows).
     */
    @Inject(method = "getProjectileCount", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reverseMultishot(int level, CallbackInfoReturnable<Integer> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Fire 1 arrow but consume 3 (handled in CrossbowItem via ammo consumption)
            cir.setReturnValue(1);
        }
        // NORMAL / EASY: original value
    }

    /**
     * Reverses Piercing: arrows bounce off instead of passing through.
     */
    @Mixin(PiercingEnchantment.class)
    public static class PiercingMixin {
        @Inject(method = "getPierceLevel", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reversePiercing(int level, CallbackInfoReturnable<Integer> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // No piercing — arrows bounce
                cir.setReturnValue(0);
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
            }
        }
    }

    /**
     * Reverses Quick Charge: increases reload time.
     */
    @Mixin(QuickChargeEnchantment.class)
    public static class QuickChargeMixin {
        @Inject(method = "getChargeTimeReduction", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseQuickCharge(int level, CallbackInfoReturnable<Float> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // Negative reduction = slower reload
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
            }
        }
    }
}
