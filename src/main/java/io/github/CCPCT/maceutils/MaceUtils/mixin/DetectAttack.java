package io.github.CCPCT.maceutils.MaceUtils.mixin;

import io.github.CCPCT.maceutils.MaceUtils.util.Logic;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.CCPCT.maceutils.MaceUtils.util.Packets;
import io.github.CCPCT.maceutils.MaceUtils.util.Chat;
import io.github.CCPCT.maceutils.MaceUtils.config.ModConfig;


@Mixin(ClientPlayerInteractionManager.class)
public class DetectAttack {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttack(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player == null) return;
        if (!ModConfig.get().mainToggle) return;
        Chat.debug("hit");

        int swapSlot = -1;

        if (ModConfig.get().smartSwap){
            swapSlot = Logic.getSuitableMaceSpot();
            Chat.debug("smart to " + swapSlot);

        } else if (ModConfig.get().fixedSwap){
            swapSlot = ModConfig.get().fixedSwapSlot;
            Chat.debug("fixed to " + swapSlot);
//            Chat.debug("Swapped to hotbar slot " + ModConfig.get().fixedSwapSlot);
//            Chat.debug(String.valueOf(EnchantmentHelper.getEnchantments(player.getMainHandStack())));
//            Chat.debug(String.valueOf(player.getMainHandStack().getEnchantments().getEnchantments().stream().anyMatch(entry -> entry.getKey().map(key -> key.getValue().equals(Identifier.of("minecraft", "density"))).orElse(false))));
        }
        if (swapSlot == -1) {
            Chat.send("No mace...");
            return;

        }
        if (ModConfig.get().smartSwap || ModConfig.get().fixedSwap) {
            Packets.selectHotbarSlot(swapSlot,false);
            if (ModConfig.get().swapBack) {
                Packets.selectHotbarSlot(player.getInventory().selectedSlot, true);
            } else {
                player.getInventory().selectedSlot = swapSlot;
            }
        }

    }

}