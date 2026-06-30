package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.KnockbackEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin for {@link KnockbackEnchantment} (击退).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — reverses knockback direction (pulls enemy closer)</li>
 *   <li>NORMAL — increases knockback strength by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 *
 * <p>Implementation: the knockback strength value is negated in reversed mode,
 * which causes the target to be pulled toward the attacker instead of pushed away.
 */
@Mixin(KnockbackEnchantment.class)
public class KnockbackEnchantmentMixin {

    /**
     * Modifies the knockback strength variable during attack processing.
     * In reversed mode, the value is negated to pull the target closer.
     * In enhanced mode, the value is multiplied by 1.5.
     *
     * @param strength the original knockback strength
     * @return the modified knockback strength
     */
    @ModifyVariable(
            method = "onUserAttacked",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private float tfarcenim$modifyKnockback(float strength) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: negate knockback to pull enemy closer
            return -strength;
        } else if (dm.isEnhancedEnchantments()) {
            // Enhance: 1.5× knockback
            return strength * DifficultyManager.ENHANCEMENT_MULTIPLIER;
        }
        return strength;
    }

    /**
     * Alternative injection point: modifies the knockback applied to the target
     * during the attack method of LivingEntity.
     * This handles the case where knockback is applied via the enchantment's
     * post-attack hook.
     */
    @ModifyVariable(
            method = "applyKnockback",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private float tfarcenim$reverseKnockbackDirection(float knockbackStrength) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            return -knockbackStrength;
        } else if (dm.isEnhancedEnchantments()) {
            return knockbackStrength * DifficultyManager.ENHANCEMENT_MULTIPLIER;
        }
        return knockbackStrength;
    }
}
