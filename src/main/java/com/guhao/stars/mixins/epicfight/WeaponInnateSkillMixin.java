package com.guhao.stars.mixins.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

@Mixin(value = WeaponInnateSkill.class,remap = false)
public class WeaponInnateSkillMixin {

}
