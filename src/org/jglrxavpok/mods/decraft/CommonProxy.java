package org.jglrxavpok.mods.decraft;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author jglrxavpok
 *
 */
public class CommonProxy
{

	public void init()
	{
		TickRegistry.registerTickHandler(new ServerTickHandler(), Side.SERVER);
	};
}
