package io.github.CCPCT.maceutils.MaceUtils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;

import net.minecraft.util.Identifier;

public class Logic {
    public static int getFallingSpeed(){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return 0;
        double fallingSpeed = player.getVelocity().y;
        Chat.debug("Falling Speed: " + fallingSpeed);
        return (int) fallingSpeed;
    }
    public static int getSuitableMaceSpot(){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;

        // get mace slot
        // [density, breach]
        int[] slots = new int[]{-1, -1};
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().main.get(i).getItem() == Items.MACE){
                boolean containDensity = player.getInventory().main.get(i).getEnchantments().getEnchantments().stream().anyMatch(
                        entry -> entry.getKey().map(
                                key -> key.getValue().equals(Identifier.of("minecraft", "density"))).orElse(false));

                boolean containBreach = player.getInventory().main.get(i).getEnchantments().getEnchantments().stream().anyMatch(
                        entry -> entry.getKey().map(
                                key -> key.getValue().equals(Identifier.of("minecraft", "breach"))).orElse(false));

                if (containDensity){
                    slots[0] = i;
                }
                if (containBreach){
                    slots[1] = i;
                }

            }
        }
        if (slots[0] == -1 && slots[1] == -1) return -1;
        if (getFallingSpeed()<-0.9){
            if (slots[0] != -1){
                Chat.send("Using density mace at slot " + slots[0]);
                return slots[0];
            } else {
                Chat.send("No Density, Using breach mace at slot " + slots[1]);
                return slots[1];
            }
        } else {
            if (slots[1] != -1){
                Chat.send("Using breach mace at slot " + slots[1]);
                return slots[1];
            }
            // not returning density as not suitable for low fall speed
        }
        return -1;
    }

    public static void clientSelectSlot(int slot) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        PlayerInventory inventory = player.getInventory();
        if (slot < 0 || slot >= inventory.size()) return; // validate slot
        inventory.selectedSlot = slot; // set the selected slot
    }

}