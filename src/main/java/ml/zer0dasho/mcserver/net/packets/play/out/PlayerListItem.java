package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class PlayerListItem extends MinecraftPacket {
	
	public final int ID = 0x38;
	
	public byte[] player;
	public int action, numberOfPlayers;
	
	public PlayerListItem(int action, int numberOfPlayers, byte[] player) {
		this.action = action;
		this.numberOfPlayers = numberOfPlayers;
		this.player = player;
	}
	
	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		VarInt.putVarInt(action, result);
		VarInt.putVarInt(numberOfPlayers, result);
		result.write(player);
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
}