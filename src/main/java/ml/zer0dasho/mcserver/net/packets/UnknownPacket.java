package ml.zer0dasho.mcserver.net.packets;

import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.VarInt;

public class UnknownPacket extends MinecraftPacket {

	public int ID;
	public int size;
	public ByteBuffer buffer;
	
	public UnknownPacket(ByteBuffer buffer) {
		try {
			this.decode(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.buffer = buffer;
	}
	
	public String toString() {
		return 
				"MinecraftPacket:\n" 
				+ super.toString() 
				+ String.format(
						"id: %d\n"
							+ "size: %d\n",
						ID,
						size
				);
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		this.size = VarInt.getVarInt(buffer);
		this.ID = VarInt.getVarInt(buffer);
	}

	@Override
	public ByteBuffer encode() throws IOException {
		buffer.rewind();
		return buffer;
	}

}
