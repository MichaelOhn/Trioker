package utils.go;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task to execute once 5
 * seconds have passed.
 */

public class ReminderBeep {

  public Timer timer;
  public int i = 0;
  public int finaltime = 1000;
  public boolean stop = false;
  public ReminderBeep() {
	  TimerTask task = new TimerTask()
	  {
		  
	      @Override
	      public void run()
	      {
	         i++;
	         System.out.println("time : "+i);
	         if(stop == true) {
	        	 finaltime = i;
		         timer.cancel();
	             timer.purge();
	         }
	      }
	  };
	  timer = new Timer();
	  timer.schedule(task, 0, 1000);
	  
  }
  
  public void stop () {
	  stop = true;
  }
  
  public int getFinalTime() {
	  return this.finaltime;
  }
}
       