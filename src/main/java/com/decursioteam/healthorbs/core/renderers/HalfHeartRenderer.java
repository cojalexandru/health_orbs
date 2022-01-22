package com.decursioteam.healthorbs.core.renderers;

import com.decursioteam.healthorbs.HealthOrbs;
import com.decursioteam.healthorbs.core.HealthPickupsConfig;
import com.decursioteam.healthorbs.core.entities.HalfHeartEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HalfHeartRenderer extends EntityRenderer<HalfHeartEntity> {

    private static final ResourceLocation HALF_HEART_TEXTURE = new ResourceLocation(HealthOrbs.MOD_ID, "textures/entity/health_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(HALF_HEART_TEXTURE);

    public HalfHeartRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public boolean shouldRender(HalfHeartEntity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
        return true;
    }

    protected int getBlockLightLevel(HalfHeartEntity p_225624_1_, BlockPos p_225624_2_) {
        return MathHelper.clamp(super.getBlockLightLevel(p_225624_1_, p_225624_2_) + 7, 0, 15);
    }

    public void render(HalfHeartEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        int i = 1;
        float f = (float) (i % 4 * 16 + 0) / 64.0F;
        float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
        float f2 = (float) (i / 4 * 16 + 0) / 64.0F;
        float f3 = (float) (i / 4 * 16 + 16) / 64.0F;
        float f8 = ((float) p_225623_1_.tickCount + p_225623_3_) / 2.0F;
        int j = (int) ((MathHelper.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int k = 255;
        int l = (int) ((MathHelper.sin(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
        p_225623_4_.translate(0.0D, (double) 0.1F, 0.0D);
        p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        float f9 = 0.3F;
        p_225623_4_.scale(0.3F, 0.3F, 0.3F);
        IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(RENDER_TYPE);
        MatrixStack.Entry matrixstack$entry = p_225623_4_.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        if(HealthPickupsConfig.COMMON.nhpAnimation.get()) {
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, j, 255, l, f, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, j, 255, l, f1, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, j, 255, l, f1, f2, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, j, 255, l, f, f2, p_225623_6_);
        }
        else{
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, 15, 255, 15, f, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, 15, 255, 15, f1, f3, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, 15, 255, 15, f1, f2, p_225623_6_);
            vertex(ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, 15, 255, 15, f, f2, p_225623_6_);
        }
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(HalfHeartEntity p_110775_1_) {
        return HALF_HEART_TEXTURE;
    }

    private static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
        if(HealthPickupsConfig.COMMON.nhpAnimation.get()){
            bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 128).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
        }
        else bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 255).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
