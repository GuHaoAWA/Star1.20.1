package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import com.guhao.stars.units.data.TimeContext;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    public Gui gui;

    @Shadow
    public GameRenderer gameRenderer;

    @Shadow
    @Nullable
    public Screen screen;
    @Shadow
    public ProfilerFiller profiler;
    @Shadow
    public int rightClickDelay;
    @Shadow
    public int missTime;
    @Shadow
    @Nullable
    public Overlay overlay;
    @Shadow
    public Timer timer;
    @Shadow(remap = false)
    public float realPartialTick;
    @Shadow
    public float pausePartialTick;
    @Shadow
    public ChatListener chatListener;
    @Shadow
    public Tutorial tutorial;
    @Shadow
    @Nullable
    public HitResult hitResult;
    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    @Shadow
    public Options options;
    @Shadow
    public KeyboardHandler keyboardHandler;
    @Shadow
    public LevelRenderer levelRenderer;
    @Shadow
    @Nullable
    public IntegratedServer singleplayerServer;
    @Shadow
    public volatile boolean pause;
    @Shadow
    public SoundManager soundManager;
    @Unique
    protected boolean star$isTimeStop = false;

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void setScreen(@org.jetbrains.annotations.Nullable Screen p_91153_);

    @Shadow
    protected abstract void handleKeybinds();


    @Shadow public static Minecraft instance;

    @Inject(method = "getPartialTick", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void getPartialTick(CallbackInfoReturnable<Float> cir) {
        if (StarDataUnit.isTimeStopped()) cir.setReturnValue(timer.partialTick);
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onRenderTickStart(F)V", remap = false))
    private void runTick_modifyPartial(boolean p_91384_, CallbackInfo ci) {
        if (!p_91384_) return;
        long l = TimeContext.Client.timer.advanceTime(TimeContext.Both.getRealMillis());
        star$isTimeStop = StarDataUnit.isTimeStopped();
        if (StarDataUnit.isTimeStopped() && gameMode != null && player != null) {
            if (!pause) {
                timer.msPerTick = 1.0e32F;
                realPartialTick = timer.partialTick;
                for (int i = 0; i < l; i++) {
                    if (this.overlay == null && this.screen == null) {
                        this.profiler.push("Keybindings");
                        this.handleKeybinds();
                        if (this.missTime > 0) {
                            --this.missTime;
                        }
                        this.profiler.pop();

                    }
                    this.gameRenderer.tick();
                    this.profiler.push("keyboard");
                    this.keyboardHandler.tick();
                    this.profiler.pop();
                    if (level != null) {
                        if (this.screen == null && this.player != null) {
                            if (this.player.isDeadOrDying() && !(this.screen instanceof DeathScreen)) {
                                this.setScreen(null);
                            } else if (this.player.isSleeping()) {
                                this.setScreen(new InBedChatScreen());
                            }
                        } else {
                            Screen $$4 = this.screen;
                            if ($$4 instanceof InBedChatScreen inbedchatscreen) {
                                if (this.player != null && !this.player.isSleeping()) {
                                    inbedchatscreen.onPlayerWokeUp();
                                }
                            }
                        }

                        if (this.screen != null) {
                            this.missTime = 10000;
                        }

                        if (this.screen != null) {
                            Screen.wrapScreenError(() -> this.screen.tick(), "Ticking screen", this.screen.getClass().getCanonicalName());
                        }
                        if (player != null && (player.isSpectator() || player.isCreative())) {
                            if (this.rightClickDelay > 0) {
                                --this.rightClickDelay;
                            }

                            this.profiler.push("gui");
                            this.chatListener.tick();
                            this.gui.tick(false);
                            this.profiler.pop();
                            this.gameRenderer.pick(1.0F);
                            this.tutorial.onLookAt(this.level, this.hitResult);
                            this.profiler.push("gameMode");
                            if (this.gameMode != null) {
                                this.gameMode.tick();
                            }
                            this.profiler.pop();
                            if (!this.options.renderDebug) {
                                this.gui.clearCache();
                            }
                        }
                        if (level != null) {
                            level.tickingEntities.forEach((entity) -> {
                                if (!entity.isRemoved() && !entity.isPassenger()) {
                                    if (entity instanceof Player player1) {
                                        level.guardEntityTick(level::tickNonPassenger, entity);
                                        EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class).tick(new LivingEvent.LivingTickEvent(player1));
                                        EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class).tick(new LivingEvent.LivingTickEvent(player1));
/*
                                        EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class).tick(new LivingEvent.LivingTickEvent(player1));
*/
                                        EpicFightCapabilities.getEntityPatch(entity, LocalPlayerPatch.class).tick(new LivingEvent.LivingTickEvent(player1));
                                        EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class).getAnimator().tick();
                                        EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class).getClientAnimator().tick();
                                    }
                                }
                            });

                            try {
                                this.level.tick(() -> true);
                            } catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.forThrowable(throwable, "Exception in world tick");
                                if (this.level == null) {
                                    CrashReportCategory crashreportcategory = crashreport.addCategory("Affected level");
                                    crashreportcategory.setDetail("Problem", "Level is null!");
                                } else {
                                    this.level.fillReportDetails(crashreport);
                                }

                                throw new ReportedException(crashreport);
                            }
                        }
                    }

                }

            }
            if (star$isTimeStop) {
                if (screen instanceof DeathScreen) screen.tick();
                for (int i = 0; i < l; i++) {
                    this.soundManager.tick(true);
                }
            }
        } else {
            if (timer.msPerTick == 1.0e32F) timer.msPerTick = 50.0F;
        }
    }

}
