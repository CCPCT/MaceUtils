package io.github.CCPCT.maceutils.MaceUtils.mixin;

import io.github.CCPCT.maceutils.MaceUtils.util.Logic;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.CCPCT.maceutils.MaceUtils.util.Packets;
import io.github.CCPCT.maceutils.MaceUtils.util.Chat;
import io.github.CCPCT.maceutils.MaceUtils.config.ModConfig;


@Mixin(ClientPlayerInteractionManager.class)
public class DetectAttack {
    @Unique
    private int clock = -1;

    @Unique
    private int selectedSlot;

    @Inject(method = "attackEntity", at = @At("HEAD"))
    public void onAttackClient(PlayerEntity player, Entity target, CallbackInfo ci) {

        if (player == null) return;
        if (!ModConfig.get().mainToggle) return;

        if (!(target instanceof LivingEntity livingTarget)) return;
        int armourValue = livingTarget.getArmor();
        Chat.debug("armour value: "+armourValue);

        int swapSlot = -1;
        selectedSlot = player.getInventory().selectedSlot;

        if (ModConfig.get().smartSwap){
            if (armourValue < 15 && Logic.getFallingSpeed()>=-0.9) return;
            swapSlot = Logic.getSuitableMaceSpot();
            Chat.debug("smart to " + swapSlot);

        } else if (ModConfig.get().fixedSwap){
            swapSlot = ModConfig.get().fixedSwapSlot;
            Chat.debug("fixed to " + swapSlot);
        }

        player.getInventory().selectedSlot = swapSlot;

        if (swapSlot == -1) {
            Chat.send("No mace...");
            return;
        }

        if (ModConfig.get().smartSwap || ModConfig.get().fixedSwap) {
                // single player
            Packets.selectHotbarSlot(swapSlot, false);

            if (ModConfig.get().swapBack) {
                Chat.debug("selected slot: "+selectedSlot);

                Packets.selectHotbarSlot(selectedSlot, true);

                clock = 1;
            }
        }
    }

    @Inject(method="tick", at=@At("TAIL"))
    public void onTick(CallbackInfo ci) {

        if (clock==0) Logic.clientSelectSlot(selectedSlot);
        if (clock >= 0) clock--;
    }

}