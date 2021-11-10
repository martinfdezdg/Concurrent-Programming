package IncDec;

public class MonitorIncDec {
	private volatile int var = 0;
    
	public synchronized void incrementar() {
		var++;
	}
	public synchronized void decrementar() {
		var--;
	}
	public int getVar() {
		return var;
	}
}
