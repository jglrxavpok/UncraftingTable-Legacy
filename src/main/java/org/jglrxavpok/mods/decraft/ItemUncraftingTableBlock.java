package org.jglrxavpok.mods.decraft;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class ItemUncraftingTableBlock extends ItemBlock
{

    public ItemUncraftingTableBlock(Block block)
    {
        super(block);
    }

    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        super.onCreated(stack, world, player);
        if(super.field_150939_a == ModUncrafting.instance.uncraftingTable)
            player.addStat(ModUncrafting.instance.craftTable, 1);
    }

}
