package ml.zer0dasho.std.net.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class STDPacketProcessor {
	
	private int messageSize;
	private ByteArrayOutputStream partialMessage;
	
	private List<ByteBuffer> received = new ArrayList<ByteBuffer>();

	public List<ByteBuffer> received() {
		return this.received;
	}

	public ByteBuffer writeOut(MinecraftPacket packet) throws IOException {
		return packet.encode();
	}

	public boolean readIn(ByteBuffer messageFragment) {
		// Start reading new partial message
		if(partialMessage == null) {
			partialMessage = new ByteArrayOutputStream();
			messageSize = VarInt.getVarInt(messageFragment);
		}
		
		// Amount of bytes to complete message
		int sizeRemaining = messageSize - partialMessage.size();
		
		// Read bytes
		byte[] data = new byte[sizeRemaining > messageFragment.remaining() ? messageFragment.remaining() : sizeRemaining];
		messageFragment.get(data, 0, sizeRemaining > messageFragment.remaining() ? messageFragment.remaining() : sizeRemaining);
		
		// Add bytes to partial message
		partialMessage.write(data, 0, data.length);
		
		if(partialMessage.size() != messageSize)
			return false;
		
		// Add completed message to list
		byte[] completedMessage = partialMessage.toByteArray();
		
		ByteBuffer result = ByteBuffer.allocate(VarInt.varIntSize(messageSize) + messageSize);
		VarInt.putVarInt(messageSize, result);
		result.put(completedMessage);
		result.flip();
		
		received.add(result);
		partialMessage = null;
		
		// Start new message if necessary
		if(messageFragment.limit() > messageFragment.position())
			this.readIn(messageFragment.slice(messageFragment.position(), messageFragment.limit() - messageFragment.position()));
		
		return true;
	}
}