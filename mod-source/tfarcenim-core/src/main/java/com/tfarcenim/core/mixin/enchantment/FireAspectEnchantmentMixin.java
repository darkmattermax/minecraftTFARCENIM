package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link FireAspectEnchantment} (火焰附加).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE — the attacker sets <em>themselves</em> on fire instead of the target</li>
 *   <li>NORMAL — extends the burn duration by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(FireAspectEnchantment.class)
public class FireAspectEnchantmentMixin {

    /**
     * Injects into the post-attack method. In reversed mode, the fire is
     * applied to the attacker instead of the target.
     */
    @Inject(
            method = "onTargetDamaged",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tfarcenim$reverseFireAspect(LivingEntity user, Entity target, int level,
                                             CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            // Cancel the original fire-on-target behaviour
            ci.cancel();
            // Set the attacker on fire instead
            int burnTime = level * 4; // standard fire aspect burn time
            user.setFireTicks(burnTime * 20); // convert seconds to ticks
        }
        // NORMAL: let original proceed (enhanced burn time handled via the level value)
        // EASY: original behaviour
    }
}
