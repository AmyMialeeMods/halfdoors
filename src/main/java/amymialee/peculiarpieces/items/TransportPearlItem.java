package amymialee.peculiarpieces.items;

import amymialee.peculiarpieces.util.PeculiarHelper;
import amymialee.peculiarpieces.util.WarpManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class TransportPearlItem extends Item {
    public TransportPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.LODESTONE) {
            writeStone(context.getStack(), context.getBlockPos().add(0, 1, 0));
            player.getItemCooldownManager().set(this, 1);
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockPos pos = readStone(stack);
        if (user.isSneaking()) {
            incrementSlot(stack);
            user.getItemCooldownManager().set(this, 1);
        } else {
            if (!world.isClient && !pos.equals(BlockPos.ORIGIN)) {
                WarpManager.queueTeleport(user, Vec3d.ofBottomCenter(pos));
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.getItemCooldownManager().set(this, 1);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    public static void writeStone(ItemStack stack, BlockPos pos) {
        stack.getOrCreateNbt().put("pp:stone_%d".formatted(getSlot(stack)), NbtHelper.fromBlockPos(pos));
    }

    public static BlockPos readStone(ItemStack stack) {
        return NbtHelper.toBlockPos(stack.getOrCreateNbt().getCompound("pp:stone_%d".formatted(getSlot(stack))));
    }

    private static void incrementSlot(ItemStack stack) {
        NbtCompound mainCompound = stack.getOrCreateNbt();

        {
            NbtCompound nbtCompound = stack.getSubNbt("display");
            if (nbtCompound != null) {
                if (nbtCompound.contains("Name")) {
                    MutableText text = Text.Serializer.fromJson(nbtCompound.getString("Name"));
                    if (text != null ) {
                        mainCompound.putString("pp:stone_name_%d".formatted(getSlot(stack)), text.getString());
                    }
                }
                nbtCompound.remove("Name");
                if (nbtCompound.isEmpty()) {
                    stack.removeSubNbt("display");
                }
            }
        }

        stack.getOrCreateNbt().putInt("pp:selected", PeculiarHelper.clampLoop(0, 7, getSlot(stack) + 1));

        {
            if (mainCompound.contains("pp:stone_name_%d".formatted(getSlot(stack)))) {
                stack.setCustomName(new LiteralText(mainCompound.getString("pp:stone_name_%d".formatted(getSlot(stack)))).fillStyle(Style.EMPTY.withItalic(false)));
            } else {
                stack.removeCustomName();
            }
        }
    }

    public static int getSlot(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("pp:selected");
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound compound = stack.getOrCreateNbt();
        for (int i = 0; i < 8; i++) {
            BlockPos pos = NbtHelper.toBlockPos(compound.getCompound("pp:stone_%d".formatted(i)));
            if (!pos.equals(BlockPos.ORIGIN)) {
                if (compound.contains("pp:stone_name_%d".formatted(i))) {
                    tooltip.add(new TranslatableText(compound.getString("pp:stone_name_%d".formatted(i)) + ": x%d, y%d, z%d".formatted(pos.getX(), pos.getY(), pos.getZ())).setStyle(Style.EMPTY.withColor(MathHelper.hsvToRgb(((float)(i + 1) / 8), 1.0f, 1.0f))));
                } else {
                    tooltip.add(new TranslatableText((i + 1) + ": x%d, y%d, z%d".formatted(pos.getX(), pos.getY(), pos.getZ())).setStyle(Style.EMPTY.withColor(MathHelper.hsvToRgb(((float)(i + 1) / 8), 1.0f, 1.0f))));
                }
            } else {
                if (compound.contains("pp:stone_name_%d".formatted(i))) {
                    tooltip.add(new TranslatableText("peculiarpieces.transport_pearl.empty", compound.getString("pp:stone_name_%d".formatted(i))).setStyle(Style.EMPTY.withColor(MathHelper.hsvToRgb(((float)(i + 1) / 8), 1.0f, 1.0f))));
                } else {
                    tooltip.add(new TranslatableText("peculiarpieces.transport_pearl.empty", i + 1).setStyle(Style.EMPTY.withColor(MathHelper.hsvToRgb(((float)(i + 1) / 8), 1.0f, 1.0f))));
                }
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
