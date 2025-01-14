package net.dorianpb.cem.external.models;


import net.dorianpb.cem.internal.CemModelRegistry;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.entity.mob.EndermanEntity;

public class CemEndermanModel extends EndermanEntityModel<EndermanEntity>{
	private final CemModelRegistry registry;
	
	public CemEndermanModel(float scale, CemModelRegistry registry){
		super(scale);
		this.registry = registry;
		this.registry.initModels(this);
		this.head = this.registry.getModel("head");
		this.helmet = this.registry.getModel("headwear");
		this.torso = this.registry.getModel("body");
		this.leftArm = this.registry.getModel("left_arm");
		this.rightArm = this.registry.getModel("right_arm");
		this.leftLeg = this.registry.getModel("left_leg");
		this.rightLeg = this.registry.getModel("right_leg");
	}
	
	@Override
	public void setAngles(EndermanEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
	
}