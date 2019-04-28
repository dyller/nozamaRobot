import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
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

public class ClientMapConstructor {
	int speed = 100;
	int acceration = 100;
	int portNumber = 5000;
	 Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-53.2);
	 Wheel rigthWheel = WheeledChassis.modelWheel(Motor.D, 56).offset(53.2);
	 RangeFinderAdapter ultrasonicSensorAdaptor; 
	 MovePilot pilot;
	 boolean newComand = false;
	 Navigator navi;
	 DataInputStream dis;
	 DataOutputStream dos;
	 EV3 brick = (EV3) BrickFinder.getDefault();
	 float rangeToObject;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerNavi serverRobot = new ServerNavi();
		serverRobot.setup();

	}
	
	public void setup()
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
			LCD.drawString("Connected", 0, 1);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			
			boolean done = false;
			while(!done)
			{
				ultrasonicMapConstruction();
				
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
				case "forward": start();
					break;
				case "stop": stop();
				break;
					}
				
				
			    }
					
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void ultrasonicMapConstruction() {
		// TODO Auto-generated method stub
	
		 rangeToObject =  ultrasonicSensorAdaptor.getRange();
		if(rangeToObject <= 25)
		{
			try {
				dos.writeUTF(rangeToObject+"");
				dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
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
