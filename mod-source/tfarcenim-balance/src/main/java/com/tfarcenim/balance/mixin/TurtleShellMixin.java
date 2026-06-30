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
 * Reinforces the turtle shell: durability 275 → 300 (twine-stitched upgrade).
 *
 * <p>The companion defense boost (2 → 3) is applied at the armor-material level
 * in {@link ArmorMaterialMixin} (a {@code TURTLE} case on the head slot) rather
 * than here, because per-item defense accessors are inherited from
 * {@code ArmorItem} and a subclass-targeted injection would not scope correctly.
 * Splitting the two concerns this way keeps both overrides correctly scoped.
 *
 * <p><b>Implementation note:</b> targets {@link Item} with an item-identity
 * guard on the turtle helmet, because {@code getMaxDamage} is inherited. See
 * {@link BowItemMixin}.
 *
 * <p><b>Mapping note (Yarn 1.21.10):</b> descriptor
 * {@code getMaxDamage(Lnet/minecraft/item/ItemStack;)I}.
 */
@Mixin(Item.class)
public class TurtleShellMixin {

    @Inject(method = "getMaxDamage(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"), cancellable = true)
    private void tfarcenim$turtleDurability(ItemStack stack,
                                            CallbackInfoReturnable<Integer> cir) {
        if (BalanceConfig.ENABLED && stack.isOf(Items.TURTLE_HELMET)) {
            cir.setReturnValue(BalanceConfig.TURTLE_SHELL_DURABILITY);
        }
    }
}
