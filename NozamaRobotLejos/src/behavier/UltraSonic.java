package behavier;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.geometry.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class UltraSonic implements Behavior {

	RangeFinderAdapter _ultrasonicAdapter;
	Wheel _sonicMotor;
	Navigator _navi;
	int _heading;
	ShortestPathFinder _pathfinder;
	boolean supressed = false;
	private int minDistance = 10;
	Pose _naviPose;
	boolean objectRight = false;
	boolean objectLeft = false;
	boolean done = true;

	public UltraSonic(RangeFinderAdapter ultrasonicAdapter, Wheel sonicMotor, Navigator navi, ShortestPathFinder pathFinder) {
		// TODO Auto-generated constructor stub
		this._ultrasonicAdapter = ultrasonicAdapter;
		this._sonicMotor = sonicMotor;
		this._navi = navi;
		this._pathfinder = pathFinder;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		//return false;
		return _ultrasonicAdapter.getRange() < minDistance ||!done;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Delay.msDelay(20);
		done = false;
		_navi.stop();
		supressed = false;
		System.out.println("action");
		LCD.clear();
		LCD.drawString("Obstacle detected", 0, 5);
		_sonicMotor.getMotor().rotate(30);
		objectRight = _ultrasonicAdapter.getRange() <= 15;
		Delay.msDelay(1000);
		_sonicMotor.getMotor().rotate(-60);
		objectLeft = _ultrasonicAdapter.getRange() <= 15;
		_sonicMotor.getMotor().rotate(30);
		_naviPose = _navi.getPoseProvider().getPose();
		checkHeading();
		printLine();

	}

	private void printLine() {
		System.out.println("printline");
		if (objectRight && objectLeft) {
			System.out.println("heading: " + _heading);
			LCD.clear();
			LCD.drawString("Huge obstacle detected", 0, 5);
			switch(_heading) {
			case 0:
				_pathfinder.getMap().add(new Line(
						_naviPose.getX()+10,
						_naviPose.getY()+25,
						_naviPose.getX()+10,
						_naviPose.getY()-25));
				break;
			case 1:
				_pathfinder.getMap().add(new Line(
						_naviPose.getX()+25,
						_naviPose.getY()+10,
						_naviPose.getX()-25,
						_naviPose.getY()+10));
				break;
			case 2:
				_pathfinder.getMap().add(new Line(
						_naviPose.getX()-10,
						_naviPose.getY()+25,
						_naviPose.getX()-10,
						_naviPose.getY()-25));
				break;
			case 3:
				_pathfinder.getMap().add(new Line(
						_naviPose.getX()+25,
						_naviPose.getY()-10,
						_naviPose.getX()-25,
						_naviPose.getY()-10));
				break;
			}
			LCD.clear();
			LCD.drawString("Line added", 0, 5);
			try {
				System.out.println("calculate route");
				_pathfinder.findRoute(_naviPose, _navi.getPath().get(_navi.getPath().size()-1));
			} catch (DestinationUnreachableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (objectRight) {
				LCD.clear();
				LCD.drawString("Right obstacle detected", 0, 5);
			} else if (objectLeft) {
				LCD.clear();
				LCD.drawString("Left obstacle detected", 0, 5);
			}
		}
		done = true;
	}

	private void checkHeading() {
		// TODO Auto-generated method stub
		int angleOfHeading = (int) _naviPose.getHeading();
		switch(((angleOfHeading+45)%360)/90) {
			case 0://315 too 44
				_heading = 0;
			break;
			case 1: //45 to 134
				_heading = 1;
			break;
			case 2: //135 to 224
				_heading = 2;
			break;
			case 3: //225 to 314
				_heading = 3;
			break;
		}
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		supressed = true;
	}

}
