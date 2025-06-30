package io.github.CCPCT.maceutils.MaceUtils.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;


import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;


public class Packets implements ClientModInitializer {
    private static ArrayList<Packet<?>> packetsToSend = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (packetsToSend.isEmpty()){
                return;
            }
            if (packetsToSend.getFirst() == null){
                packetsToSend.removeFirst();
                return;
            }

            sendPacket(packetsToSend.getFirst());

            packetsToSend.removeFirst();
        });
    }

    public static void sendPacket(Packet<?> packet) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return;
        networkHandler.sendPacket(packet);
    }

    //packet methods
    public static void swapItems(int start, int end) {
        // use protocal number
        ItemStack startItem = Logic.getItemStack(start);
        if (startItem == null) return;
        ItemStack endItem = Logic.getItemStack(end);
        if (endItem == null) return;

        Chat.debug(start + " to " + end);
        Chat.debug(startItem + " to " + endItem);
        clickItem(start,ItemStack.EMPTY,false);
        clickItem(end,startItem,false);

        //dont need if item in end slot is originally empty
        Chat.debug(endItem.getItem() + "");
        if (endItem.getItem() == Items.AIR) return;
        clickItem(start,endItem,false);
    }

    public static void clickItem(int slot, ItemStack holding, boolean delay) {
        if (MinecraftClient.getInstance().player == null) return;
        ScreenHandler screenHandler = MinecraftClient.getInstance().player.currentScreenHandler;
        if (delay) {
            packetsToSend.add(new ClickSlotC2SPacket(
                    screenHandler.syncId,
                    screenHandler.getRevision(),
                    slot,
                    0,
                    SlotActionType.PICKUP,
                    holding,
                    new Int2ObjectOpenHashMap<>()
            ));
        } else {
            sendPacket(new ClickSlotC2SPacket(
                    screenHandler.syncId,
                    screenHandler.getRevision(),
                    slot,
                    0,
                    SlotActionType.PICKUP,
                    holding,
                    new Int2ObjectOpenHashMap<>()
            ));
        }
    }

    public static void useItem(boolean delay){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (delay) {
            packetsToSend.add(new PlayerInteractItemC2SPacket(
                    Hand.MAIN_HAND,
                    0,
                    player.getYaw(),
                    player.getPitch()

            ));
        } else {
            sendPacket(new PlayerInteractItemC2SPacket(
                    Hand.MAIN_HAND,
                    0,
                    player.getYaw(),
                    player.getPitch()

            ));
        }
    }

    public static void selectHotbarSlot(int slot, boolean delay) {
        // use protocal number
        if (slot < 0 || slot > 8) return; // validate slot
        if (delay){
            packetsToSend.add(new UpdateSelectedSlotC2SPacket(slot));
        } else {
            sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }
    }
}