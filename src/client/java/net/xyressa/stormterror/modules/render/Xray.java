package net.xyressa.stormterror.modules.render;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.xyressa.stormterror.system.Category;
import net.xyressa.stormterror.system.Module;

import java.util.HashSet;
import java.util.Set;

public class Xray extends Module {

    // Static flag for Mixins to access quickly
    public static boolean xrayEnabled = false;

    // The list of blocks we WANT to see (Whitelist)
    private static final Set<Block> TARGET_BLOCKS = new HashSet<>();

    public Xray() {
        super("Xray", "Shows ores and hides useless blocks.", Category.RENDER);

        // Initialize the whitelist (Ores, Chests, etc.)
        addTargets(
                Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
                Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
                Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE,
                Blocks.ANCIENT_DEBRIS,
                Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST,
                Blocks.SPAWNER, Blocks.LAVA, Blocks.WATER
        );
    }

    private void addTargets(Block... blocks) {
        for (Block b : blocks) TARGET_BLOCKS.add(b);
    }

    /**
     * Checks if a block should be visible when Xray is on.
     */
    public static boolean isVisible(Block block) {
        return TARGET_BLOCKS.contains(block);
    }

    @Override
    public void onEnable() {
        xrayEnabled = true;
        // Reloads the world renderer (chunks) to apply transparency
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        xrayEnabled = false;
        // Reloads the chunks again to restore normal blocks
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }
}