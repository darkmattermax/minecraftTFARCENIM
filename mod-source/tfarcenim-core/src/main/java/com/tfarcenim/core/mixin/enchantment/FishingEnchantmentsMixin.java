package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.LuckOfTheSeaEnchantment;
import net.minecraft.enchantment.LureEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for fishing enchantments (海之眷顾 / 饵钓).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Luck of the Sea — reduces treasure probability (negative luck)</li>
 *       <li>Lure — increases wait time (slower bites)</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — enhances both effects by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(LuckOfTheSeaEnchantment.class)
public class FishingEnchantmentsMixin {

    /**
     * Reverses Luck of the Sea: reduces treasure probability.
     */
    @Inject(method = "getTreasureChanceMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reverseLuckOfTheSea(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: reduce treasure chance
            float reversed = -cir.getReturnValue();
            cir.setReturnValue(Math.max(-0.15f * level, reversed));
        } else if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }

    /**
     * Reverses Lure: increases wait time instead of decreasing it.
     */
    @Mixin(LureEnchantment.class)
    public static class LureEnchantmentMixin {

        @Inject(method = "getLureReduction", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseLure(int level, CallbackInfoReturnable<Integer> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // Reverse: increase wait time (negative reduction = more wait)
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
            }
            // EASY: original value unchanged
        }
    }
}
