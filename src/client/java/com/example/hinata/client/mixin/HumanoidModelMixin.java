package com.example.hinata.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;

/**
 * Hinata jumper: thinner sleeves.
 * Maid outfits: cat ears on the helmet.
 */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    // 1.0 = normal armor thickness. Lower = thinner. Try 0.7 to 0.9.
    private static final float SLIM = 0.7f;

    private static final String EAR_R = "hinata_cat_ear_r";
    private static final String EAR_L = "hinata_cat_ear_l";

    /**
     * Adds two cat ears (base + tip cube each) as children of the head.
     * Their texture lives in the unused strip at (56..64, 16..28) of the 64x32 armor texture,
     * so any armor that doesn't paint there simply doesn't show them.
     */
    @Inject(method = "createMesh(Lnet/minecraft/client/model/geom/builders/CubeDeformation;F)Lnet/minecraft/client/model/geom/builders/MeshDefinition;",
            at = @At("RETURN"))
    private static void hinata$addCatEars(CubeDeformation deformation, float yOffset,
                                          CallbackInfoReturnable<MeshDefinition> cir) {
        PartDefinition headDef = cir.getReturnValue().getRoot().getChild("head");

        CubeDeformation big  = new CubeDeformation(0.75f);
        CubeDeformation tipD = new CubeDeformation(0.6f);

        headDef.addOrReplaceChild(EAR_R, CubeListBuilder.create()
            .texOffs(56, 16).addBox(-4.0f, -10.0f, -2.0f, 3.0f, 2.0f, 1.0f, big)    // base
            .texOffs(56, 19).addBox(-3.0f, -13.0f, -2.0f, 1.0f, 2.0f, 1.0f, tipD),  // tip
            PartPose.ZERO);

        headDef.addOrReplaceChild(EAR_L, CubeListBuilder.create()
            .texOffs(56, 22).addBox(1.0f, -10.0f, -2.0f, 3.0f, 2.0f, 1.0f, big)     // base
            .texOffs(56, 25).addBox(2.0f, -13.0f, -2.0f, 1.0f, 2.0f, 1.0f, tipD),   // tip
            PartPose.ZERO);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("RETURN"))
    private void hinata$setupAnim(HumanoidRenderState state, CallbackInfo ci) {
        boolean isPlayerModel = (Object) this instanceof PlayerModel;

        // Cat ears: always enabled. They only show where the armor texture paints the ear strip,
        // i.e. on the maid helmets, so no name lookup is needed (it didn't work on players).
        if (head.hasChild(EAR_R)) head.getChild(EAR_R).visible = true;
        if (head.hasChild(EAR_L)) head.getChild(EAR_L).visible = true;

        if (isPlayerModel) return; // never touch the player's own arms

        Component name = state.chestEquipment.get(DataComponents.CUSTOM_NAME);
        float s = (name != null && "Hinata".equals(name.getString())) ? SLIM : 1.0f;

        rightArm.xScale = s; rightArm.zScale = s;
        leftArm.xScale = s;  leftArm.zScale = s;
    }
}
