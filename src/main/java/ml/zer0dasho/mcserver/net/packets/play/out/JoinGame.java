package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class JoinGame extends MinecraftPacket {

	public final int ID = 0x01;
	
	public int entity_id;
	public byte gamemode;
	public byte dimension;
	public byte difficulty;
	public byte max_players;
	public String level_type;
	public boolean reduced_debug_info;

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		
		this.entity_id = buffer.getInt();
		this.gamemode = buffer.get();
		this.dimension = buffer.get();
		this.difficulty = buffer.get();
		this.max_players = buffer.get();
		this.level_type = NetUtils.readUTF8(buffer);
		this.reduced_debug_info = buffer.get() == 0 ? false : true;
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(ByteBuffer.allocate(Integer.BYTES).putInt(entity_id).array());
		result.write(gamemode);
		result.write(dimension);
		result.write(difficulty);
		result.write(max_players);
		result.write(NetUtils.writeUTF8(level_type));
		result.write((byte) (reduced_debug_info ? 1 : 0));
		
	
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
}
