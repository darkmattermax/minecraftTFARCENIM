package com.tfarcenim.core.mixin.enchantment;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.enchantment.LoyaltyEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mixin for {@link LoyaltyEnchantment} (忠诚).
 *
 * <p>V6 design: In reversed mode, the trident flies toward the nearest entity
 * (player or mob) instead of returning to the thrower. If a mob catches it,
 * the mob automatically equips the trident.
 */
@Mixin(LoyaltyEnchantment.class)
public class LoyaltyEnchantmentMixin {

    /**
     * Injects into the trident return logic. In reversed mode, redirects
     * the trident toward the nearest entity instead of the thrower.
     */
    @Inject(
            method = "onTridentReturn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tfarcenim$redirectLoyalty(TridentEntity trident, CallbackInfo ci) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isReversedEnchantments()) {
            ci.cancel();
            // Find the nearest entity (player or mob) within 64 blocks
            Vec3d pos = trident.getPos();
            Box searchArea = new Box(pos.add(-32, -32, -32), pos.add(32, 32, 32));
            List<LivingEntity> nearby = trident.getWorld().getEntitiesByClass(
                    LivingEntity.class, searchArea, e -> e != trident.getOwner());

            if (!nearby.isEmpty()) {
                // Find the closest entity
                LivingEntity nearest = null;
                double minDist = Double.MAX_VALUE;
                for (LivingEntity entity : nearby) {
                    double dist = entity.squaredDistanceTo(pos);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = entity;
                    }
                }
                if (nearest != null) {
                    // Redirect trident velocity toward the nearest entity
                    Vec3d direction = nearest.getEyePos().subtract(pos).normalize();
                    trident.setVelocity(direction.multiply(1.5));
                    // Mark the trident for auto-equip when it hits a mob
                    trident.addCommandTag("tfarcenim_loyalty_redirect");

                    // Schedule equip check: if the trident lands near a mob, equip it
                    if (nearest instanceof MobEntity mob) {
                        ItemStack tridentStack = trident.asItemStack();
                        // Equip the trident in the mob's main hand
                        mob.equipLootStack(net.minecraft.entity.EquipmentSlot.MAINHAND, tridentStack);
                    }
                }
            }
        }
        // NORMAL: enhanced return speed (1.5×) — handled via level multiplier
        // EASY: original behaviour
    }

    /**
     * Enhances return speed in NORMAL mode.
     */
    @Inject(method = "getReturnAcceleration", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$enhanceReturnSpeed(int level, CallbackInfoReturnable<Float> cir) {
        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isEnhancedEnchantments()) {
            cir.setReturnValue(cir.getReturnValue() * DifficultyManager.ENHANCEMENT_MULTIPLIER);
        }
    }
}
