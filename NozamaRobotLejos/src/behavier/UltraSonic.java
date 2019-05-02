package behavier;

import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;

public class UltraSonic implements Behavior{

	RangeFinderAdapter _ultrasonicAdapter;
	Wheel _sonicMotor;
	MovePilot _pilot;
	boolean supressed = false;
	private float distanceone;
	private float distancetwo;
	private int direction;

	public UltraSonic(RangeFinderAdapter ultrasonicAdapter, Wheel sonicMotor, MovePilot pilot) {
		// TODO Auto-generated constructor stub
		this._ultrasonicAdapter = ultrasonicAdapter;
		this._sonicMotor = sonicMotor;
		this._pilot = pilot;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
		//return _pilot.isMoving() && _ultrasonicAdapter.getRange()<20;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		_sonicMotor.getMotor().rotate(45);
		distanceone = _ultrasonicAdapter.getRange();
		_sonicMotor.getMotor().rotate(-90);
		distancetwo = _ultrasonicAdapter.getRange();
		if(distanceone <= 25 && distancetwo <= 25) {
			_pilot.travel(-50);
			direction = ((int) (Math.random() * 2));
			switch (direction) {
			case 0:
				_pilot.rotateLeft();
				break;
			case 1:
				_pilot.rotateRight();
				break;
			}
		}else {
			if(distanceone > distancetwo) {
			_pilot.rotate(50);
		}else if(distanceone < distancetwo){
			_pilot.rotate(-50);
		}
		}
		_sonicMotor.getMotor().rotate(45);
		while(!supressed && _pilot.isMoving())
		Thread.yield();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		supressed = true;
	}

}
