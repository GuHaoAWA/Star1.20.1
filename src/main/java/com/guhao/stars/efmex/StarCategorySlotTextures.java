package com.guhao.stars.efmex;

import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;


public enum StarCategorySlotTextures implements CategorySlotTexture {

    COUNTER(3, 6, 38, 46),
    COUNTERATTACK(3, 6, 38, 46),
    EFN_SEKIRO(3, 6, 38, 46);


    private final int offsetX;
    private final int offsetY;
    private final int texWidth;
    private final int texHeight;
    private final int universalOrder;

    private StarCategorySlotTextures(int offsetX, int offsetY, int texWidth, int texHeight) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.universalOrder = CategorySlotTexture.ENUM_MANAGER.assign(this);
    }

    public int offsetX() {
        return this.offsetX;
    }

    public int offsetY() {
        return this.offsetY;
    }

    public int texWidth() {
        return this.texWidth;
    }

    public int texHeight() {
        return this.texHeight;
    }

    public int universalOrdinal() {
        return this.universalOrder;
    }
}

