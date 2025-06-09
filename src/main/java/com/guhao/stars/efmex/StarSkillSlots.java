package com.guhao.stars.efmex;

import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillSlot;

public enum StarSkillSlots implements SkillSlot {
    DOTE(StarSkillCategories.DOTE),
    COUNTER(StarSkillCategories.COUNTER);

    final SkillCategory category;
    final int id;

    private StarSkillSlots(StarSkillCategories category) {
        this.category = category;
        this.id = SkillSlot.ENUM_MANAGER.assign(this);
    }

    public SkillCategory category() {
        return this.category;
    }

    public int universalOrdinal() {
        return this.id;
    }
}
