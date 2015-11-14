package de.xilent.kadsebot;

public class GradesThread extends Thread {

	
	
	@Override
	public void run() {
		while(true) {
			for(GradesCheckContainer c : Receiver.instance.checkGrades)  {
				try {
					c.checkForNewGrades();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
