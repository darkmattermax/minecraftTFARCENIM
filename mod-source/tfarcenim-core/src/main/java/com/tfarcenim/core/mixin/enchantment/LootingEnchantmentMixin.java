package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.LootingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link LootingEnchantment} (抢夺).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reduces drops (negative looting bonus)</li>
 *   <li>NORMAL — increases drops by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(LootingEnchantment.class)
public class LootingEnchantmentMixin {

    @Inject(method = "getLootingMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyLooting(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: looting reduces drop count (minimum 0)
            float reversed = -cir.getReturnValue();
            cir.setReturnValue(Math.max(0.0f, reversed));
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× looting bonus
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }
}
