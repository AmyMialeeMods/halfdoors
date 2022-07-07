package amymialee.halfdoors.blocks;

import amymialee.halfdoors.screens.DoorcutterScreenHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DoorcutterBlock extends StonecutterBlock {
    private static final Text TITLE = Text.translatable("container.doorcutter");

    public DoorcutterBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new DoorcutterScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }
}