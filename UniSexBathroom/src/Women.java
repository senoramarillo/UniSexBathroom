
public class Women extends Thread{
	
	ShowerMonitor monitor;
	int id;
	
	public Women(ShowerMonitor monitor, int id){
		this.monitor = monitor;
		this.id = id;
	}

	public void run(){
		int i=0;
		while(true){
			try {
				monitor.enterWoman(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monitor.leaveWoman(id);
		}
	}
}
