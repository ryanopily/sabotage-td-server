package ml.zer0dasho.mcserver.net.packets.login.out;

import java.util.UUID;

import ml.zer0dasho.mcserver.net.Connection;
import ml.zer0dasho.mcserver.net.Connection.ConnectionState;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class LoginSuccess extends WriteablePacket {
	
	public LoginSuccess() {
		this.writeVarInt(0x02);
		this.writeUTF8(UUID.randomUUID().toString());
		this.writeUTF8("zer0dashoml");
	}
	
	@Override
	public void apply(Connection connection) {
		connection.state = ConnectionState.PLAY;
	}
}
