package worker;

public class BaseWorker extends Thread{
	public boolean running = true;
	
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public long getLastWork() {
		return lastWork;
	}
	public void setLastWork(long lastWork) {
		this.lastWork = lastWork;
	}
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}
	public long lastWork = System.currentTimeMillis();
	public long error    = 0;
}
