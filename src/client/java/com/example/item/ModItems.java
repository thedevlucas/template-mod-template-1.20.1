package com.example.item;

import com.example.TemplateMod;
import com.example.item.custom.DialogueDebugItem;
import com.example.item.custom.ObjectiveDebugItem;
import com.example.item.custom.ShakeDebugItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item OBJECTIVE_DEBUG = registerItem("objective_debug", new ObjectiveDebugItem(new FabricItemSettings()));
    public static final Item DIALOGUE_DEBUG = registerItem("dialogue_debug", new DialogueDebugItem(new FabricItemSettings()));
    public static final Item SHAKE_DEBUG = registerItem("shake_debug", new ShakeDebugItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(TemplateMod.MOD_ID, name), item);
    }

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        entries.add(OBJECTIVE_DEBUG);
        entries.add(DIALOGUE_DEBUG);
        entries.add(SHAKE_DEBUG);
    }

    public static void registerModItems(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
