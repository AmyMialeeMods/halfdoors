package xyz.amymialee.halfdoors.mixin;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.TrapdoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrapdoorBlock.class)
public interface TrapdoorBlockAccessor {
    @Accessor("blockSetType")
    BlockSetType getBlockSetType();
}