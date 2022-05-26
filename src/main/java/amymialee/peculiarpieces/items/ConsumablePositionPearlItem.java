package amymialee.peculiarpieces.items;

import amymialee.peculiarpieces.util.WarpManager;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ConsumablePositionPearlItem extends Item {
    public ConsumablePositionPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        BlockPos pos = NbtHelper.toBlockPos(context.getStack().getOrCreateNbt().getCompound("pp:stone"));
        if (player != null && (pos.equals(BlockPos.ORIGIN) || player.isCreative()) && player.isSneaking()) {
            writeStone(context.getStack(), context.getBlockPos().add(0, 1, 0));
            player.getItemCooldownManager().set(this, 20);
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockPos pos = readStone(stack);
        if (!world.isClient && !pos.equals(BlockPos.ORIGIN)) {
            WarpManager.queueTeleport(user, Vec3d.ofBottomCenter(pos));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.getItemCooldownManager().set(this, 40);
        stack.decrement(1);
        return TypedActionResult.consume(stack);
    }

    public static void writeStone(ItemStack stack, BlockPos pos) {
        stack.getOrCreateNbt().put("pp:stone", NbtHelper.fromBlockPos(pos));
    }

    public static BlockPos readStone(ItemStack stack) {
        return NbtHelper.toBlockPos(stack.getOrCreateNbt().getCompound("pp:stone"));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound compound = stack.getOrCreateNbt();
        BlockPos pos = NbtHelper.toBlockPos(compound.getCompound("pp:stone"));
        if (!pos.equals(BlockPos.ORIGIN)) {
            tooltip.add(new TranslatableText("Position: x%d, y%d, z%d".formatted(pos.getX(), pos.getY(), pos.getZ())).formatted(Formatting.GRAY));
        } else {
            tooltip.add(new TranslatableText("peculiarpieces.pearl.empty").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("peculiarpieces.pearl.bind").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
