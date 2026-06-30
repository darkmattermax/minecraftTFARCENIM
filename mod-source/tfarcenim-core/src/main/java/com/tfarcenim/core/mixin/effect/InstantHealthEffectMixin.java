package com.tfarcenim.core.mixin.effect;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.InstantHealthStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link InstantHealthStatusEffect}.
 *
 * <p>In HARDCORE difficulty, instant healing effects are rendered useless —
 * the potion of healing heals 0 HP, making health management much harder.
 */
@Mixin(InstantHealthStatusEffect.class)
public class InstantHealthEffectMixin {

    /**
     * Intercepts the apply effect method. In HARDCORE mode, healing does nothing.
     */
    @Inject(method = "applyUpdateEffect", at = @At("HEAD"), cancellable = true)
    private void tfarcenim$nullifyHealingInHardcore(LivingEntity entity, int amplifier,
                                                     CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();

        if (dm.isHardcore()) {
            // Cancel the healing — potions are useless in hardcore
            ci.cancel();
        }
        // Other modes: original healing behaviour
    }
}
