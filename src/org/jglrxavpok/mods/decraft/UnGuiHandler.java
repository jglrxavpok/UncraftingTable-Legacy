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

	private ContainerUncraftingTable	lastServerContainer;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z) == ModUncrafting.uncraftingTable)
		{
			if(id == 0)
			{
				ContainerUncraftingTable c = new ContainerUncraftingTable(player.inventory, world, world.getBlockMetadata(x, y, z) == 1,x,y,z,ModUncrafting.standardLevel,ModUncrafting.maxUsedLevel);
				lastServerContainer = c;
				return c;
			}
			else if(id == 1)
			{
				if(lastServerContainer != null)
				{
					lastServerContainer.onContainerClosed(player);
					lastServerContainer = null;
				}
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z) == ModUncrafting.uncraftingTable)
		{
			if(id == 0)
			{
				String name = StatCollector.translateToLocal("tile.uncrafting_table.name");
				if(name == null)name = "Uncrafting Table";
				return new GuiUncraftingTable(player.inventory, world, name, world.getBlockMetadata(x, y, z) == 1,x,y,z, ModUncrafting.modInstance.minLvlServer,ModUncrafting.modInstance.maxLvlServer);
			}
			else if(id == 1)
			{
				return new GuiUncraftOptions();
			}
		}
		return null;
	}

}