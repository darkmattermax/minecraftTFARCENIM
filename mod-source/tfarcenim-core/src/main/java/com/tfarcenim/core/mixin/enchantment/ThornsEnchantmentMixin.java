package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link ThornsEnchantment} (荆棘).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — the attacker is healed instead of taking thorns damage</li>
 *   <li>NORMAL — increases thorns damage by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(ThornsEnchantment.class)
public class ThornsEnchantmentMixin {

    /**
     * Injects into the thorns damage application.
     * In reversed mode, instead of damaging the attacker, heals them.
     */
    @Inject(
            method = "onUserAttacked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tfarcenim$reverseThorns(LivingEntity user, Entity attacker, int level,
                                         CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Cancel original thorns damage
            ci.cancel();
            // Heal the attacker instead
            if (attacker instanceof LivingEntity livingAttacker) {
                float healAmount = level * 1.5f; // same as thorns damage would be
                livingAttacker.heal(healAmount);
            }
        }
        // NORMAL: original thorns damage (enhanced via multiplier)
        // EASY: original behaviour
    }

    /**
     * Enhances thorns damage multiplier in NORMAL mode.
     */
    @Inject(method = "getThornsDamage", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$enhanceThorns(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
    }
}
