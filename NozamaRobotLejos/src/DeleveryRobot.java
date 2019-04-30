import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLStreamException;

import behavier.DectectObject;
import behavier.StopEscapeButton;
import behavier.findpath;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.ColorAdapter;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.SVGMapLoader;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class DeleveryRobot {
	ColorAdapter colorAdapter;
	Wheel backMotor = WheeledChassis.modelWheel(Motor.D, 30).offset(0);
	Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-55.80);
	Wheel rigthWheel = WheeledChassis.modelWheel(Motor.B, 56).offset(55.80);
	Arbitrator arb;
	MovePilot pilot;
	Waypoint esbjerg = new Waypoint(440,2200);
	Waypoint aarhus = new Waypoint(1300,3600);
	Waypoint kolding = new Waypoint(2300,2200);
	Waypoint odense = new Waypoint(3600,2200);
	Waypoint soenderborg = new Waypoint(2500,400);
	String filePath = "denmark.svg";
	ShortestPathFinder pathFinder;
	LineMap map;
	EV3 brick = (EV3) BrickFinder.getDefault();
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub

		DeleveryRobot drobot= new DeleveryRobot();
		drobot.initMap();
		drobot.startAbitrater();
	}
	private void initMap() {
       try {
		map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
        pathFinder = new ShortestPathFinder(map);
		
		pathFinder.lengthenLines(100);
	} catch (FileNotFoundException | XMLStreamException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		//map.flip();
		
		
		
	}
	public void startAbitrater()
	{
		    try(EV3ColorSensor ev3ColoerSensor = new EV3ColorSensor(brick.getPort("S3"));
		    		)
		    {
		    	backMotor.getMotor().setAcceleration(100);
		    	backMotor.getMotor().setSpeed(100);
		    colorAdapter = new ColorAdapter(ev3ColoerSensor);
		    
			Waypoint distiantion = esbjerg;
		    Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		    pilot = new MovePilot(chassis);
		    Navigator navi = new Navigator(pilot);
			Behavior stopEscapeButton = new StopEscapeButton();
			Behavior findPath = new findpath(distiantion,pathFinder,navi,backMotor);
			Behavior detectObject = new DectectObject(colorAdapter,navi);
			Behavior[] behaiverArray = {findPath,detectObject,stopEscapeButton};
	        arb = new Arbitrator(behaiverArray);
	        arb.go();
		    }
	}
}
