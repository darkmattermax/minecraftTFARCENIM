package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.SoulSpeedEnchantment;
import net.minecraft.enchantment.SwiftSneakEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for boot enchantments (灵魂疾行/迅捷潜行).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Soul Speed — slows the player on soul sand/soil</li>
 *       <li>Swift Sneak — slows the player while sneaking</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — both effects enhanced by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(SoulSpeedEnchantment.class)
public class BootEnchantmentsMixin {

    /**
     * Reverses Soul Speed: slows the player on soul sand.
     */
    @Inject(method = "getSoulSpeedMultiplier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reverseSoulSpeed(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: slow down on soul sand
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value
    }

    /**
     * Reverses Swift Sneak: slows the player while sneaking.
     */
    @Mixin(SwiftSneakEnchantment.class)
    public static class SwiftSneakMixin {
        @Inject(method = "getSwiftSneakSpeedMultiplier", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseSwiftSneak(int level, CallbackInfoReturnable<Float> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // Reverse: slower sneak speed
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
            }
            // EASY: original value
        }
    }
}
