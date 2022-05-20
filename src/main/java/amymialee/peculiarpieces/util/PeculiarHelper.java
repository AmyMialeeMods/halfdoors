package amymialee.peculiarpieces.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class PeculiarHelper {
    public static int clampLoop(int start, int end, int input) {
        if (input < start) {
            return clampLoop(start, end, (end - start + 1) + input);
        } else if (input > end) {
            return clampLoop(start, end, input - (end - start + 1));
        }
        return input;
    }

    public static NbtCompound fromVec3d(Vec3d pos) {
        if (pos == null) {
            pos = Vec3d.ZERO;
        }
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putDouble("X", pos.getX());
        nbtCompound.putDouble("Y", pos.getY());
        nbtCompound.putDouble("Z", pos.getZ());
        return nbtCompound;
    }

    public static Vec3d toVec3d(NbtCompound compound) {
        return new Vec3d(compound.getDouble("X"), compound.getDouble("Y"), compound.getDouble("Z"));
    }
}