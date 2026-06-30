package com.tfarcenim.core.event;

import com.tfarcenim.core.TfarcenimCore;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * LogRightClickHandler — 原木右键劈木板事件处理器.
 *
 * <p>When a player right-clicks a log block while holding an axe, the log
 * is consumed and 4 planks are dropped (instead of the vanilla 2 from crafting).
 * This provides a faster wood processing method that rewards exploration
 * with an axe.
 */
public class LogRightClickHandler {

    /**
     * Registers the use-block event callback.
     * Called from {@code TfarcenimCore.onInitialize()}.
     */
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            return onUseBlock(player, world, hand, hitResult);
        });
        TfarcenimCore.LOGGER.info("Registered LogRightClickHandler");
    }

    /**
     * Handles the right-click event on log blocks.
     *
     * @param player     the player performing the action
     * @param world      the world
     * @param hand       the hand used (main/off)
     * @param hitResult  the block hit result
     * @return SUCCESS if the log was converted, PASS otherwise
     */
    private static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand,
                                           BlockHitResult hitResult) {
        ItemStack heldStack = player.getStackInHand(hand);

        // Check if the player is holding an axe
        if (!(heldStack.getItem() instanceof AxeItem)) {
            return ActionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        // Check if the target block is a log
        if (!state.isIn(BlockTags.LOGS)) {
            return ActionResult.PASS;
        }

        // Don't process if player is sneaking (allow normal block interaction)
        if (player.isSneaking()) {
            return ActionResult.PASS;
        }

        // Process on server side only
        if (!world.isClient) {
            // Remove the log block
            world.removeBlock(pos, false);

            // Determine the appropriate plank type based on the log type
            Block plankType = getPlankForLog(state.getBlock());

            // Drop 4 planks at the log's position
            Block.dropStack(world, pos, new ItemStack(plankType, 4));

            // Damage the axe by 1
            heldStack.damage(1, player, p -> p.sendToolBreakStatus(hand));

            TfarcenimCore.LOGGER.debug("Converted log to 4 planks at {}", pos);
        }

        return ActionResult.SUCCESS;
    }

    /**
     * Returns the appropriate plank block for a given log block.
     * Maps each wood type to its corresponding planks.
     *
     * @param logBlock the log block
     * @return the corresponding plank block
     */
    private static Block getPlankForLog(Block logBlock) {
        // Map log blocks to plank blocks
        if (logBlock == Blocks.OAK_LOG || logBlock == Blocks.STRIPPED_OAK_LOG) {
            return Blocks.OAK_PLANKS;
        } else if (logBlock == Blocks.BIRCH_LOG || logBlock == Blocks.STRIPPED_BIRCH_LOG) {
            return Blocks.BIRCH_PLANKS;
        } else if (logBlock == Blocks.SPRUCE_LOG || logBlock == Blocks.STRIPPED_SPRUCE_LOG) {
            return Blocks.SPRUCE_PLANKS;
        } else if (logBlock == Blocks.JUNGLE_LOG || logBlock == Blocks.STRIPPED_JUNGLE_LOG) {
            return Blocks.JUNGLE_PLANKS;
        } else if (logBlock == Blocks.ACACIA_LOG || logBlock == Blocks.STRIPPED_ACACIA_LOG) {
            return Blocks.ACACIA_PLANKS;
        } else if (logBlock == Blocks.DARK_OAK_LOG || logBlock == Blocks.STRIPPED_DARK_OAK_LOG) {
            return Blocks.DARK_OAK_PLANKS;
        } else if (logBlock == Blocks.MANGROVE_LOG || logBlock == Blocks.STRIPPED_MANGROVE_LOG) {
            return Blocks.MANGROVE_PLANKS;
        } else if (logBlock == Blocks.CHERRY_LOG || logBlock == Blocks.STRIPPED_CHERRY_LOG) {
            return Blocks.CHERRY_PLANKS;
        } else if (logBlock == Blocks.CRIMSON_STEM || logBlock == Blocks.STRIPPED_CRIMSON_STEM) {
            return Blocks.CRIMSON_PLANKS;
        } else if (logBlock == Blocks.WARPED_STEM || logBlock == Blocks.STRIPPED_WARPED_STEM) {
            return Blocks.WARPED_PLANKS;
        } else if (logBlock == Blocks.BAMBOO_BLOCK || logBlock == Blocks.STRIPPED_BAMBOO_BLOCK) {
            return Blocks.BAMBOO_PLANKS;
        }
        // Default to oak planks
        return Blocks.OAK_PLANKS;
    }
}
