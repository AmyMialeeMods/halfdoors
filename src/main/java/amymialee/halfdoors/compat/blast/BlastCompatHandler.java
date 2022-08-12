package amymialee.halfdoors.compat.blast;

import amymialee.halfdoors.entities.TinyDoorEntity;
import ladysnake.blast.common.entity.BombEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Box;

public class BlastCompatHandler {
    public static boolean projectileBombing(ProjectileEntity projectile, Entity target) {
        boolean success = false;
        if (projectile.distanceTo(target) < 1) {
            boolean them = explodeIfBomb(target, 2.15f);
            boolean proj = explodeIfBomb(projectile, 2f);
            success = them || proj;
        }
        return success;
    }

    public static boolean explodeIfBomb(Entity target, float bonus) {
        if (target instanceof BombEntity bomb) {
            bomb.setExplosionRadius(bomb.getExplosionRadius() * bonus);
            bomb.explode();
            return true;
        }
        return false;
    }

    public static Entity getNearestBomb(TinyDoorEntity entity, float radius, ProjectileEntity except) {
        return TinyDoorEntity.getClosestEntity(entity.world.getEntitiesByClass(BombEntity.class, new Box(
                entity.getX() - radius,
                entity.getY() - radius,
                entity.getZ() - radius,
                entity.getX() + radius,
                entity.getY() + radius,
                entity.getZ() + radius),
                (a) -> a != except),
                entity,
                entity.getX(),
                entity.getY(),
                entity.getZ());
    }
}