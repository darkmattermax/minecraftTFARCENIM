package com.tfarcenim.core.mixin.entity;

import com.tfarcenim.core.TfarcenimCore;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link LivingEntity}.
 *
 * <p>Prevents undead entities from burning in daylight. In vanilla, zombies,
 * skeletons, and other undead burn when exposed to sunlight. TFARCENIM removes
 * this mechanic — undead are now a persistent threat during the day.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * Injects at the head of the entity tick method. If the entity is undead
     * and is on fire during daytime, extinguishes it.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void tfarcenim$preventUndeadBurning(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        // Check if the entity is tagged as undead
        if (self.getType().isIn(EntityTypeTags.UNDEAD)) {
            // If on fire and it's daytime, extinguish
            if (self.isOnFire() && self.getWorld().isDay()) {
                self.extinguish();
            }
        }
    }

    /**
     * Additional injection: ensures undead don't catch fire in the first place
     * by intercepting the burning check for undead entities in sunlight.
     */
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tfarcenim$preventUndeadSunlightDamage(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.getType().isIn(EntityTypeTags.UNDEAD)) {
            // Continuously extinguish undead during daytime to counteract
            // any sunlight ignition that may have already triggered
            if (self.getWorld().isDay() && !self.getWorld().isRaining()) {
                if (self.isOnFire()) {
                    self.extinguish();
                }
            }
        }
    }
}
