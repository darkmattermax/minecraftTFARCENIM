package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.RespirationEnchantment;
import net.minecraft.enchantment.AquaAffinityEnchantment;
import net.minecraft.enchantment.DepthStriderEnchantment;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for underwater enchantments (水下呼吸/水下速掘/深海探索者/冰霜行者).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Respiration — shortens underwater breath time</li>
 *       <li>Aqua Affinity — slows mining underwater</li>
 *       <li>Depth Strider — slows underwater movement</li>
 *       <li>Frost Walker — melts nearby ice instead of freezing</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — all effects enhanced by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(RespirationEnchantment.class)
public class WaterEnchantmentsMixin {

    /**
     * Reverses Respiration: shortens underwater breath.
     */
    @Inject(method = "getBreathExtension", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$reverseRespiration(int level, CallbackInfoReturnable<Integer> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Reverse: reduces breath time (negative extension)
            cir.setReturnValue(-cir.getReturnValue());
        } else if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
        }
        // EASY: original value
    }

    /**
     * Reverses Aqua Affinity: slows mining underwater.
     */
    @Mixin(AquaAffinityEnchantment.class)
    public static class AquaAffinityMixin {
        @Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseAquaAffinity(int level, CallbackInfoReturnable<Float> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() / 2.0f); // slower underwater mining
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
            }
        }
    }

    /**
     * Reverses Depth Strider: slows underwater movement.
     */
    @Mixin(DepthStriderEnchantment.class)
    public static class DepthStriderMixin {
        @Inject(method = "getDepthStriderSpeed", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseDepthStrider(int level, CallbackInfoReturnable<Float> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
            }
        }
    }

    /**
     * Reverses Frost Walker: melts nearby ice instead of freezing.
     */
    @Mixin(FrostWalkerEnchantment.class)
    public static class FrostWalkerMixin {
        @Inject(method = "getFreezeRadius", at = @At("RETURN"), cancellable = true)
        private void tfarcenim$reverseFrostWalker(int level, CallbackInfoReturnable<Integer> cir) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                // Reverse: set radius to negative — handled in the freeze logic
                // to melt ice instead of freezing
                cir.setReturnValue(-cir.getReturnValue());
            } else if (dm.isEnhancedEnchantments()) {
                cir.setReturnValue((int) (cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER));
            }
        }
    }
}
