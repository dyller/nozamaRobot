package MapConstuctor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;

public class Forward implements Behavior {

	Navigator navi;
	Boolean suppressed;
	
	public Forward(Navigator navi) {
		this.navi = navi;
	}
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action() {
		
		// TODO Auto-generated method stub
		suppressed = false;
		navi.followPath();
		while(!suppressed)
		{
			LCD.drawString("forward", 0, 5);
			Thread.yield();
		}
		navi.stop();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		suppressed = true;
	}
}
