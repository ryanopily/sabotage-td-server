package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class PlayerPositionAndLook extends MinecraftPacket {

	public final int ID = 0x08;
	public double x, y, z;
	public float yaw, pitch;
	public boolean onGround;

	public PlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		
		this.x = buffer.getDouble();
		this.y = buffer.getDouble();
		this.z = buffer.getDouble();
		this.yaw = buffer.getFloat();
		this.pitch = buffer.getFloat();
		this.onGround = buffer.get() == 0 ? false : true;
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(NetUtils.writeDoubles(x, y, z));
		result.write(NetUtils.writeFloats(yaw, pitch));
		result.write(onGround ? 1 : 0);
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
}
