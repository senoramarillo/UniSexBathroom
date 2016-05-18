
public class Men extends Thread{
	
	ShowerMonitor monitor;
	int id;
	
	public Men(ShowerMonitor monitor, int id){
		this.monitor = monitor;
		this.id = id;
	}
			
		public void run(){
			while(true){
				try {
					monitor.enterMan(id);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				monitor.leaveMan(id);
			}
		}
	}
	
