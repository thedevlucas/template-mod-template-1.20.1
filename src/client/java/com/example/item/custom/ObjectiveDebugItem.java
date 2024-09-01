package com.example.item.custom;

import com.example.Utilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ObjectiveDebugItem extends Item {
    private int touched;

    public ObjectiveDebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            switch (touched){
                case 0:
                    Utilities.addObjective("interact with villagers ", player);
                    touched += 1;
                    break;
                case 1:
                    Utilities.addOFailedbjective(player);
                    touched = 2;
                    break;
                case 2:
                    Utilities.addCompletedObjective(player);
                    touched = 0;
                    break;
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }
}
