package com.guhao.stars.efmex;

import com.hm.efn.client.EFNCategorySlotTextures;
import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;
public class EpicSkillsCompat {
    public EpicSkillsCompat() {
    }

    public static void registerCategorySlotTexture() {
        CategorySlotTexture.ENUM_MANAGER.registerEnumCls("star", StarCategorySlotTextures.class);
    }
}
