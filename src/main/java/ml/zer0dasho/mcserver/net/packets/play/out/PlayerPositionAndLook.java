package ml.zer0dasho.mcserver.net.packets.play.out;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class PlayerPositionAndLook extends WriteablePacket {

	public PlayerPositionAndLook() {
		this.writeVarInt(0x08);
		this.writeDouble(0);
		this.writeDouble(0);
		this.writeDouble(0);
		this.writeFloat(0);
		this.writeFloat(0);
		this.writeByte((byte) 0);
	}
	
}
