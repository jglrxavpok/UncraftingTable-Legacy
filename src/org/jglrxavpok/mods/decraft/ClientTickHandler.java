package org.jglrxavpok.mods.decraft;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler extends ServerTickHandler
{

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if(type.equals(EnumSet.of(TickType.CLIENT)))
		{
			GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
			if (guiscreen != null)
			{
				onTickInGui();
			}
			else onTickInGame();
		}
	}
	
	private void onTickInGui(){}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.PLAYER, TickType.CLIENT);
	}
}