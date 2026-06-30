package com.tfarcenim.core.item;

import com.tfarcenim.core.TfarcenimCore;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

/**
 * GuideBookItem — 指南书 custom item.
 *
 * <p>This item is placed in the starter/bonus chest. When opened, it displays
 * the message: "箱子中的东西一定要全部拿着哦" (Make sure to take everything
 * from the chest!).
 *
 * <p>The book is implemented as a writable book with pre-set content.
 */
public class GuideBookItem extends Item {

    /** The registry identifier for this item. */
    public static final Identifier ID = new Identifier("tfarcenim", "guide_book");

    /** The guide book content text (Chinese). */
    private static final String GUIDE_TEXT_ZH = "箱子中的东西一定要全部拿着哦";

    /** The guide book content text (English). */
    private static final String GUIDE_TEXT_EN = "Make sure to take everything from the chest!";

    /**
     * Constructs the guide book item with default settings.
     */
    public GuideBookItem(Settings settings) {
        super(settings);
    }

    /**
     * Creates a guide book ItemStack with pre-written content.
     *
     * @return an ItemStack containing the guide book with text
     */
    public static ItemStack createGuideBook() {
        ItemStack stack = new ItemStack(Registries.ITEM.get(ID));

        // Create book content using NBT
        NbtList pages = new NbtList();
        pages.add(NbtString.of(
                "{\"text\":\"" + GUIDE_TEXT_ZH + "\\n\\n" +
                        GUIDE_TEXT_EN + "\",\"color\":\"gold\"}"
        ));

        stack.getOrCreateNbt().put("pages", pages);
        stack.getOrCreateNbt().putString("title", "TFARCENIM Guide");
        stack.getOrCreateNbt().putString("author", "TFARCENIM");
        stack.getOrCreateNbt().putBoolean("resolved", true);

        return stack;
    }

    /**
     * Appends tooltip text to the guide book item.
     */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.tfarcenim.guide_book.tooltip")
                .formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("item.tfarcenim.guide_book.tooltip2")
                .formatted(Formatting.GRAY, Formatting.ITALIC));
        super.appendTooltip(stack, world, tooltip, context);
    }

    /**
     * Registers this item in the vanilla item registry.
     */
    public static void register() {
        GuideBookItem item = new GuideBookItem(
                new Item.Settings().maxCount(1)
        );
        Registry.register(Registries.ITEM, ID, item);
        TfarcenimCore.LOGGER.info("Registered Guide Book item");
    }
}
