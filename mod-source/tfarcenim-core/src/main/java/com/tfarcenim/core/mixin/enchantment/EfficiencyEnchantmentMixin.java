package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.EfficiencyEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link EfficiencyEnchantment} (效率).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reduces mining speed (efficiency becomes inefficiency)</li>
 *   <li>NORMAL — increases mining speed by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(EfficiencyEnchantment.class)
public class EfficiencyEnchantmentMixin {

    @Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyEfficiency(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: efficiency slows down mining (minimum 0.1 to avoid div-by-zero)
            float reversed = -cir.getReturnValue();
            cir.setReturnValue(Math.max(0.1f, 1.0f + reversed)); // slow but not negative
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× mining speed
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }
}
