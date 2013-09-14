package org.jglrxavpok.mods.decraft;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author jglrxavpok
 *
 */
public class ClientProxy extends CommonProxy
{

    @Override
    public void init()
    {
		TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
    }
}
