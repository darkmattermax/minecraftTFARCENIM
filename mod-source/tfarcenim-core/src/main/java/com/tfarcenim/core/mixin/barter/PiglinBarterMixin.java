package com.tfarcenim.core.mixin.barter;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin for Piglin bartering.
 *
 * <p>In TFARCENIM, piglins accept gold ingots but return iron ingots
 * (and other items) instead of the vanilla gold-based loot table.
 * This makes gold less valuable and adds a resource conversion mechanic.
 */
@Mixin(PiglinEntity.class)
public class PiglinBarterMixin {

    /**
     * Intercepts the barter result. Replaces gold-based barter loot with
     * iron ingots and a modified loot table.
     */
    @Inject(method = "getBarterItemStack", at = @At("RETURN"), cancellable = true)
    private void tfarcenim$modifyBarterLoot(ItemStack stack,
                                            CallbackInfoReturnable<ItemStack> cir) {
        PiglinEntity self = (PiglinEntity) (Object) this;

        // Get the original barter item
        ItemStack original = cir.getReturnValue();

        // 40% chance to return iron ingots instead of the normal barter item
        if (self.getRandom().nextFloat() < 0.40f) {
            int count = 1 + self.getRandom().nextInt(3); // 1-3 iron ingots
            cir.setReturnValue(new ItemStack(Items.IRON_INGOT, count));
            return;
        }

        // Otherwise return the original item (which is from the gold barter table)
        // The loot table itself is modified via the datapack
    }
}
