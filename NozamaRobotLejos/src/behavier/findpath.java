package behavier;
import java.util.LinkedList;
import java.util.Queue;

import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.robotics.ColorAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class findpath implements Behavior {
	
	Queue<Path> pathList = new LinkedList<Path>();
	Waypoint start = new Waypoint(1000,1100);
	Waypoint _distiantion;
	ShortestPathFinder _pathFinder;
	MovePilot _pilot;
	Navigator navi;
	Wheel _backMotor;
	boolean go = false; 
	boolean suppress = false; 
	boolean done = false; 
	boolean delevery = false;
	public findpath(Waypoint distiantion, ShortestPathFinder pathFinder, Navigator navi, Wheel backMotor) {
		// TODO Auto-generated constructor stub
		this._distiantion =distiantion;
		this._pathFinder = pathFinder;
		//this._pilot = pilot;
		
		navi = navi;
		this._backMotor= backMotor;
		Button.ENTER.addKeyListener(new lejos.hardware.KeyListener() {

			
			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(Key k) {
				// TODO Auto-generated method stub
				go =true;
			}
		});
	}

	

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return go;
	}

	@Override
	public void action() {
		if (navi.pathCompleted() ||navi.getPath().isEmpty())
		{
		try {
			LCD.drawString("calculate route", 0, 5);
			navi.getPoseProvider().setPose(start.getPose());
			pathList.add(_pathFinder.findRoute(start.getPose(),_distiantion));
			//pathList.add(_pathFinder.findRoute(_distiantion.getPose(),start));
		} catch (DestinationUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LCD.drawString("Route error", 0, 5);
		}LCD.drawString("route done", 0, 5);
		}
		else
		{
			navi.followPath();
		}
		
		// TODO Auto-generated method stub
	while(!suppress && !done)
	{
		
		Thread.yield();
		if(navi.pathCompleted() ||navi.getPath().isEmpty())
		{
			LCD.drawString("new path", 0, 5);
			if(!pathList.isEmpty())
			{
				if(navi.getWaypoint() == _distiantion)
				{
					_backMotor.getMotor().rotate(-10);
				}
			navi.setPath(pathList.poll());
			navi.followPath();
			Sound.beep();
			}
			else if (!delevery)
			{
				_backMotor.getMotor().rotate(-360);
				Sound.beep();
				LCD.drawString("pathlist empty", 0, 5);
				done = true;
			}
			else 
			{
				
				try {
					navi.followPath(_pathFinder.findRoute(_distiantion.getPose(),start));
				} catch (DestinationUnreachableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		
		}
	}
	}

	@Override
	public void suppress() {
		navi.stop();
		// TODO Auto-generated method stub
		suppress = true;
	}

}
