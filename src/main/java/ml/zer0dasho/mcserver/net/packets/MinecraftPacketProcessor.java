package ml.zer0dasho.mcserver.net.packets;

import java.nio.ByteBuffer;
import java.util.List;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public interface MinecraftPacketProcessor {

	public List<ByteBuffer> received();
	public ByteBuffer writeOut(WriteablePacket packet);
	public boolean readIn(ByteBuffer messageFragment);
	
}
