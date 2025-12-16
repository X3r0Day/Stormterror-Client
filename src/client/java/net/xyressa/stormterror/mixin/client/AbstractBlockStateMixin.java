package net.xyressa.stormterror.mixin.client;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.xyressa.stormterror.modules.render.Xray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow public abstract Block getBlock();

    // ==========================================================
    // FIX 1: Chunk Occlusion (The "Disappearing Chunks" Fix)
    // ==========================================================

    /**
     * Target: public boolean isOpaqueFullCube()
     * Logic: If Stone claims to be "Not Opaque", the game's occlusion culling
     * knows that light/vision can pass through it, so it renders the chunks behind it.
     */
    @Inject(method = "isOpaqueFullCube()Z", at = @At("HEAD"), cancellable = true)
    private void onIsOpaqueFullCube(CallbackInfoReturnable<Boolean> cir) {
        if (Xray.xrayEnabled) {
            // If it's a hidden block (Stone), say "I am NOT opaque" (FALSE)
            if (!Xray.isVisible(this.getBlock())) {
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * Target: public boolean isOpaque()
     * Logic: Secondary check used by some rendering layers.
     */
    @Inject(method = "isOpaque()Z", at = @At("HEAD"), cancellable = true)
    private void onIsOpaque(CallbackInfoReturnable<Boolean> cir) {
        if (Xray.xrayEnabled) {
            if (!Xray.isVisible(this.getBlock())) {
                cir.setReturnValue(false);
            }
        }
    }

    // ==========================================================
    // FIX 2: Hidden Ores (The "Exposed Only" Fix)
    // ==========================================================

    /**
     * Target: public VoxelShape getCullingFace(Direction direction)
     * Logic: Tells the neighbor (Ore) that the Stone block is empty space,
     * so the Ore must draw its side face.
     */
    @Inject(
            method = "getCullingFace(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/shape/VoxelShape;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetCullingFace(Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        if (Xray.xrayEnabled) {
            if (!Xray.isVisible(this.getBlock())) {
                cir.setReturnValue(VoxelShapes.empty());
            }
        }
    }

    // ==========================================================
    // FIX 3: Visual Polish (Lighting & Side Rendering)
    // ==========================================================

    @Inject(
            method = "isSideInvisible(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onIsSideInvisible(net.minecraft.block.BlockState neighbor, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (Xray.xrayEnabled) {
            // If I am an Ore, ALWAYS render me.
            if (Xray.isVisible(this.getBlock())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (Xray.xrayEnabled) {
            cir.setReturnValue(1.0f);
        }
    }
}