package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class JoinGame extends MinecraftPacket {

	public final int ID = 0x01;
	
	public String levelType;
	public int entityId;
	public boolean reducedDebugInfo;
	public byte gamemode, dimension, difficulty, maxPlayers;

	public JoinGame(String levelType, int entityId, boolean reducedDebugInfo, byte gamemode, byte dimension,
			byte difficulty, byte maxPlayers) {
		this.levelType = levelType;
		this.entityId = entityId;
		this.reducedDebugInfo = reducedDebugInfo;
		this.gamemode = gamemode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		
		this.entityId = buffer.getInt();
		this.gamemode = buffer.get();
		this.dimension = buffer.get();
		this.difficulty = buffer.get();
		this.maxPlayers = buffer.get();
		this.levelType = NetUtils.readUTF8(buffer);
		this.reducedDebugInfo = buffer.get() == 0 ? false : true;
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(NetUtils.writeInts(entityId));
		result.write(gamemode);
		result.write(dimension);
		result.write(difficulty);
		result.write(maxPlayers);
		result.write(NetUtils.writeUTF8s(levelType));
		result.write((byte) (reducedDebugInfo ? 1 : 0));
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
}
