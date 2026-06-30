package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.FortuneEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link FortuneEnchantment} (时运).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reduces ore drops (fortune becomes misfortune)</li>
 *   <li>NORMAL — increases ore drops by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(FortuneEnchantment.class)
public class FortuneEnchantmentMixin {

    /**
     * Modifies the fortune drop multiplier.
     * In reversed mode, the multiplier is reduced (fewer drops).
     */
    @Inject(method = "getFortuneMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyFortune(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: fortune reduces drops (minimum 0)
            float reversed = -cir.getReturnValue();
            cir.setReturnValue(Math.max(0.0f, reversed));
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× fortune bonus
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }
}
