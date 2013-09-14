package org.jglrxavpok.mods.decraft;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.DataInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;


/**
 * 
 * @author jglrxavpok
 *
 */
public class ClientPacketHandler implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player)
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(payload.data));
		if(payload.channel.equals("Uncrafting"))
		{
			handlePacket(payload);
		}
	}

	private void handlePacket(Packet250CustomPayload packet)
	{
	}
}