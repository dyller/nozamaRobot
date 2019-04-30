package behavier;
import lejos.hardware.gps.Coordinates;
import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.ColorAdapter;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class DectectObject implements Behavior {
	Waypoint start = new Waypoint(1000,1100);
	ColorAdapter _colorAdaptor;
	boolean suppress = false;
Navigator _navi;
	public DectectObject(ColorAdapter colorAdapter, Navigator navi) {
		// TODO Auto-generated constructor stub
		this._colorAdaptor = colorAdapter;
		this._navi = navi;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		if(_navi.getClass())
		return _colorAdaptor.getColorID() != Color.NONE && _navi.getPath().get(_navi.getPath().size()-1) != start;
	}

	@Override
	public void action() {
		while(!suppress)
		{
		    LCD.drawString("DetectObject", 0,5);
		   
			if(_colorAdaptor.getColorID() == Color.NONE)
			{
				LCD.drawString("Ready Go", 0,5);
				
				suppress = true;
				
			}
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		suppress = true;
	}

}
