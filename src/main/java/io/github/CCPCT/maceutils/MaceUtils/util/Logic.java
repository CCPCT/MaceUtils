package io.github.CCPCT.maceutils.MaceUtils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.minecraft.util.Identifier;

public class Logic {

    public static int getItemSpot(Item item){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (player.getInventory().main.get(i).getItem() == item){
                return i;
            }
        }
        return -1;
    }


    public static int getSuitableMaceSpot(){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        double fallingSpeed = player.getVelocity().y;
        Chat.debug("Falling Speed: " + fallingSpeed);

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
        if (fallingSpeed<-0.9){
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

    public static ItemStack getItemStack(int slot){
        // input protocol number
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;
        if (slot == 45) return player.getInventory().offHand.getFirst();
        else if (slot <= 8) return player.getInventory().armor.get(8-slot);
        else if (slot >= 36) return player.getInventory().main.get(slot-36);
        else return player.getInventory().main.get(slot);
    }

    public static int invToProtocolSlot(int slot,int invType){
        // invType -> 0:main, 1:armour, 2:offHand
        if (invType==2) return 45;
        if (invType==1) return 8-slot;
        if (invType==0){
            if (slot<=8) {
                return slot + 36;
            } else {
                return slot;
            }
        }
        return -1;
    }


    public static int getItemCount(Item item){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        PlayerInventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (inventory.main.get(i).getItem() == item){
                count += inventory.main.get(i).getCount();
            }
        }
        if (inventory.offHand.getFirst().getItem() == item){
            count += inventory.offHand.getFirst().getCount();
        }
        return count;
    }
}