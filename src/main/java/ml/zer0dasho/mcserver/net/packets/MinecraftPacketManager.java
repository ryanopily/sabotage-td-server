package ml.zer0dasho.mcserver.net.packets;

import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.Connection.ConnectionState;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.ReadablePacket;

public interface MinecraftPacketManager {

	public ReadablePacket getPacket(ByteBuffer packetData, ConnectionState connectionState);
	
}
