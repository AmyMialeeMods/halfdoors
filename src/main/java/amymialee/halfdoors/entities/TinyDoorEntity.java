package amymialee.halfdoors.entities;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.util.HomingArrowAccessor;
import ladysnake.blast.common.entity.BombEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class TinyDoorEntity extends ThrownItemEntity {
    public TinyDoorEntity(EntityType<? extends TinyDoorEntity> entityType, World world) {
        super(entityType, world);
    }

    public TinyDoorEntity(World world, LivingEntity owner) {
        super(Halfdoors.TINY_DOOR_ENTITY, owner, world);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof ProjectileEntity projectileEntity) {
            if (projectileEntity.getOwner() != null) {
                Entity target;
                int radius = 24;
                target = getClosestEntity(world.getEntitiesByClass(TinyDoorEntity.class, new Box(getX() - radius, getY() - radius, getZ() - radius, getX() + radius, getY() + radius, getZ() + radius), (a) -> a != projectileEntity), this, getX(), getY(), getZ());
                if (target == null) {
                    if (FabricLoader.getInstance().isModLoaded("blast")) {
                        target = getClosestEntity(world.getEntitiesByClass(BombEntity.class, new Box(getX() - radius, getY() - radius, getZ() - radius, getX() + radius, getY() + radius, getZ() + radius), (a) -> a != projectileEntity), this, getX(), getY(), getZ());
                    }
                }
                if (target == null && projectileEntity.getOwner() instanceof LivingEntity living) {
                    target = getClosestEntity(world.getEntitiesByClass(LivingEntity.class, new Box(getX() - radius, getY() - radius, getZ() - radius, getX() + radius, getY() + radius, getZ() + radius), (a) -> !living.isTeammate(a) && a != living), this, getX(), getY(), getZ());
                }
                if (projectileEntity instanceof HomingArrowAccessor homingArrowAccessor) {
                    world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.6F, 1 + 2f / homingArrowAccessor.getBounces());
                } else {
                    world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.6F, 1f + (world.getRandom().nextFloat()));
                }
                if (target != null) {
                    if (projectileEntity instanceof HomingArrowAccessor homingArrowAccessor) {
                        homingArrowAccessor.setHomingTarget(target);
                        homingArrowAccessor.setBounces(homingArrowAccessor.getBounces() + 1);
                    } else {
                        double d = target.distanceTo(this);
                        double e = target.getX() - getX() + (target.getVelocity().getX() * d);
                        double f = target.getBodyY(1) - getY() + (target.getVelocity().getY() * d);
                        double g = target.getZ() - getZ() + (target.getVelocity().getZ() * d);
                        Vec3d targetVel = new Vec3d(e, f, g);
                        projectileEntity.setPosition(this.getPos());
                        projectileEntity.setVelocity(targetVel);
                    }
                    if (projectileEntity instanceof TridentEntity) {
                        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                        assert lightningEntity != null;
                        lightningEntity.refreshPositionAfterTeleport(target.getPos());
                        this.world.spawnEntity(lightningEntity);
                    }
                } else {
                    Vec3d targetVel = new Vec3d(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f);
                    targetVel.multiply(50);
                    projectileEntity.setPosition(this.getPos());
                    projectileEntity.setVelocity(targetVel);
                }
                projectileEntity.refreshPositionAndAngles(projectileEntity.getX(), projectileEntity.getY(), projectileEntity.getZ(), projectileEntity.getYaw(), projectileEntity.getPitch());
                if (projectileEntity instanceof PersistentProjectileEntity persistentProjectile) {
                    persistentProjectile.setDamage(persistentProjectile.getDamage() * 1.75f);
                    persistentProjectile.setCritical(true);
                }
                this.discard();
            }
        }
    }

    private static Entity getClosestEntity(List<? extends Entity> entityList, @Nullable Entity entity, double x, double y, double z) {
        Entity entity2 = null;
        for (Entity entity3 : entityList) {
            if (entity instanceof MobEntity mobEntity && mobEntity.getTarget() == entity3) {
                return mobEntity;
            }
            if (entity3 == entity) {
                continue;
            }
            if (entity2 == null) {
                entity2 = entity3;
                continue;
            }
            if (entity3.squaredDistanceTo(x, y, z) < entity2.squaredDistanceTo(x, y, z)) {
                entity2 = entity3;
            }
        }
        return entity2;
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hitResult = getCollision(this, this::canHit);
        if (hitResult != null) {
            this.onCollision(hitResult);
        }
    }

    public static HitResult getCollision(Entity entity, Predicate<Entity> predicate) {
        World world = entity.world;
        return getEntityCollision(world, entity, entity.getBoundingBox().stretch(entity.getVelocity()).expand(1.0D), predicate);
    }

    @Nullable
    public static EntityHitResult getEntityCollision(World world, Entity entity, Box box, Predicate<Entity> predicate) {
        Entity entity2 = getClosestEntity(world.getOtherEntities(entity, box, predicate), entity, entity.getX(), entity.getY(), entity.getZ());
        if (entity2 == null) {
            return null;
        } else {
            return new EntityHitResult(entity2);
        }
    }

    protected boolean canHit(Entity entity) {
        if (!(entity instanceof TinyDoorEntity) && !entity.isSpectator() && entity.isAlive()) {
            Entity entity2 = this.getOwner();
            return entity2 == null || !entity2.isConnectedThroughVehicle(entity);
        } else {
            return false;
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.BLOCK) {
            this.discard();
        }
        super.onCollision(hitResult);
    }

    @Override
    protected float getGravity() {
        return 0.016f;
    }

    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }
}