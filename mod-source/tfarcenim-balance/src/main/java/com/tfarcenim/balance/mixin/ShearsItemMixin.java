package com.tfarcenim.balance.mixin;

import com.tfarcenim.balance.config.BalanceConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Raises the shears durability to 260 (vanilla 238) — flint-honed blades.
 *
 * <p><b>Implementation note:</b> targets {@link Item} (not {@code ShearsItem})
 * with an item-identity guard, because {@code getMaxDamage} is inherited and a
 * subclass-targeted injection would affect all items. See {@link BowItemMixin}.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> descriptor
 * {@code getMaxDamage(Lnet/minecraft/item/ItemStack;)I}.
 */
@Mixin(Item.class)
public class ShearsItemMixin {

    @Inject(method = "getMaxDamage(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"), cancellable = true)
    private void tfarcenim$shearsDurability(ItemStack stack,
                                            CallbackInfoReturnable<Integer> cir) {
        if (BalanceConfig.ENABLED && stack.isOf(Items.SHEARS)) {
            cir.setReturnValue(BalanceConfig.SHEARS_DURABILITY);
        }
    }
}
