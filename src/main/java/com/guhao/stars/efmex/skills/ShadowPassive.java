package com.guhao.stars.efmex.skills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class ShadowPassive extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174009");

    public static ShadowPassive.Builder createShadowPassiveBuilder() {
        return (new ShadowPassive.Builder())
                .setCategory(SkillCategories.PASSIVE)
                .setResource(Resource.NONE);
    }

    public static class Builder extends SkillBuilder<ShadowPassive> {}

    public ShadowPassive(ShadowPassive.Builder builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            if (event.getDamageSource().getEntity() instanceof LivingEntity target) {
                container.getExecutor().getOriginal().hurtMarked = true;
                Vec3 viewVec = target.getViewVector(1.0F);
                container.getExecutor().getOriginal().teleportTo(target.getX() + viewVec.x() * 4.5f, target.getY(), target.getZ() + viewVec.z() * 4.5f);
                container.getExecutor().playAnimationSynchronized(Animations.BATTOJUTSU_DASH,-0.60F);
                container.getExecutor().playSound(EpicFightSounds.EVISCERATE.get(),0f,0f);
            }
        },-1);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
            if (container.getExecutor().getAnimator().getPlayerFor(null).getAnimation() == Animations.BATTOJUTSU_DASH) {
                event.setResult(AttackResult.ResultType.MISSED);
                event.setCanceled(true);
            }
        },-1);
    }
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
    }
}
