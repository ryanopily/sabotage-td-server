package ml.zer0dasho.mcserver.net.packets.play.out;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class JoinGame extends WriteablePacket {

	public JoinGame() {
		String level = "default";
		
		this.writeVarInt(0x01);
		this.writeInt(69);
		this.writeByte((byte)0);
		this.writeByte((byte)0);
		this.writeByte((byte)0);
		this.writeByte((byte)1);
		this.writeUTF8(level);
		this.writeByte((byte)0);
	}
	
}
