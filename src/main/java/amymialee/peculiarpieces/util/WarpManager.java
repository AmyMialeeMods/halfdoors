package amymialee.peculiarpieces.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class WarpManager {
    private static final ArrayList<Pair<Entity, Vec3d>> dueTeleports = new ArrayList<>();

    public static void tick() {
        for (int i = 0; i < dueTeleports.size();) {
            Pair<Entity, Vec3d> pair = dueTeleports.get(i);
            Entity entity = pair.getLeft();
            Vec3d pos = pair.getRight();
                if (entity instanceof LivingEntity livingEntity) {
                    teleport(livingEntity, pos.x, pos.y, pos.z, true);
                } else {
                    entity.teleport(pos.x, pos.y, pos.z);
                }

            dueTeleports.remove(pair);
        }
    }

    public static void queueTeleport(Entity entity, Vec3d pos) {
        dueTeleports.add(new Pair<>(entity, pos));
    }

    public static void teleport(Entity entity, double x, double y, double z, boolean particleEffects) {
        entity.requestTeleport(x, y, z);
        if (particleEffects) {
            entity.world.sendEntityStatus(entity, (byte)46);
        }
        if (entity instanceof PathAwareEntity) {
            ((PathAwareEntity)entity).getNavigation().stop();
        }
    }
}
