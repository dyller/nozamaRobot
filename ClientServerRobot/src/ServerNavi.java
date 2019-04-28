import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLStreamException;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.SVGMapLoader;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.utility.Delay;

public class ServerNavi {
	static String filePath = "denmark.svg";
	int speed = 222;
	int acceration = 169;
	int portNumber = 5000;
	 Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 56).offset(-55.80);
	 Wheel rigthWheel = WheeledChassis.modelWheel(Motor.B, 56).offset(55.80);
	 MovePilot pilot;
	 Waypoint start = new Waypoint(1000,1100);
		Waypoint esbjerg = new Waypoint(440,2200);
		Waypoint aarhus = new Waypoint(1300,3600);
		Waypoint kolding = new Waypoint(2300,2200);
		Waypoint odense = new Waypoint(3600,2200);
		Waypoint soenderborg = new Waypoint(2500,400);
	 boolean newComand = false;
	 Navigator navi;
	 DataInputStream dis;
	 DataOutputStream dos;
	 ShortestPathFinder pathFinder;
	 LineMap map;
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerNavi serverRobot = new ServerNavi();
		try {
			serverRobot.findPath();
		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverRobot.setup();

	}
	void findPath () throws FileNotFoundException, XMLStreamException 
	{
		
		map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
		
		//map.flip();
		
		pathFinder = new ShortestPathFinder(map);
		pathFinder.lengthenLines(10);

	}
	
	
	public void setup()
	{
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		 pilot = new MovePilot(chassis);
		 pilot.setLinearSpeed(speed);
		 pilot.setAngularSpeed(speed);
		 navi = new Navigator(pilot);
		
		try {
			ServerSocket server = new ServerSocket(portNumber);
		
			
			LCD.drawString("connecting", 0, 0);
			Sound.beep();
			Socket s = server.accept();
			LCD.drawString("Connected", 0, 1);
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
				case "forward": try {
						start();
					} catch (XMLStreamException | DestinationUnreachableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "stop": stop();
				break;
					}
				
				
			    }
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	private void start() throws FileNotFoundException, XMLStreamException, DestinationUnreachableException {
		boolean done = false;
			Queue<Path> pathList = new LinkedList<Path>();
			findPath();
			LCD.drawString("path cal", 0, 0);
			pathList.add(pathFinder.findRoute(esbjerg.getPose() , aarhus));
			pathList.add(pathFinder.findRoute(aarhus.getPose() , start));
			navi.getPoseProvider().setPose(esbjerg.getPose());
		while(!done)
		{
			Thread.yield();
			LCD.drawString("while not done", 0, 0);
			if(navi.pathCompleted() ||navi.getPath().isEmpty())
			{
				LCD.drawString("new path", 0, 0);
				if(!pathList.isEmpty())
				{
				
				navi.setPath(pathList.poll());
				navi.followPath();
				Sound.beep();
				}
				else 
				{
					LCD.drawString("pathlist empty", 0, 0);
					done = true;
				}	
			
			}
			
			try {
				float x = navi.getPoseProvider().getPose().getX();
				float y = navi.getPoseProvider().getPose().getY();
				LCD.drawString(x+"", 0, 3);
				LCD.drawString(y+"", 0, 4);
				dos.writeUTF(x+" "+y);
			    dos.flush();
			} catch ( NullPointerException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	
		// TODO Auto-generated method stub
		
	
	}


	public void stop () {
		int count = 0; 
		
		for(Line line :map.getLines())
		{
			
				 
			
			
			
			LCD.drawString("count:"+count, 5, 5);
			try {
				dos.writeUTF(line.x1+" "
						+line.y1+" "
						+line.x2+" "
						+line.y2+ " "+
						+count
);
				 dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   count++;
		}
		try {
			dos.writeUTF("done");
			 dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
}
