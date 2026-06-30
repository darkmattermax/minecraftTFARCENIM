package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link ProtectionEnchantment}.
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reverses protection (negative protection amount = more damage taken)</li>
 *   <li>NORMAL — amplifies protection by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {

    @Inject(method = "getProtectionAmount", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyProtection(int level, DamageSource source,
                                            CallbackInfoReturnable<Integer> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: protection becomes vulnerability (negative value)
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× protection
            cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
        }
        // EASY: original value unchanged
    }
}
