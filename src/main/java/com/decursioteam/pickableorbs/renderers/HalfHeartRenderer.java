package com.decursioteam.pickableorbs.renderers;

import com.decursioteam.pickableorbs.codec.ExtraOptions;
import com.decursioteam.pickableorbs.codec.OrbData;
import com.decursioteam.pickableorbs.entities.HalfHeartEntity;
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

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class HalfHeartRenderer extends EntityRenderer<HalfHeartEntity> {

    private ResourceLocation HALF_HEART_TEXTURE;
    private RenderType RENDER_TYPE;

    protected final int r;
    protected final int g;
    protected final int b;
    protected final boolean animation;

    public HalfHeartRenderer(EntityRendererProvider.Context renderManagerIn, OrbData orbData, ExtraOptions extraData) {
        super(renderManagerIn);
        this.shadowRadius = 0.15F;
        this.HALF_HEART_TEXTURE = orbData.getTexture();
        this.RENDER_TYPE = RenderType.entityTranslucent(HALF_HEART_TEXTURE);
        if(!orbData.getColor().contains("#")){
            String newColor = "#" + orbData.getColor();
            this.r = Color.decode(newColor).getRed();
            this.g = Color.decode(newColor).getGreen();
            this.b = Color.decode(newColor).getBlue();
        } else {
            this.r = Color.decode(orbData.getColor()).getRed();
            this.g = Color.decode(orbData.getColor()).getGreen();
            this.b = Color.decode(orbData.getColor()).getBlue();
        }
        this.animation = extraData.getAnimation();

        this.shadowStrength = 0.75F;
    }

    @Override
    public boolean shouldRender(HalfHeartEntity p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        return true;
    }

    protected int getBlockLightLevel(HalfHeartEntity p_225624_1_, BlockPos p_225624_2_) {
        return Mth.clamp(super.getBlockLightLevel(p_225624_1_, p_225624_2_) + 7, 0, 15);
    }

    public void render(HalfHeartEntity p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        int i = 1;
        float f = (float) (i % 4 * 16 + 0) / 64.0F;
        float f1 = (float) (i % 4 * 16 + 16) / 64.0F;
        float f2 = (float) (i / 4 * 16 + 0) / 64.0F;
        float f3 = (float) (i / 4 * 16 + 16) / 64.0F;
        float f8 = ((float) p_225623_1_.tickCount + p_225623_3_) / 2.0F;
        int R = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * (float)r);
        int G = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * (float)g);
        int B = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * (float)b);
        p_225623_4_.translate(0.0D, (double) 0.1F, 0.0D);
        p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        p_225623_4_.scale(0.3F, 0.3F, 0.3F);
        VertexConsumer ivertexbuilder = p_225623_5_.getBuffer(RENDER_TYPE);
        PoseStack.Pose matrixstack$entry = p_225623_4_.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();

        if(animation) {
            vertex(true, ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, R, G, B, f, f3, p_225623_6_);
            vertex(true,ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, R, G, B, f1, f3, p_225623_6_);
            vertex(true,ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, R, G, B, f1, f2, p_225623_6_);
            vertex(true,ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, R, G, B, f, f2, p_225623_6_);
        } else {
            vertex(false, ivertexbuilder, matrix4f, matrix3f, -0.5F, -0.25F, r, g, b, f, f3, p_225623_6_);
            vertex(false, ivertexbuilder, matrix4f, matrix3f, 0.5F, -0.25F, r, g, b, f1, f3, p_225623_6_);
            vertex(false, ivertexbuilder, matrix4f, matrix3f, 0.5F, 0.75F, r, g, b, f1, f2, p_225623_6_);
            vertex(false, ivertexbuilder, matrix4f, matrix3f, -0.5F, 0.75F, r, g, b, f, f2, p_225623_6_);
        }
        p_225623_4_.popPose();
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(HalfHeartEntity p_110775_1_) {
        return HALF_HEART_TEXTURE;
    }

    private static void vertex(boolean animation, VertexConsumer bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
        if(animation) {
            bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 155).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
        } else
        bufferIn.vertex(matrixIn, x, y, 0.0F).color(red, green, blue, 255).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
    }
}
