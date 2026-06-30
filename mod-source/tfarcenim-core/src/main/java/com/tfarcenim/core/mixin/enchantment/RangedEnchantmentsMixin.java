package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.PowerEnchantment;
import net.minecraft.enchantment.PunchEnchantment;
import net.minecraft.enchantment.FlameEnchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for ranged weapon enchantments (力量/冲击/火矢/无限/穿刺).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Power — reduces arrow damage (negative multiplier)</li>
 *       <li>Punch — pulls target closer instead of pushing away</li>
 *       <li>Flame — sets the archer on fire instead of the target</li>
 *       <li>Infinity — consumes extra arrows (2-3 per shot)</li>
 *       <li>Impaling — reduces damage against aquatic / increases against land</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — all effects enhanced by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(PowerEnchantment.class)
public class RangedEnchantmentsMixin {

    /**
     * Reverses Power: reduces arrow damage.
     */
    @Inject(method = "getAttackDamageModifier", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reversePower(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
    }

    /**
     * Reverses Punch: pulls target closer (negative knockback).
     */
    @Mixin(PunchEnchantment.class)
    public static class PunchMixin {
        @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
        private void tfarcenim$reversePunch(LivingEntity user, Entity target, int level,
                                            CallbackInfo ci) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                ci.cancel();
                // Apply reverse knockback (pull toward shooter)
                if (target instanceof LivingEntity livingTarget && user != null) {
                    var pullDir = user.getPos().subtract(target.getPos()).normalize();
                    livingTarget.takeKnockback(level * 0.5f, pullDir.x, pullDir.z);
                }
            }
        }
    }

    /**
     * Reverses Flame: sets the archer on fire.
     */
    @Mixin(FlameEnchantment.class)
    public static class FlameMixin {
        @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
        private void tfarcenim$reverseFlame(LivingEntity user, Entity target, int level,
                                            CallbackInfo ci) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                ci.cancel();
                // Set the shooter on fire instead
                user.setFireTicks(level * 100);
            }
        }
    }

    /**
     * Reverses Infinity: consumes extra arrows.
     */
    @Mixin(InfinityEnchantment.class)
    public static class InfinityMixin {
        @Inject(method = "isInfinite", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseInfinity(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // Infinity no longer saves arrows — force consumption
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * Reverses Impaling: reduces damage against aquatic, increases against land.
     */
    @Mixin(ImpalingEnchantment.class)
    public static class ImpalingMixin {
        @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseImpaling(int level, CallbackInfoReturnable<Float> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
            }
        }
    }
}
