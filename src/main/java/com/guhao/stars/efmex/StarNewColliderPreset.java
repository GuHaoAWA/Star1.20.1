package com.guhao.stars.efmex;

import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class StarNewColliderPreset {
    public static final Collider BLADE_RUSH_FINISHER = new OBBCollider(1.2D, 0.8D, 2.0D, 0D, 1.0D, -1.2D);
    public static final Collider MORTAL_BLADE = new MultiOBBCollider(3, 0.4D, 2D, 4D, 0D, 0.5D, -5D);
    public static final Collider LETHAL_SLICING = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 1.0D, -1.0D);
    public static final Collider LETHAL_SLICING1 = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 0.5D, -1.0D);
    public static final Collider LORD_OF_THE_STORM = new MultiOBBCollider(3, 0.05D, 0.7D, 9.5D, 0.0D, 0.0D, -10.0D);
    public static final Collider YAMATO_SHEATH = new MultiOBBCollider(3, 0.5D, 0.5D, 1.0D, 0.0D, 0.0D, 0.5D);
    public static final Collider YAMATO_P = new MultiOBBCollider(3, 0.4D, 0.4D, 1.5D, 0.0D, 0.0D, -0.5D);
    public static final Collider YAMATO_DASH = new OBBCollider(1.7D, 1.0D, 2.0D, 0.0D, 1.0D, -1.0D);
    public static final Collider YAMATO_P0 = new OBBCollider(1.7D, 1.0D, -3.5D, 0.0D, 1.0D, -2.5D);
    public static final Collider YAMATO_DASH_FINISH = new OBBCollider(1.7D, 1.0D, 3.5D, 0.0D, 1.0D, 1.0D);
    public static final Collider YAMATO = new MultiOBBCollider(3, 0.4D, 0.4D, 1.0D, 0.0D, 0.0D, -0.5D);
    public static final Collider FATAL_DRAW_DASH = new OBBCollider(0.7, 0.7, 4.0, 0.0, 1.0, -4.0);

    public static final Collider EXECUTE = new MultiOBBCollider(3, 0.4D, 0.4D, 1.5D, 0.0D, 0.0D, -0.5D);
    public static final Collider EXECUTE_SECOND = new MultiOBBCollider(2, 0.8, 0.8, 2.0, 0.0, 1.0D, -1.0D);
    public static final Collider EXECUTE_SECOND_GREATSWORD = new MultiOBBCollider(2, 0.8, 1.0, 3.0, 0.0, 1.0D, 2.0D);

    public static final Collider SUPER_FIST = new MultiOBBCollider(4, 0.8, 0.8, 0.8, 0.0, 0.0, 0.0);
}
