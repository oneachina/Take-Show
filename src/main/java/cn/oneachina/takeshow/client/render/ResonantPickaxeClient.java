package cn.oneachina.takeshow.client.render;

import cn.oneachina.takeshow.network.ResonantPickaxePingPayload;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ResonantPickaxeClient {

    private static final Map<BlockPos, Long> ACTIVE = new HashMap<>();
    private static RenderType XRAY_LINE_TYPE;

    private ResonantPickaxeClient() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ResonantPickaxePingPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (context.client().level == null) {
                    return;
                }
                long expiresAt = context.client().level.getGameTime() + payload.durationTicks();
                for (BlockPos pos : payload.positions()) {
                    ACTIVE.put(pos, expiresAt);
                }
            });
        });

        LevelRenderEvents.AFTER_TRANSLUCENT_TERRAIN.register(ResonantPickaxeClient::render);
    }

    private static void render(net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        long now = minecraft.level.getGameTime();
        if (ACTIVE.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<BlockPos, Long>> it = ACTIVE.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue() < now) {
                it.remove();
            }
        }
        if (ACTIVE.isEmpty()) {
            return;
        }

        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().position();
        PoseStack poseStack = context.poseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        MultiBufferSource.BufferSource bufferSource = context.bufferSource();
        RenderType renderType = getXrayLineType();
        VertexConsumer consumer = bufferSource.getBuffer(renderType);
        consumer.setLineWidth(2.0f);

        for (BlockPos pos : ACTIVE.keySet()) {
            AABB box = new AABB(pos).inflate(0.002);
            drawBoxLines(consumer, poseStack, box, 120, 190, 255, 180);
        }

        bufferSource.endBatch(renderType);
        poseStack.popPose();
    }

    private static RenderType getXrayLineType() {
        if (XRAY_LINE_TYPE != null) {
            return XRAY_LINE_TYPE;
        }

        RenderPipeline pipeline = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                .withLocation("pipeline/takeshow_xray_lines")
                .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
                .build();

        RenderSetup setup = RenderSetup.builder(pipeline)
                .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .createRenderSetup();

        XRAY_LINE_TYPE = RenderType.create("takeshow_xray_lines", setup);
        return XRAY_LINE_TYPE;
    }


    private static void drawBoxLines(VertexConsumer consumer, PoseStack poseStack, AABB box, int r, int g, int b, int a) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        line(consumer, poseStack, x1, y1, z1, x2, y1, z1, r, g, b, a);
        line(consumer, poseStack, x2, y1, z1, x2, y1, z2, r, g, b, a);
        line(consumer, poseStack, x2, y1, z2, x1, y1, z2, r, g, b, a);
        line(consumer, poseStack, x1, y1, z2, x1, y1, z1, r, g, b, a);

        line(consumer, poseStack, x1, y2, z1, x2, y2, z1, r, g, b, a);
        line(consumer, poseStack, x2, y2, z1, x2, y2, z2, r, g, b, a);
        line(consumer, poseStack, x2, y2, z2, x1, y2, z2, r, g, b, a);
        line(consumer, poseStack, x1, y2, z2, x1, y2, z1, r, g, b, a);

        line(consumer, poseStack, x1, y1, z1, x1, y2, z1, r, g, b, a);
        line(consumer, poseStack, x2, y1, z1, x2, y2, z1, r, g, b, a);
        line(consumer, poseStack, x2, y1, z2, x2, y2, z2, r, g, b, a);
        line(consumer, poseStack, x1, y1, z2, x1, y2, z2, r, g, b, a);
    }

    private static void line(VertexConsumer consumer, PoseStack poseStack, float x1, float y1, float z1, float x2, float y2, float z2, int r, int g, int b, int a) {
        consumer.addVertex(poseStack.last().pose(), x1, y1, z1).setColor(r, g, b, a).setNormal(0.0f, 1.0f, 0.0f).setLineWidth(2.0f);
        consumer.addVertex(poseStack.last().pose(), x2, y2, z2).setColor(r, g, b, a).setNormal(0.0f, 1.0f, 0.0f).setLineWidth(2.0f);
    }
}
