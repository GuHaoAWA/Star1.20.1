package com.guhao.stars.efmex;

import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.skill.SkillCategory;

public enum StarSkillCategories implements SkillCategory {
    DOTE(true, true, true,ResourceLocation.fromNamespaceAndPath("epicfight", "skillbook_dodge")),
    COUNTER(true, true, true,ResourceLocation.fromNamespaceAndPath("epicfight", "skillbook_dodge")),  //黄危危反
    COUNTERATTACK(true, true, true,ResourceLocation.fromNamespaceAndPath("epicfight", "skillbook_dodge"));  //高冲反斩

    final boolean save;
    final boolean sync;
    final boolean modifiable;
    final int id;
    final ResourceLocation bookIcon;
    StarSkillCategories(boolean ShouldSave, boolean ShouldSync, boolean Modifiable,ResourceLocation bookIcon) {
        this.modifiable = Modifiable;
        this.save = ShouldSave;
        this.sync = ShouldSync;
        this.id = SkillCategory.ENUM_MANAGER.assign(this);
        this.bookIcon = bookIcon;
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

    public ResourceLocation bookIcon() {
        return this.bookIcon == null ? SkillCategory.DEFAULT_BOOK_ICON : this.bookIcon;
    }
}
