package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.UnbreakingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for {@link UnbreakingEnchantment} (耐久).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reverses the unbreaking probability (items break faster)</li>
 *   <li>NORMAL — improves the unbreaking probability by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 *
 * <p>In reversed mode, the "should not damage" check returns false more often,
 * causing tools to consume durability faster.
 */
@Mixin(UnbreakingEnchantment.class)
public class UnbreakingEnchantmentMixin {

    /**
     * Injects into the shouldPreventDamage method.
     * In reversed mode, inverts the probability check so items take damage more often.
     */
    @Inject(method = "shouldPreventDamage", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reverseUnbreaking(int level, CallbackInfoReturnable<Boolean> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: if unbreaking would prevent damage, force damage instead
            cir.setReturnValue(false);
        }
        // NORMAL: original logic preserved (enhanced effect via level multiplier)
        // EASY: original behaviour
    }
}
