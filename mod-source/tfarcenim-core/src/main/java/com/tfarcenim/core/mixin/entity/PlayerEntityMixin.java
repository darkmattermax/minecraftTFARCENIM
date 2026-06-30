package com.tfarcenim.core.mixin.entity;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link PlayerEntity}.
 *
 * <p>In HARDCORE difficulty, limits the player's maximum health to 10 HP
 * (5 hearts) instead of the vanilla 20 HP (10 hearts).
 */
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /**
     * Injects into the player tick method. On every tick, if the difficulty
     * is HARDCORE, forces the max health attribute to 10 HP.
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void tfarcenim$applyHardcoreHealthLimit(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isHardcore()) {
            // Get the max health attribute instance
            EntityAttributeInstance maxHealth = self.getAttributeInstance(
                    EntityAttributes.GENERIC_MAX_HEALTH);

            if (maxHealth != null) {
                double currentMax = maxHealth.getBaseValue();
                if (currentMax > 10.0) {
                    maxHealth.setBaseValue(10.0);
                    // Also clamp current health to the new max
                    if (self.getHealth() > 10.0f) {
                        self.setHealth(10.0f);
                    }
                }
            }
        }
    }

    /**
     * Also intercepts player initialization to set up the 10 HP limit
     * from the start in HARDCORE mode.
     */
    @Inject(method = "initDataTracker", at = @At("RETURN"))
    private void tfarcenim$initHardcoreHealth(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        DifficultyManager dm = DifficultyManager.getInstance();
        if (dm.isHardcore()) {
            EntityAttributeInstance maxHealth = self.getAttributeInstance(
                    EntityAttributes.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(10.0);
            }
        }
    }
}
