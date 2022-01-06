package ml.zer0dasho.mcserver.net.packets.play.out;

import java.util.concurrent.ThreadLocalRandom;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class KeepAlive extends WriteablePacket {
	
	public KeepAlive() {
		int random = ThreadLocalRandom.current().nextInt();
		
		this.writeVarInt(0);
		this.writeVarInt(random);
	}

}
