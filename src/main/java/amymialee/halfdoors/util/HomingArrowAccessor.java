package amymialee.halfdoors.util;

import net.minecraft.entity.Entity;

public interface HomingArrowAccessor {
    Entity getHomingTarget();
    void setHomingTarget(Entity target);
    int getBounces();
    void setBounces(int bounces);
}
