import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;

import org.jfree.chart.demo.PieChartDemo1;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.SVGMapLoader;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;

public class NozamaRobot {
	static String filePath = "denmark.svg";
	Waypoint start = new Waypoint(1000,2900);
	Waypoint esbjerg = new Waypoint(440,1800);
	Waypoint aarhus = new Waypoint(1300,400);
	Waypoint kolding = new Waypoint(2300,1800);
	Waypoint odense = new Waypoint(3600,1800);
	Waypoint soenderborg = new Waypoint(2500,3600);
	Waypoint currentWaypoint = null;
	boolean done = false;
	int acceleration = 50;
	int speed = 100;
	Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 55.130).offset(-69.80);
	Wheel rigthWheel = WheeledChassis.modelWheel(Motor.B, 55.5).offset(69.80);
	ShortestPathFinder pathFinder;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NozamaRobot nozamaRobot = new NozamaRobot();
		nozamaRobot.setup();
	}
	
	private void setup()
	{
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setAngularAcceleration(acceleration);
		pilot.setLinearAcceleration(acceleration);
		pilot.setAngularSpeed(speed);
		pilot.setLinearSpeed(speed);
		/*pilot.travel(2000);
		pilot.rotate(-180);
		pilot.travel(2000);
		pilot.rotate(-180);*/
		Sound.setVolume(Sound.VOL_MAX);
		Navigator navi = new Navigator(pilot);
		//navi.getPoseProvider().setPose(new Pose(1000,2900,0));
		try {
		Queue<Path> pathList = new LinkedList<Path>();
		findPath();
		LCD.drawString("path cal", 0, 0);
		pathList.add(pathFinder.findRoute(new Pose(esbjerg.getPose().getX(),
				esbjerg.getPose().getY(),-90) , start));
		pathList.add(pathFinder.findRoute(start.getPose() , esbjerg));
		
		LCD.drawString("path completing done", 0, 0);
		/*pathList.add(pathFinder.findRoute(start.getPose() , aarhus));
		pathList.add(pathFinder.findRoute(aarhus.getPose() , start));
		
		pathList.add(pathFinder.findRoute(start.getPose() , kolding));
		pathList.add(pathFinder.findRoute(kolding.getPose() , start));
		
		pathList.add(pathFinder.findRoute(start.getPose() , odense));
		pathList.add(pathFinder.findRoute(odense.getPose() , start));
		
		pathList.add(pathFinder.findRoute(start.getPose() , soenderborg));
		pathList.add(pathFinder.findRoute(soenderborg.getPose() , start));
		navi.setPath(pathFinder.findRoute(start.getPose() , esbjerg));*/
		while (!done)
		{
			LCD.drawString("while not done", 0, 0);
			if(navi.pathCompleted() ||navi.getPath().isEmpty())
			{
				LCD.drawString("new path", 0, 0);
				if(!pathList.isEmpty())
				{
				navi.setPath(pathList.poll());
				currentWaypoint = navi.getPath().get(navi.getPath().size()-1);
				navi.followPath();
				Sound.beep();
				}
				else 
				{
					LCD.drawString("pathlist empty", 0, 0);
					done = true;
				}	
			
			}
			/*LCD.drawString("Following path", 0, 0);
			if(navi.getWaypoint()!=currentWaypoint)
			{
				currentWaypoint = navi.getWaypoint();
				Sound.beep();
			}*/
			try
			{
			LCD.drawString("( "+currentWaypoint.getX()+" , "
					+currentWaypoint.getY()+" )", 0, 2);
			LCD.drawString("( "+(int)navi.getPoseProvider().getPose().getX()+" , "
					+(int)navi.getPoseProvider().getPose().getY()+" )", 0, 3);
			}
			catch(Exception e)
			{
				
			}
		}
		} catch (DestinationUnreachableException e) {
			LCD.drawString("something wrong", 0, 0);
		} catch (FileNotFoundException e1) {
			LCD.drawString("file not found", 0, 0);
		} catch (XMLStreamException e1) {
			LCD.drawString("something really wrong", 0, 0);
		}
		
	}
	void findPath () throws FileNotFoundException, XMLStreamException 
	{
		
		LineMap map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
		map.flip();
		pathFinder = new ShortestPathFinder(map);
		pathFinder.lengthenLines(10);

	}
}
