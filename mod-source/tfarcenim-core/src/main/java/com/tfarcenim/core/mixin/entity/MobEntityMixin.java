package com.tfarcenim.core.mixin.entity;

import com.tfarcenim.core.util.DifficultyManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link MobEntity}.
 *
 * <p>Supports the Loyalty enchantment reversal: when a redirected trident
 * lands near a mob, the mob automatically picks up and equips the trident
 * in its main hand.
 *
 * <p>Also enhances mob AI in reversed difficulty: mobs near a dropped trident
 * will attempt to pick it up.
 */
@Mixin(MobEntity.class)
public class MobEntityMixin {

    /**
     * Injects into the mob tick method. Checks if there's a trident item
     * nearby that was redirected by the Loyalty enchantment, and if so,
     * the mob picks it up and equips it.
     */
    @Inject(method = "tick", at = @At("RETURN"))
    private void tfarcenim$mobsEquipTridents(CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;

        DifficultyManager dm = DifficultyManager.getInstance();
        if (!dm.isReversedEnchantments()) {
            return; // Only active in reversed mode
        }

        // Check if the mob already has a weapon
        ItemStack mainHand = self.getMainHandStack();
        if (!mainHand.isEmpty()) {
            return; // Mob already has a weapon
        }

        // Look for trident items on the ground within 2 blocks
        var nearbyItems = self.getWorld().getEntitiesByClass(
                net.minecraft.entity.ItemEntity.class,
                self.getBoundingBox().expand(2.0),
                itemEntity -> {
                    ItemStack stack = itemEntity.getStack();
                    return stack.isOf(Items.TRIDENT) &&
                            itemEntity.getCommandTags().contains("tfarcenim_loyalty_redirect");
                });

        if (!nearbyItems.isEmpty()) {
            var tridentItem = nearbyItems.get(0);
            ItemStack tridentStack = tridentItem.getStack().copy();
            tridentItem.discard();

            // Equip the trident in the mob's main hand
            self.equipStack(EquipmentSlot.MAINHAND, tridentStack);

            // Set the mob to not despawn naturally while holding a weapon
            self.setPersistent();
        }
    }
}
