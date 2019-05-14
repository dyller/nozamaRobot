import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLStreamException;
import behavier.StopEscapeButton;
import behavier.UltraSonic;
import behavier.findpath;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RangeFinderAdapter;
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
import lejos.utility.Delay;

public class DeleveryRobot {

	Wheel backMotor = WheeledChassis.modelWheel(Motor.D, 30).offset(0);
	Wheel sonicMotor = WheeledChassis.modelWheel(Motor.A, 30).offset(0);
	Wheel rightWheel = WheeledChassis.modelWheel(Motor.C, 55.130).offset(55.7);//55,8//65//56
	Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 55.130)/*55.5,55.130*/.offset(-55.9);//55.7,
	Arbitrator arb;
	MovePilot pilot;
	Queue<Path> pathList;
	Waypoint esbjerg = new Waypoint(440, 2200);
	Waypoint aarhus = new Waypoint(1300, 3600);
	Waypoint kolding = new Waypoint(2300, 2200);
	Waypoint odense = new Waypoint(3600, 2200);
	Waypoint soenderborg = new Waypoint(2500, 400);
	String filePath = "denmark.svg";
	ShortestPathFinder pathFinder;
	int speed_130 = 130;
	int speed_90 = 90;
	int acc_85 = 85;
	int acc_50 = 50;
	boolean go;
	LineMap map;
	public EV3 brick = (EV3) BrickFinder.getDefault();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DeleveryRobot drobot = new DeleveryRobot();
		drobot.initMap();
		drobot.startAbitrater();
	}

	private void initMap() {
		try {
			map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
			pathFinder = new ShortestPathFinder(map);
			map.flip();
			pathFinder.lengthenLines(80);
			
		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startAbitrater() {
		pathList = new LinkedList<Path>();
		backMotor.getMotor().setAcceleration(acc_85);
		backMotor.getMotor().setSpeed(speed_130);
		sonicMotor.getMotor().setAcceleration(acc_50);
		sonicMotor.getMotor().setSpeed(50);
		try (EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(brick.getPort("S4"))) {
			RangeFinderAdapter ultrasonicAdapter = new RangeFinderAdapter(ultrasonicSensor);

			Waypoint distiantion = esbjerg;
			Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel },
					WheeledChassis.TYPE_DIFFERENTIAL);
			pilot = new MovePilot(chassis);
			pilot.setLinearAcceleration(acc_85);
			pilot.setLinearSpeed(speed_130);
			pilot.setAngularAcceleration(acc_50);
			pilot.setAngularSpeed(speed_90);
			Navigator navi = new Navigator(pilot);
			Behavior stopEscapeButton = new StopEscapeButton();
			Behavior findPath = new findpath(pathList, distiantion, pathFinder, navi, backMotor, brick,go);
			Behavior Ultrasonic = new UltraSonic(ultrasonicAdapter, sonicMotor, navi, pathFinder,go );
			Behavior[] behaiverArray = { findPath, Ultrasonic, stopEscapeButton };
			arb = new Arbitrator(behaiverArray);
			arb.go();
		}
	}
}
