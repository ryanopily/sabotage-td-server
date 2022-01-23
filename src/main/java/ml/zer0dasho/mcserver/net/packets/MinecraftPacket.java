package ml.zer0dasho.mcserver.net.packets;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class MinecraftPacket {
	
		public String toString() { return new String(); }

		public abstract void decode(ByteBuffer buffer) throws IOException;
		public abstract ByteBuffer encode() throws IOException;
		
}
