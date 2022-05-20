package amymialee.halfdoors.items;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.inventory.LauncherScreenHandler;
import com.google.common.collect.Lists;
import com.simibubi.create.content.contraptions.components.structureMovement.glue.SuperGlueEntity;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DoorbladeEntity extends PersistentProjectileEntity {
    private static final TrackedData<Boolean> LOYALTY;
    private boolean dealtDamage;
    public int returnTimer;
    public boolean landed = false;
    public int landingTime;
    private int groundAge = 0;

    public DoorbladeEntity(EntityType<? extends DoorbladeEntity> entityType, World world) {
        super(entityType, world);
    }

    public DoorbladeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(Halfdoors.DOORBLADE_ENTITY, owner.getX(), owner.getEyeY() - 0.4D, owner.getZ(), world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        }
        this.dataTracker.set(LOYALTY, DoorLauncherItem.hasTridentUpgrade(stack));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LOYALTY, false);
    }

    public void tick() {
        if (this.inGround) {
            groundAge++;
            if (groundAge > 5) {
                this.dealtDamage = true;
            }
            if (groundAge > 200) {
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }
                this.discard();
            }
        } else {
            this.groundAge = 0;
        }
        Entity entity = this.getOwner();
        boolean i = this.dataTracker.get(LOYALTY);
        if (i && (this.dealtDamage || this.isNoClip()) && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = entity.getPos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.1D, this.getZ());
                if (this.world.isClient) {
                    this.lastRenderY = this.getY();
                }
                this.setVelocity(vec3d.multiply(0.45D));
                this.returnTimer++;
            }
        }
        if (!landed) {
            landingTime = age;
            if (inGround) {
                landed = true;
            }
        }
        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack asItemStack() {
        return new ItemStack(Halfdoors.IRON_HALFDOOR);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (this.piercedEntities == null) {
            this.piercedEntities = new IntOpenHashSet(5);
        }
        if (this.piercingKilledEntities == null) {
            this.piercingKilledEntities = Lists.newArrayListWithCapacity(5);
        }
        if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
            this.discard();
            return;
        }
        this.piercedEntities.add(entity.getId());

        if (FabricLoader.getInstance().isModLoaded("create")) {
            if (entity instanceof SuperGlueEntity) {
                return;
            }
        }

        Entity owner = this.getOwner();
        DamageSource damageSource;
        if (owner == null) {
            damageSource = DamageSource.trident(this, this);
        } else {
            damageSource = DamageSource.trident(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity)owner).onAttacking(entity);
            }
        }
        boolean bl = entity.getType() == EntityType.ENDERMAN;
        if (!bl) {
            entity.timeUntilRegen = 0;
            if (this.isOnFire()) {
                entity.setFireTicks(entity.getFireTicks() + this.getFireTicks());
            }
        }
        if (entity.damage(damageSource, (float) this.getDamage())) {
            if (bl) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (!this.world.isClient && owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)owner, livingEntity);
                }
                this.onHit(livingEntity);
                if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
                if (!entity.isAlive() && this.piercingKilledEntities != null) {
                    this.piercingKilledEntities.add(livingEntity);
                }
            }
            this.playSound(this.getSound(), 0.6F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
    }

    protected boolean tryPickup(PlayerEntity player) {
        ItemStack launcher = getLauncher(player);
        if (launcher != null && DoorLauncherItem.hasShulkerUpgrade(launcher) && !(player.currentScreenHandler instanceof LauncherScreenHandler)) {
            ItemStack ammo = DoorLauncherItem.readInventory(launcher);
            if (ammo.isEmpty()) {
                DoorLauncherItem.writeInventory(launcher, new ItemStack(Halfdoors.IRON_HALFDOOR));
                return true;
            }
            if (ammo.isOf(Halfdoors.IRON_HALFDOOR.asItem()) && ammo.getCount() < ammo.getItem().getMaxCount()) {
                ammo.increment(1);
                DoorLauncherItem.writeInventory(launcher, ammo);
                return true;
            }
        }
        if (super.tryPickup(player)) {
            return true;
        }
        if (this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack())) {
            return true;
        }
        this.dropStack(this.asItemStack(), 0.1f);
        this.discard();
        return true;
    }

    public ItemStack getLauncher(PlayerEntity player) {
        for(int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Halfdoors.DOOR_LAUNCHER) && DoorLauncherItem.hasShulkerUpgrade(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    protected SoundEvent getHitSound() {
        return Halfdoors.DOORBLADE_HIT_GROUND;
    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dealtDamage = nbt.getBoolean("DealtDamage");
        this.dataTracker.set(LOYALTY, nbt.getBoolean("Loyalty"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Loyalty", this.dataTracker.get(LOYALTY));
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void age() {
        if (this.pickupType != PersistentProjectileEntity.PickupPermission.ALLOWED || !this.dataTracker.get(LOYALTY)) {
            super.age();
        }
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    static {
        LOYALTY = DataTracker.registerData(DoorbladeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}