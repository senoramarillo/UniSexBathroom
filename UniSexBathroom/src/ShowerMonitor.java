//Fehlerhaft


public class ShowerMonitor {

	int nm = 0;
	int nf = 0; 
	int n = 5;
	int man=0;
	int woman=0;
	
	
	public static void main(String[] args){
		ShowerMonitor monitor = new ShowerMonitor();
		Men m1 = new Men(monitor, 1);
		Men m2 = new Men(monitor, 2);
		Men m3 = new Men(monitor, 3);
		Women w1 = new Women(monitor, 1);
		Women w2 = new Women(monitor, 2);
		Women w3 = new Women(monitor, 3);
		m2.start();
		m3.start();
		m1.start();
		w1.start();
		w2.start();
		w3.start();
	}
	
	
	public synchronized void enterMan(int id) throws InterruptedException{
		man++;
		while((nf>0||nm>=n)||(man>0) && woman==0){
				out(Thread.currentThread().getName()+" Man "+id+" waits...");
				this.wait();
				man--;
		}
		nm++;
		out(Thread.currentThread().getName()+" Man "+id+" is trying to clean his body");
	}
	
	public synchronized void leaveMan(int id){
		nm--;
		out(Thread.currentThread().getName()+" Man "+id+" is leaving");
		if(nm==0){
			this.notifyAll();
		}
	}
	
	public synchronized void enterWoman(int id) throws InterruptedException{
		woman++;
		while((nm>0||nf>=n)||((woman>0 && man==0))){
				out(Thread.currentThread().getName()+" Woman "+id+" waits...");
				this.wait();
				woman--;
		}
		nf++;
		out(Thread.currentThread().getName()+" Woman "+id+" is trying to clean her body");
	}
		
	
	public synchronized void leaveWoman(int id){
		nf--;
		out(Thread.currentThread().getName()+" Woman "+id+" is leaving");
		if(nf==0){
			this.notifyAll();
		}
	}
	
	private synchronized void out(String string) {
		System.out.println(string);
	}

}
