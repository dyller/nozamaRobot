package behavier;

import java.util.Queue;

import org.freedesktop.dbus.test.profile.Log;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.hardware.device.MSC;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class findpath implements Behavior {

	Queue<Path> _pathList;
	boolean home = true;
	Waypoint _distiantion;
	ShortestPathFinder _pathFinder;
	Navigator _navi;
	Wheel _backMotor;
	EV3 _brick;
	DectectObject _detectObject;
	boolean _go;
	boolean suppress = false;
	boolean done = true;
	boolean productDelivered = false;

	public findpath(Queue<Path> pathList, Waypoint distiantion, ShortestPathFinder pathFinder, Navigator navi,
			Wheel backMotor, EV3 brick, boolean go) {
		// TODO Auto-generated constructor stub
		this._distiantion = distiantion;
		this._pathFinder = pathFinder;
		_brick = brick;
		_go = go;
		_navi = navi;
		boolean done = true;
		_pathList = pathList;
		this._backMotor = backMotor;
		Button.ENTER.addKeyListener(new lejos.hardware.KeyListener() {

			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(Key k) {
				// TODO Auto-generated method stub
				LCD.clear();
				LCD.drawString("Checking if there is an object", 0, 4);
				if (checkIfObject())
					_go = true;
			}
		});
	}

	protected boolean checkIfObject() {
		// TODO Auto-generated method stub
		_detectObject = new DectectObject();
		return _detectObject.checkIfObject(_brick);
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return (!home || _go) && !_navi.getPath().isEmpty()  || !done ;
	}

	@Override
	public void action() {
		System.out.println("action findpath" + _navi.getPoseProvider().getPose());
		done = false;
		suppress = false;
		
		while (!suppress && (!_navi.getPath().isEmpty() || !done )) {
			_navi.followPath();
			
				if(_navi.pathCompleted())
				{
					System.out.println("Navi completed done");
					if (home) {
						_navi.clearPath();
					deliverProduct();}
					else {
						home= true;
						 _go = false;
						done = true;
						suppress = true;
					}
					
				}
			Thread.yield();
		}
	}

	private void deliverProduct() {
		// TODO Auto-generated method stub
		LCD.clear();
		LCD.drawString("I reach the destination", 0, 5);
		Sound.beep();
		_backMotor.getMotor().rotate(-360);
		_backMotor.getMotor().close();
		home = false;
		_go = true;
		done = true;
		suppress = true;
		
	}

	@Override
	public void suppress() {
		_navi.stop();
		// TODO Auto-generated method stub
		suppress = true;
	}

}
