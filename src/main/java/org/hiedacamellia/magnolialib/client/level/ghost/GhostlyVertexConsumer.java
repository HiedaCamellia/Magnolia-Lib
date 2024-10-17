package org.hiedacamellia.magnolialib.client.level.ghost;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.jetbrains.annotations.NotNull;

public class GhostlyVertexConsumer implements VertexConsumer {
    public static final GhostlyVertexConsumer INSTANCE = new GhostlyVertexConsumer();
    private VertexConsumer parent;
    private int offsetX;
    private int offsetY;
    private int offsetZ;

    public static GhostlyVertexConsumer of(VertexConsumer parent, int offsetX, int offsetY, int offsetZ) {
        INSTANCE.parent = parent;
        INSTANCE.offsetX = offsetX;
        INSTANCE.offsetY = offsetY;
        INSTANCE.offsetZ = offsetZ;
        return INSTANCE;
    }

    @Override
    public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay) {
        this.putBulkData(pose, quad, new float[]{1.0F, 1.0F, 1.0F, 1.0F}, red, green, blue, alpha, new int[]{packedLight, packedLight, packedLight, packedLight}, packedOverlay, false);
    }

    @Override
    public VertexConsumer setOverlay(int packedOverlay) {
        return parent.setUv1(packedOverlay & '\uffff', packedOverlay >> 16 & '\uffff');
    }

    @Override
    public VertexConsumer addVertex(float v, float v1, float v2) {
        return parent.addVertex(v, v1, v2);
    }

    @Override
    public VertexConsumer setColor(int i, int i1, int i2, int i3) {
        return parent.setColor(i, i1, i2, i3);
    }

    @Override
    public VertexConsumer setUv(float v, float v1) {
        return parent.setUv(v, v1);
    }

    @Override
    public VertexConsumer setUv1(int i, int i1) {
        return parent.setUv1(i, i1);
    }

    @Override
    public VertexConsumer setUv2(int i, int i1) {
        return parent.setUv2(i, i1);
    }

    @Override
    public VertexConsumer setNormal(float v, float v1, float v2) {
        return parent.setNormal(v, v1, v2);
    }
}
