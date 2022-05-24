package amymialee.halfdoors.client;

import amymialee.halfdoors.items.flipper.TinyDoorEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class TinyDoorEntityModel extends EntityModel<TinyDoorEntity> {
    private final ModelPart root;
    public final ModelPart saw;

    public TinyDoorEntityModel(ModelPart root) {
        super(RenderLayer::getEntityCutout);
        this.root = root;
        this.saw = root.getChild("door");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("door", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -2F, -8.0F, 16.0F, 4F, 16.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 17);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(TinyDoorEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}
}
