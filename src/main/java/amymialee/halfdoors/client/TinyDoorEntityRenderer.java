package amymialee.halfdoors.client;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.HalfdoorsClient;
import amymialee.halfdoors.entities.TinyDoorEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class TinyDoorEntityRenderer<T extends TinyDoorEntity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE = Halfdoors.id("textures/entity/tiny_door.png");
    private final TinyDoorEntityModel model;

    public TinyDoorEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TinyDoorEntityModel(ctx.getPart(HalfdoorsClient.TINY_DOOR_LAYER));
    }

    protected int getBlockLight(T entity, BlockPos pos) {
        return 15;
    }

    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(0.1f, 0.1f, 0.1f);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevPitch - 90, entity.getPitch() - 90)));
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(entity.age));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.model.getLayer(this.getTexture(entity)), false, false);
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }

    public Identifier getTexture(TinyDoorEntity doorSaw) {
        return TEXTURE;
    }
}