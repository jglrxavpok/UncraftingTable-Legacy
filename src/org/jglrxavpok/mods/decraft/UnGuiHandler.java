package org.jglrxavpok.mods.decraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;


/**
 * @author jglrxavpok
 */
public class UnGuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(world.getBlockId(x, y, z) == ModUncrafting.uncraftingTable.blockID)
		{
			ContainerUncraftingTable c = new ContainerUncraftingTable(player.inventory, world, world.getBlockMetadata(x, y, z) == 1,x,y,z);
			return c;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(world.getBlockId(x, y, z) == ModUncrafting.uncraftingTable.blockID)
		{
			String name = StatCollector.translateToLocal("tile.uncraftingtable.name");
			if(name == null)name = "Uncrafting Table";
			return new GuiUncraftingTable(player.inventory, world, name, world.getBlockMetadata(x, y, z) == 1,x,y,z);
		}
		return null;

	}

}