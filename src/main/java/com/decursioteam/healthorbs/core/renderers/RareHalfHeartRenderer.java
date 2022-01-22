package com.decursioteam.healthorbs.core.renderers;

import com.decursioteam.healthorbs.HealthOrbs;
import com.decursioteam.healthorbs.core.HealthPickupsConfig;
import com.decursioteam.healthorbs.core.entities.RareHalfHeartEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RareHalfHeartRenderer extends EntityRenderer<RareHalfHeartEntity> {

    private static final ResourceLocation HALF_HEART_TEXTURE = new ResourceLocation(HealthOrbs.MOD_ID, "textures/entity/super_health_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(HALF_HEART_TEXTURE);

    public RareHalfHeartRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public boolean shouldRender(RareHalfHeartEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return super.shouldRender(p_114491_, p_114492_, p_114493_, p_114494_, p_114495_);
    }

    protected int getBlockLightLevel(RareHalfHeartEntity p_225624_1_, BlockPos p_225624_2_) {
        return Mth.clamp(super.getBlockLightLevel(p_225624_1_, p_225624_2_) + 7, 0, 15);
    }

    public void render(RareHalfHeartEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        int i = 1;
        float f = (float) (i % 4 * 16 + 0) / 64.0F;
        float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
        float f2 = (float) (i / 4 * 16 + 0) / 64.0F;
        float f3 = (float) (i / 4 * 16 + 16) / 64.0F;
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        float f7 = 255.0F;
        float f8 = ((float) p_225623_1_.tickCount + p_225623_3_) / 2.0F;
        int j = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int k = 255;
        int l = (int) ((Mth.sin(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
        p_225623_4_.translate(0.0D, (double) 0.1F, 0.0D);
        p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        float f9 = 0.3F;
        p_225623_4_.scale(0.3F, 0.3F, 0.3F);
        VertexConsumer ivertexbuilder = p_225623_5_.getBuffer(RENDER_TYPE);
        PoseStack.Pose matrixstack$entry = p_225623_4_.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        if(HealthPickupsConfig.COMMON.rhpAnimation.get()) {
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, j, 0, l, f, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, j, 0, l, f1, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, j, 0, l, f1, f2, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, j, 0, l, f, f2, p_225623_6_);
        }
        else{
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, 255, 255, 255, f, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, 255, 255, 255, f1, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, 255, 255, 255, f1, f2, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, 255, 255, 255, f, f2, p_225623_6_);
        }
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(RareHalfHeartEntity p_110775_1_) {
        return HALF_HEART_TEXTURE;
    }

    private static void vertex(VertexConsumer bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
        if(HealthPickupsConfig.COMMON.rhpAnimation.get()){
            bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 128).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
        }
        else bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 255).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
