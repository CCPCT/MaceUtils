package io.github.CCPCT.maceutils.MaceUtils.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class configScreen extends Screen {

    protected configScreen() {
        super(Text.literal("Mace Utils Config"));
    }

    public static Screen getConfigScreen(Screen parent) {
        ModConfig.load();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Mace Utils Config"))
                .setSavingRunnable(ModConfig::save);

        ConfigCategory generalTab = builder.getOrCreateCategory(Text.literal("General"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Main mod toggle"),ModConfig.get().mainToggle)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Enable to use the mod"))
                .setSaveConsumer(newValue -> ModConfig.get().mainToggle = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Chat feedback"),ModConfig.get().chatfeedback)
                .setDefaultValue(true)
                .setTooltip(Text.literal("feedback in chat"))
                .setSaveConsumer(newValue -> ModConfig.get().chatfeedback = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Swap back"),ModConfig.get().swapBack)
                .setDefaultValue(true)
                .setTooltip(Text.literal("swap back to original hotbar slot after attribute swapping"))
                .setSaveConsumer(newValue -> ModConfig.get().swapBack = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Fixed swap"),ModConfig.get().fixedSwap)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Swap to a specific hotbar slot when attacking an entity. Will be overridden by smart swap if both enabled"))
                .setSaveConsumer(newValue -> ModConfig.get().fixedSwap = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startIntField(Text.literal("Fixed swap slot"),ModConfig.get().fixedSwapSlot)
                .setDefaultValue(0)
                .setMin(0)
                .setMax(8)
                .setTooltip(Text.literal("Fixed swap to this slot, 0-8"))
                .setSaveConsumer(newValue -> ModConfig.get().fixedSwapSlot = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Smart swap"),ModConfig.get().smartSwap)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Use different mace enchantment depends on fall speed, will override fixed swap if both enabled"))
                .setSaveConsumer(newValue -> ModConfig.get().smartSwap = newValue)
                .build());

        generalTab.addEntry(entryBuilder.startBooleanToggle(Text.literal("Debug"),ModConfig.get().debug)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> ModConfig.get().debug = newValue)
                .build());




        return builder.build();
    }
}
