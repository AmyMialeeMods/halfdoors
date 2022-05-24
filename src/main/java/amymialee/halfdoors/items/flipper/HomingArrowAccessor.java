package amymialee.halfdoors.items.flipper;

import net.minecraft.entity.Entity;

public interface HomingArrowAccessor {
    Entity getHomingTarget();
    void setHomingTarget(Entity target);
}
