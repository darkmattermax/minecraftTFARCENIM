package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.RiptideEnchantment;
import net.minecraft.enchantment.ChannelingEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for trident enchantments (激流/引雷).
 *
 * <p>Difficulty behaviour:
 * <ul>
 *   <li>HARD / HARDCORE:
 *     <ul>
 *       <li>Riptide — pulls the player downward instead of propelling forward</li>
 *       <li>Channeling — lightning strikes the thrower instead of the target</li>
 *     </ul>
 *   </li>
 *   <li>NORMAL — enhanced effects by 1.5×</li>
 *   <li>EASY — original vanilla behaviour</li>
 * </ul>
 */
@Mixin(RiptideEnchantment.class)
public class TridentEnchantmentsMixin {

    /**
     * Reverses Riptide: pulls player downward.
     */
    @Inject(method = "onRiptide", at = @At("HEAD"), cancellable = true)
    private void tfarcenim$reverseRiptide(PlayerEntity player, float riptideStrength,
                                          CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            ci.cancel();
            // Pull the player downward instead of propelling them forward
            Vec3d downward = new Vec3d(0, -riptideStrength * 1.5, 0);
            player.addVelocity(downward);
            player.velocityModified = true;
        }
    }

    /**
     * Enhances Riptide propulsion in NORMAL mode.
     */
    @Inject(method = "getRiptideStrength", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$enhanceRiptide(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
    }

    /**
     * Reverses Channeling: lightning strikes the thrower.
     */
    @Mixin(ChannelingEnchantment.class)
    public static class ChannelingMixin {

        @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
        private void tfarcenim$reverseChanneling(PlayerEntity thrower, TridentEntity trident,
                                                  CallbackInfo ci) {
            DifficultyManager dm = DifficultyManager.getInstance();
            if (dm.isReversedEnchantments()) {
                ci.cancel();
                // Summon lightning at the thrower's position instead
                if (!thrower.getWorld().isClient) {
                    var lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create(
                            thrower.getWorld());
                    if (lightning != null) {
                        lightning.refreshPositionAndAngles(
                                thrower.getX(), thrower.getY(), thrower.getZ(), 0, 0);
                        thrower.getWorld().spawnEntity(lightning);
                    }
                }
            }
        }
    }
}
