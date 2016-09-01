/**
 * This class simulates the traffic signal at an intersection.
 * 
 * 
 * @author Aayushi Khurana
 * @author Pranay Shashank Adavi
 * 
 * 
 */


public class Traffic extends Thread {
    /** Color of the signal */
	private String color;
    /** Info of the thread */
	private int info;
	
    /** Traffic Light */
	Object light;
	/** Resource */
	Object sync;

	/**index-0 or 1*/
	private int index;
	/** red signal at index */
	static boolean[] redFlag = new boolean[2];
	
	/**
	 * Constructor for the class Traffic
	 * 
	 * @param light resource to synchronize
	 * @param info  info of the thread.
	 * @param sync  resource to synchronize 
	 * 
	 * @return void
	 */

	Traffic(Object light, int info, Object sync) {
		this.light = light;
		this.info = info;
		this.sync = sync;
		//Thread pairs-1,3 or 2,4
		index = (info) % 2;
		System.out.println("Index is " + index);
		changeColor("Red");

	}
	/**
	 * Change color of signal to the passed color
	 *
	 * @param    string    current color
	 */
	private void changeColor(String string) {
		color = string;
		System.out.println("Traffic Post " + info + ": " + color);
	}
	/**
	 * One traffic signal at an intersection
	 *  
	 * @return void
	 */
	public void signal() {
		synchronized (light) {


			// wait for opposite thread to turn red
			synchronized (sync) {
				
				if (!redFlag[index]) {
					try {
						redFlag[index] = true;
						sync.wait();
					} catch (InterruptedException e) {
						System.err.println("Error");
					}
				} else {
					sync.notify();
					redFlag[index] = false;
				}
			}
		// change color of thread to green	
			changeColor("Green");
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				//change signal color to yellow after 
				// some time
				changeColor("Yellow");
			}
			//change from yellow to red
			changeColor("Red");

		}
		try {
			sleep(100);
		} catch (InterruptedException e) {
		}

	}
	/**
	 * Interrupt the signal with color green
	 *  
	 * @return void
	 */
	
	private void changeSignal() {
		
		if (this.color.equals("Green")) {
			this.interrupt();
		}
	}



	/**
	 * Entry point for the thread.
	 * 
	 */

	public void run() {
		while (true) {
			signal();
		}

	}
	/**
	 * The main program.
	 * 
	 * @param args
	 *            command line arguments (ignored)
	 */

	public static void main(String args[]) {
		//resource
		Object obj1 = new Object();
		Object obj2 = new Object();
        //resource to make two signals with color red
		Object obj1_sync = new Object();
		Object obj2_sync = new Object();

		Traffic t1 = new Traffic(obj1, 1, obj1_sync);
		Traffic t2 = new Traffic(obj1, 2, obj2_sync);
		Traffic t3 = new Traffic(obj2, 3, obj1_sync);
		Traffic t4 = new Traffic(obj2, 4, obj2_sync);
		
		t1.start();
		t3.start();
		try {
			sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		t2.start();
		t4.start();
       //change the color of signal from green
		while (true) {
			t1.changeSignal();
			t3.changeSignal();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			t2.changeSignal();
			t4.changeSignal();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

