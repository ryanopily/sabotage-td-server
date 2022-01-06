package ml.zer0dasho.mcserver.net.packets.play.out;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class SpawnPosition extends WriteablePacket {

	public SpawnPosition() {
		this.writeVarInt(0x05);

		for(int i = 0; i < 8; i++) 
			this.writeByte((byte)0);
	}
	
}
