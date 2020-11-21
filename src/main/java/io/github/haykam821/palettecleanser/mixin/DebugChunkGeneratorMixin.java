package io.github.haykam821.palettecleanser.mixin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.DebugChunkGenerator;

@Mixin(DebugChunkGenerator.class)
public class DebugChunkGeneratorMixin {
	@Shadow
	@Mutable
	private static List<BlockState> BLOCK_STATES;

	@Shadow
	@Mutable
	private static int X_SIDE_LENGTH;

	@Shadow
	@Mutable
	private static int Z_SIDE_LENGTH;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void cleansePalette(CallbackInfo ci) {
		BLOCK_STATES = StreamSupport.stream(Registry.BLOCK.spliterator(), false).flatMap(block -> {
			return block.getStateManager().getStates().stream();
		}).collect(Collectors.toList());

		X_SIDE_LENGTH = MathHelper.ceil(MathHelper.sqrt(BLOCK_STATES.size()));
		Z_SIDE_LENGTH = MathHelper.ceil(BLOCK_STATES.size() / (float) X_SIDE_LENGTH);
	}
}