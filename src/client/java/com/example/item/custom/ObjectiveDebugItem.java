package com.example.item.custom;

import com.example.Utilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;

import net.minecraft.util.ActionResult;

public class ObjectiveDebugItem extends Item {
    public ObjectiveDebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!context.getWorld().isClient) {
            PlayerEntity plr = context.getPlayer();
            Utilities.addObjective("Hello world!", plr);


        }
        return ActionResult.SUCCESS;
    }
}
