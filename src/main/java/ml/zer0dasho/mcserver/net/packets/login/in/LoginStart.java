package ml.zer0dasho.mcserver.net.packets.login.in;

import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.ReadablePacket;

public class LoginStart extends ReadablePacket {

	public final String name;
	
	public LoginStart(ByteBuffer data) {
		super(data);
		this.name = this.readUTF8();
	}
	
	public String toString() {
		return 
				"LoginStart:\n" 
				+ super.toString() 
				+ String.format(
						"name: %s\n",
						name
				);
	}
}