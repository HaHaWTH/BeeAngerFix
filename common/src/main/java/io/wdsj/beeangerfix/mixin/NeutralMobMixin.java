package io.wdsj.beeangerfix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NeutralMob.class)
public interface NeutralMobMixin {
    @Shadow void startPersistentAngerTimer();

    @WrapOperation(
            method = "updatePersistentAnger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/NeutralMob;setPersistentAngerTarget(Lnet/minecraft/world/entity/EntityReference;)V"
            )
    )
    private void beeAngerFix$moveLogic(NeutralMob instance, @Nullable EntityReference<LivingEntity> livingEntityEntityReference, Operation<Void> original, @Share("isNewTarget") LocalBooleanRef isNewTarget) {
        isNewTarget.set(true);
        original.call(instance, livingEntityEntityReference);
    }

    @WrapOperation(
            method = "updatePersistentAnger",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/NeutralMob;startPersistentAngerTimer()V"
            )
    )
    private void beeAngerFix$noStartPersistentAngerTimer(NeutralMob instance, Operation<Void> original, @Local(argsOnly = true) boolean updateAnger, @Share("isNewTarget") LocalBooleanRef isNewTarget) {
        if (updateAnger || isNewTarget.get()) original.call(instance);
    }
}
