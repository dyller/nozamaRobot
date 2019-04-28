package MapConstuctor;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
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
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class ClientMapConstructor {
	int speed = 100;
	int acceration = 50;
	int portNumber = 5000;
	 Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-53.2);
	 Wheel rigthWheel = WheeledChassis.modelWheel(Motor.D, 56).offset(53.2);
	 Wheel sonicWheel = WheeledChassis.modelWheel(Motor.B, 30).offset(0);
	 RangeFinderAdapter ultrasonicSensorAdaptor; 
	 MovePilot pilot;
	 boolean newComand = false;
	 Navigator navi;
	 DataInputStream dis;
	 DataOutputStream dos;
	 EV3 brick = (EV3) BrickFinder.getDefault();
	 float rangeToObject;
	 Arbitrator arb;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientMapConstructor mapRobot = new ClientMapConstructor();
		try {
			mapRobot.setup();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setup() throws SocketException
	{
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		 pilot = new MovePilot(chassis);
		 pilot.setLinearSpeed(speed);
		 pilot.setAngularSpeed(speed);
		 navi = new Navigator(pilot);
		
		try(EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(brick.getPort("S4"))) {
			ServerSocket server = new ServerSocket(portNumber);
			ultrasonicSensorAdaptor = new RangeFinderAdapter(ultrasonicSensor);
			
			LCD.drawString("connecting", 0, 0);
			Sound.beep();
			Socket s = server.accept();
			LCD.drawString("Connected", 0, 2);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			
			boolean done = false;
			while(!done)
			{
				
				
				String message = dis.readUTF();
				newComand = true;
				/*dos.writeUTF(message);
				dos.flush();
				if(message.toLowerCase().equals("quit"))
				{
					System.exit(0);
				}
				while (message.equals("forward"))*/
				
				switch(message.toLowerCase())
				{
				case "forward": startMapCreater();
					break;
				case "stop": stop();
				break;
					}
				
				
			    }
					
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}



	private void startMapCreater() {
		// TODO Auto-generated method stub
		Navigator navi = new Navigator(pilot);
		navi.addWaypoint(0,0);
		navi.addWaypoint(0,5000);
		Behavior forward = new Forward(navi);
		Behavior ultrasonic  = new UltrasonicSensor(ultrasonicSensorAdaptor,pilot, sonicWheel,dis,dos,navi);
		Behavior stopEverything = new StopRobot(Button.ESCAPE);
		Behavior[] behaiverArray = {forward, ultrasonic, stopEverything};
		arb = new Arbitrator(behaiverArray);
		
	    arb.go();
		
	}

	private void start() {
		//LineMap map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
		while(!navi.pathCompleted())
		{
			Thread.yield();
			
		}
		// TODO Auto-generated method stub
		
	}

	public void stop () {
		navi.stop();
		System.exit(0);
		
		}
}
