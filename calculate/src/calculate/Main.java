package calculate;

import java.awt.Canvas;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;

import javax.xml.stream.XMLStreamException;

import lejos.robotics.geometry.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.SVGMapLoader;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.utility.Delay;

public class Main {
	Pose currentPosePotion;
	Waypoint destionation;
	Path shortestPath;
	LineMap map;
    ShortestPathFinder pathFinder;
	String filePath = "C:\\Users\\jacob\\Desktop\\exam\\nozamaRobot\\denmark.svg";
	//waypoints
	Waypoint esbjerg = new Waypoint(440, 2200);
	Waypoint aarhus = new Waypoint(1300, 3600);
	Waypoint kolding = new Waypoint(2300, 2200);
	Waypoint odense = new Waypoint(3600, 2200);
	Waypoint soenderborg = new Waypoint(2500, 400);
	Waypoint start = new Waypoint(1000, 1100);
	
	int portNumber = 5000;
	String ipAdress = "192.168.137.114";
	DataInputStream dis;
	DataOutputStream dos;
	public static void main(String[] args) {
		Main main = new Main();
		//main.calculatePath();
		main.setupCommunication();
		
		// TODO Auto-generated method stub
	///ShortestPathFinder pathFinder;
		
	
	}
	void setupCommunication()
	{
		Socket s;
	try {
		s = new Socket(ipAdress , portNumber);
		
		
		System.out.println("Setup socket");
		
		 dis = new DataInputStream(s.getInputStream());
		 dos = new DataOutputStream(s.getOutputStream());
		 while(true)
		 {
		 String readMessage = dis.readUTF();
		 String[] message = readMessage.split(" ");
		 switch(message[0])
		 {
		 case "newLine":
			 addNewLine(Integer.parseInt(message[1]),Integer.parseInt(message[2]),
					 Integer.parseInt(message[3]), Integer.parseInt(message[4]));
			 calculatePath();
			for (Waypoint waypoint : shortestPath) {
				dos.writeUTF(waypoint.getX() + " " +waypoint.getY() +" " +waypoint.getHeading() );
				dos.flush();
			} 
			dos.writeUTF("done");
			dos.flush();
			 break;
		 case "start":
			currentPosePotion = start.getPose();
			/*destionation = new Waypoint(Double.parseDouble(message[1]),
					Double.parseDouble(message[2]),
					Double.parseDouble(message[3]));*/
			destionation = esbjerg;
			 calculatePath();
			 for (Waypoint waypoint : shortestPath) {
				 
					dos.writeUTF(waypoint.getX() + " " +waypoint.getY() +" " +waypoint.getHeading() );
					dos.flush();
					Delay.msDelay(100);
				} 
				dos.writeUTF("done");
				dos.flush();
			 break;
		 }
		 }
	}
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	void calculatePath()
	{
		try {
			map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
			pathFinder = new ShortestPathFinder(map);
			//map.flip();
			pathFinder.lengthenLines(80);
			
		} catch (FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long now = System.currentTimeMillis();
	try {
		shortestPath=pathFinder.findRoute(currentPosePotion, destionation);
		System.out.println("time it take: " + (System.currentTimeMillis()-now));
	} catch (DestinationUnreachableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	void addNewLine(int x, int y, int xEnd, int yEnd)
	{
		pathFinder.setMap(map);
		pathFinder.getMap().add(new Line(
			x,
			y,
			xEnd,
			yEnd));
	}

}
