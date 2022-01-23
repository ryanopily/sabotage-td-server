package ml.zer0dasho.std.net.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import ml.zer0dasho.mcserver.net.server.MinecraftScheduler;

public class STDScheduler implements MinecraftScheduler {

	public int ticks = 0;
	public List<STDTask> scheduleLater = new ArrayList<>(), scheduleCycle = new ArrayList<>();
 	
	@Override
	public void scheduleLater(int ticksToWait, Supplier<Boolean> task) {
		scheduleLater.add(new STDTask(ticksToWait, task));
	}
	
	@Override
	public void scheduleCycle(int cycle, Supplier<Boolean> task) {
		scheduleCycle.add(new STDTask(cycle, task));
	}

	@Override
	public void run() {
		List<STDTask> removeLater = new ArrayList<>();
		List<STDTask> removeCycle = new ArrayList<>();
		
		scheduleLater.forEach(task -> {
			if(task.run() || task.finished()) 
				removeLater.add(task);
		});
		
		scheduleCycle.forEach(task -> {;
			if(task.run())
				removeCycle.add(task);
			
			else if(task.finished())
				task.ticksElapsed = 0;
		});
		
		scheduleLater.removeAll(removeLater);
		scheduleCycle.removeAll(removeCycle);
	}
	
	public static class STDTask {
		
		public int totalTicks, ticksElapsed;
		public Supplier<Boolean> task;
		
		public STDTask(int totalTicks, Supplier<Boolean> task) {
			this.totalTicks = totalTicks;
			this.task = task;
		}
		
		public boolean finished() {
			return ticksElapsed >= totalTicks;
		}
		
		public boolean run() {
			if(++ticksElapsed >= totalTicks)
				return task.get();
			
			return false;
		}
	}
}
