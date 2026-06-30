package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.SmiteEnchantment;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link SmiteEnchantment} (亡灵杀手).
 *
 * <p>V6 design: In reversed mode, the smite bonus becomes negative —
 * undead take <em>less</em> damage and the "overflow" (the amount of
 * damage reduction) is converted into healing for the undead target.
 *
 * <p>The healing logic is handled in {@code LivingEntityMixin} via the
 * attack event, which checks for reversed smite and applies healing
 * equal to the absolute value of the negative damage bonus.
 */
@Mixin(SmiteEnchantment.class)
public class SmiteEnchantmentMixin {

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifySmite(int level, EntityType<?> entityType,
                                       CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: smite reduces damage against undead
            // The overflow (reduced damage) heals the undead — handled in attack event
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× bonus damage against undead
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
        // EASY: original value unchanged
    }

    /**
     * Calculates the healing amount for undead when smite is reversed.
     * Called from {@code LivingEntityMixin} during attack processing.
     *
     * @param originalBonus the original smite bonus damage (positive)
     * @return the healing amount (equal to the reversed bonus)
     */
    public static float calculateUndeadHeal(float originalBonus) {
        return Math.abs(originalBonus);
    }
}
