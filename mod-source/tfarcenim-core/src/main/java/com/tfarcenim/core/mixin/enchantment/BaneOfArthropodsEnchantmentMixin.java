package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.BaneOfArthropodsEnchantment;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link BaneOfArthropodsEnchantment} (节肢杀手).
 *
 * <p>Same reversal logic as {@link SmiteEnchantmentMixin}: in reversed mode
 * the bonus damage becomes negative (arthropods take less damage) and the
 * overflow is converted into healing for the arthropod target.
 */
@Mixin(BaneOfArthropodsEnchantment.class)
public class BaneOfArthropodsEnchantmentMixin {

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyBaneOfArthropods(int level, EntityType<?> entityType,
                                                  CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: bane of arthropods reduces damage against arthropods
            // The overflow (reduced damage) heals the arthropod — handled in attack event
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× bonus damage against arthropods
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }

    /**
     * Calculates the healing amount for arthropods when bane of arthropods is reversed.
     *
     * @param originalBonus the original bonus damage (positive)
     * @return the healing amount (equal to the reversed bonus)
     */
    public static float calculateArthropodHeal(float originalBonus) {
        return Math.abs(originalBonus);
    }
}
