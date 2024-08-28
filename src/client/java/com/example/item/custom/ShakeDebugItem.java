package com.example.item.custom;

import com.example.Utilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ShakeDebugItem extends Item {
    public ShakeDebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!context.getWorld().isClient) {
            Utilities.shakeScreen(5, 0.1f, 8.0f);
        }
        return ActionResult.SUCCESS;
    }
}
