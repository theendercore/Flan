package io.github.flemmli97.flan.fabric.mixin;

import io.github.flemmli97.flan.event.WorldEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Level.class)
public abstract class LevelMixin {

//    @Inject(method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;ZLnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/core/particles/ParticleOptions;Lnet/minecraft/sounds/SoundEvent;)Lnet/minecraft/world/level/Explosion;",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Explosion;explode()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void explode(Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionDamageCalculator, double d, double e, double f, float g, boolean bl, Level.ExplosionInteraction explosionInteraction, boolean bl2, ParticleOptions particleOptions, ParticleOptions particleOptions2, SoundEvent soundEvent, CallbackInfoReturnable<Explosion> cir, ParticleOptions particleOptions3, ParticleOptions particleOptions4, SoundEvent soundEvent2, Explosion.BlockInteraction blockInteraction2, Explosion explosion, Explosion explosion2) {
//        if ((Object) this instanceof ServerLevel serverLevel)
//            WorldEvents.modifyExplosion(explosion, serverLevel);
//    }
}
