//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class Sounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, StarsMod.MODID);
    public static final RegistryObject<SoundEvent> BONG = registerSoundEvent("bong");
    public static final RegistryObject<SoundEvent> BIGBONG = registerSoundEvent("bigbong");
    public static final RegistryObject<SoundEvent> DANGER = registerSoundEvent("danger");
    public static final RegistryObject<SoundEvent> DUANG1 = registerSoundEvent("duang1");
    public static final RegistryObject<SoundEvent> DUANG2 = registerSoundEvent("duang2");
    public static final RegistryObject<SoundEvent> FORESIGHT = registerSoundEvent("foresight");
    public static final RegistryObject<SoundEvent> YAMATO_STEP = registerSoundEvent("yamato_step");
    public static final RegistryObject<SoundEvent> SEKIRO = registerSoundEvent("sekiro");
    public static final RegistryObject<SoundEvent> CHUA = registerSoundEvent("chua");
    public static final RegistryObject<SoundEvent> PENG = registerSoundEvent("peng");
    public static final RegistryObject<SoundEvent> YAMATO_IN = registerSoundEvent("yamato_in");
    public static final RegistryObject<SoundEvent> CAILLO = registerSoundEvent("ciallo");
    public static final RegistryObject<SoundEvent> YUZUSOFT = registerSoundEvent("yuzusoft");

    public Sounds() {
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(StarsMod.MODID, name)));
    }
}