package com.guhao.stars.efmex;

import yesman.epicfight.skill.SkillCategory;

public enum StarSkillCategories implements SkillCategory {
    DOTE(true, true, true),
    COUNTER(true, true, true);



    final boolean save;
    final boolean sync;
    final boolean modifiable;
    final int id;

    private StarSkillCategories(boolean ShouldSave, boolean ShouldSync, boolean Modifiable) {
        this.modifiable = Modifiable;
        this.save = ShouldSave;
        this.sync = ShouldSync;
        this.id = SkillCategory.ENUM_MANAGER.assign(this);
    }

    public boolean shouldSave() {
        return this.save;
    }

    public boolean shouldSynchronize() {
        return this.sync;
    }

    public boolean learnable() {
        return this.modifiable;
    }

    public int universalOrdinal() {
        return this.id;
    }
}
