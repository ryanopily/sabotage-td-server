package ml.zer0dasho.std.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;
import ml.zer0dasho.std.net.packets.STDPacketManager;
import ml.zer0dasho.std.net.packets.STDPacketProcessor;

public class STDConnection extends MinecraftConnection {

	protected STDPacketProcessor processor;
	protected List<MinecraftPacket> toSend;
	
	public STDConnection() {
		super();
		
		processor = new STDPacketProcessor();
		toSend = new ArrayList<>();
	}

	@Override
	public boolean receivePacket(ByteBuffer packetData) {
		return processor.readIn(packetData);
	}

	@Override
	public Iterator<MinecraftPacket> receivePackets() {
		
		List<MinecraftPacket> result = new ArrayList<>();
		Iterator<ByteBuffer> packets = processor.received().iterator();
		
		while(packets.hasNext()) {
			ByteBuffer packetData = packets.next();
			result.add(STDPacketManager.getPacket(packetData, this));
			packets.remove();
		}
		
		
		return result.iterator();
	}

	@Override
	public void sendPackets(SocketChannel client) {
		toSend.forEach(packet -> {
			try {
				System.out.println("Sending " + packet.getClass().getSimpleName());
				client.write(packet.encode());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		if(toSend.size() > 0) 		System.out.println("");
		toSend.clear();
	}

	@Override
	public void sendPackets(MinecraftPacket... packets) {
		toSend.addAll(Arrays.asList(packets));
	}
}
