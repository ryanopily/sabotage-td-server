package ml.zer0dasho.mcserver.net.server;

import java.util.function.Supplier;

public interface MinecraftScheduler {

	public void scheduleCycle(int cycle, Supplier<Boolean> task);
	public void scheduleLater(int ticksToWait, Supplier<Boolean> task);
	public void run();
	
}
