package ml.zer0dasho.mcserver.net.packets.login.out;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class Disconnect extends WriteablePacket {
	
	public Disconnect() {
		String chat = "{\"text\":\"STD SERVER ;)!\"}";
		this.writeVarInt(0x0);
		this.writeUTF8(chat);
	}
	
}
