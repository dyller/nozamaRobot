package behavier;

import java.util.Queue;

import org.freedesktop.dbus.test.profile.Log;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
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
	Waypoint start = new Waypoint(1000, 1100);
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
		return _go && (!_navi.getPath().isEmpty() || !done );
	}

	@Override
	public void action() {
		System.out.print("action findpath");
		done = false;
		/*productDelivered = false;
		_pathList.clear();
		if (_pathList.isEmpty()) {
			try {
				LCD.clear();
				LCD.drawString("Calculating route", 0, 5);
				_navi.getPoseProvider().setPose(start.getPose());
				Delay.msDelay(50);
				long now = System.currentTimeMillis();
				_pathList.add(_pathFinder.findRoute(start.getPose(), _distiantion));
				System.out.println("time it take: " + (System.currentTimeMillis()-now));
				Delay.msDelay(50);
				_navi.setPath(_pathList.poll());
				Delay.msDelay(50);
			} catch (DestinationUnreachableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LCD.clear();
				LCD.drawString("Route error", 0, 5);
			}
		} else {
			LCD.clear();
			LCD.drawString("There are paths to get done, yet", 0, 5);
		}*/
		while (!suppress && (!_navi.getPath().isEmpty() || !done )) {
			_navi.followPath();
			
				if(_navi.pathCompleted())
				{
					System.out.println("Navi completed done");
					deliverProduct();
					
				}
		
			
			/*if (!productDelivered) {
				_navi.followPath();
				if (_navi.pathCompleted()) {
					LCD.clear();
					LCD.drawString("I reach destination", 0, 5);
					deliverProduct();
				}
			} else if (productDelivered) {
				_navi.followPath();
				if (_navi.pathCompleted()) {
					Sound.beep();
					LCD.clear();
					LCD.drawString("Im at the central", 0, 5);
					done = true;
					_go = false;
				}
			}*/
			/*
			 * if (!productDelivered) { Delay.msDelay(500); _navi.followPath(); if
			 * (_navi.pathCompleted()) { LCD.clear();
			 * LCD.drawString("I reach the destination", 0, 5); Sound.beep();
			 * _backMotor.getMotor().rotate(-360); productDelivered = true; LCD.clear();
			 * LCD.drawString("Route done", 0, 5); _backMotor.getMotor().stop();
			 * LCD.clear(); LCD.drawString("Calculating path to home", 0, 5); try {
			 * LCD.clear(); LCD.drawString("Calculating back", 0, 5);
			 * _pathList.add(_pathFinder.findRoute(_navi.getPoseProvider().getPose(),
			 * start)); } catch (DestinationUnreachableException e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); } _navi.setPath(_pathList.poll());
			 * 
			 * } } else if (productDelivered && _navi.pathCompleted()) {
			 * 
			 * LCD.clear(); LCD.drawString("Im at the central!", 0, 5); Sound.beep(); done =
			 * true; go = false; } else { _navi.followPath(); }
			 */
			Thread.yield();
		}
	}

	private void deliverProduct() {
		// TODO Auto-generated method stub
		LCD.clear();
		LCD.drawString("I reach the destination", 0, 5);
		Sound.beep();
		_backMotor.getMotor().rotate(-360);
		productDelivered = true;
		_backMotor.getMotor().close();
		done = true;
		
	}

	@Override
	public void suppress() {
		_navi.stop();
		// TODO Auto-generated method stub
		suppress = true;
	}

}
