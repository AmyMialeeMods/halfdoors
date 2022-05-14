package amymialee.halfdoors.client;

import amymialee.halfdoors.Halfdoors;
import amymialee.halfdoors.HalfdoorsClient;
import amymialee.halfdoors.items.DoorbladeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class DoorSawEntityRenderer extends EntityRenderer<DoorbladeEntity> {
    public static final Identifier TEXTURE = Halfdoors.id("textures/entity/door_saw.png");
    private final DoorSawEntityModel model;

    public DoorSawEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new DoorSawEntityModel(context.getPart(HalfdoorsClient.DOOR_SAW_LAYER));
    }

    public void render(DoorbladeEntity doorSaw, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, doorSaw.prevYaw, doorSaw.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, doorSaw.prevPitch, doorSaw.getPitch())));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(doorSaw.landingTime));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(doorSaw)), false, false);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        super.render(doorSaw, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }

    public Identifier getTexture(DoorbladeEntity doorSaw) {
        return TEXTURE;
    }
}