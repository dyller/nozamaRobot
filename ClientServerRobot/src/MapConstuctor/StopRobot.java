package MapConstuctor;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class StopRobot implements Behavior {
	public boolean clicked = false;
	public StopRobot (Key escape) {
		//this.clicked = escape;
		Button.ESCAPE.addKeyListener(new lejos.hardware.KeyListener() {

			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(Key k) {
				clicked = true;
				
			}
		});
	}
	private boolean suppressed = false; 
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return clicked;
	}

	@Override
	public void action() {
		suppressed = false;
	  Sound.beep();
		// TODO Auto-generated method stub
	  System.exit(0);
	}
	

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		suppressed = true;
	}

}
