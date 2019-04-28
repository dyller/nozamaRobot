package MapConstuctor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class ForwardNDraw  implements Behavior{
	boolean suppressed = false ;
	MovePilot _pilot;
	RangeFinderAdapter _rangeFinderAdapter; 
	int _distance;
public ForwardNDraw(RangeFinderAdapter rangefinderAdaptor, MovePilot pilot, int distance) {
	// TODO Auto-generated constructor stub
	_rangeFinderAdapter = rangefinderAdaptor;
	_pilot = pilot;
	_distance = distance;
	drawNAvoid();
}

public void drawNAvoid() {
	while(_rangeFinderAdapter.getRange() <= _distance) {
		LCD.drawString("ForwardNDraw", 0, 5);
		_pilot.forward();
		
	}
}

@Override
public boolean takeControl() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void action() {
	// TODO Auto-generated method stub
	
}

@Override
public void suppress() {
	// TODO Auto-generated method stub
	suppressed = true;
	
}

}
