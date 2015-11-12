package de.xilent.kadsebot;

public class GradesThread extends Thread {

	
	
	@Override
	public void run() {
		while(true) {
			try {
				sleep(600000);
			} catch (InterruptedException e) {
				return;
			}
			for(GradesCheckContainer c : Receiver.instance.checkGrades)  {
				try {
					c.checkForNewGrades();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
