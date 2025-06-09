package com.guhao.stars.mixins.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.passive.TechnicianSkill;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

@Mixin(
        value = {TechnicianSkill.class},
        remap = false
)
public class TechnicianSkillMixin extends PassiveSkill {
    public TechnicianSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }
    @Unique
    private static final UUID EVENT_UUID = UUID.fromString("99e5c782-fdaf-11pb-9a03-0242ac130003");
    @Inject(method = "onInitiate" ,at = @At("HEAD"))
    public void onInitiate(SkillContainer container, CallbackInfo ci) {
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            float consumption = container.getExecuter().getModifiedStaminaConsume(container.getExecuter().getSkill(SkillSlots.DODGE).getSkill().getConsumption());
            container.getExecuter().setStamina(container.getExecuter().getStamina() + consumption * 0.08f);
        });
    }
    @Inject(method = "onRemoved" ,at = @At("HEAD"))
    public void onRemoved(SkillContainer container, CallbackInfo ci) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
    }
}
