package calculate;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;

import javax.swing.JFrame;
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

public class Main extends JFrame {
	Pose currentPosePotion;
	Canvas canvas;
	int canvasX = 400;
	int canvasy = 400;
	Waypoint destionation;
	Path shortestPath;
	LineMap map;
    ShortestPathFinder pathFinder;
	String filePath = "C:\\Users\\jacob\\Desktop\\exam\\nozamaRobot\\denmark.svg";
	//waypoints
	String[] cities = {"Esbjerg",
	                    "Aarhus",
	                    "Kolding",
	                    "Odense",
	                    "Sonderborg"};
	
	Waypoint esbjerg = new Waypoint(440, 2200);
	Waypoint aarhus = new Waypoint(1300, 3600);
	Waypoint kolding = new Waypoint(2300, 2200);
	Waypoint odense = new Waypoint(3600, 2200);
	Waypoint soenderborg = new Waypoint(2500, 400);
	Waypoint start = new Waypoint(1000, 1100);
	
	int portNumber = 5000;
	String ipAdress = "192.168.137.253";
	DataInputStream dis;
	DataOutputStream dos;
	public static void main(String[] args) {
		Main main = new Main();
		
		//main.calculatePath();
		main.setupCommunication();
		
		// TODO Auto-generated method stub
	///ShortestPathFinder pathFinder;
		
	
	}
	Main() 
    { 
		
        super("canvas"); 
        // create a empty canvas 
        /*JPanel panel = new JPanel();
        getContentPane().add(panel);*/
        
         canvas = new Canvas() { 
        	 
            // paint the canvas 
            public void paint(Graphics g) 
            { 
            	
  
            } 
        }; 
  
        // set background 
        canvas.setBackground(Color.black); 
  
        add(canvas); 
        setSize(canvasX, canvasy); 
        
        show(); 
    }
	
	void setupCommunication()
	{
		String[] parts;
		String[] preDistanations;
	/*try {
		URL urlOrders = new URL("https://us-central1-nozama-58c5d.cloudfunctions.net/orders");
		HttpURLConnection con;
		con = (HttpURLConnection) urlOrders.openConnection();
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
				    content.append(inputLine);
				}
				in.close();
				if(inputLine!=null||inputLine.contains("userAddress"))
				{
				    parts = inputLine.split(",");
					for(String attributes: parts)
					{
						if(attributes.contains("userAddress"))
						{
							preDistanations = attributes.split(":");
							for(String address: preDistanations)
							{*/
								for(String city: cities)
								{
									String test = "\"Esbjerg\"";
									if(test.contains(city))
									{
										switch(city)
										 {
										case "Esbjerg": destionation = esbjerg;
										break;
										
										case "Aarhus": destionation = aarhus;
										break;
										
										case "Kolding": destionation = kolding;
										break;
										
										case "Odense": destionation = odense;
										break;
										
										case "Sonderborg": destionation = soenderborg;
										break;
										 }
									
										System.out.println("distanation: "+destionation.getX()+" : "+destionation.getY()); 
									}
								}
							/*}
						
						}
					}
				}
	} catch ( e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}*/
								try {
									map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();
								} catch (FileNotFoundException | XMLStreamException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
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
			 
			 System.out.println("newLine"+Integer.parseInt(message[1])+":"+Integer.parseInt(message[2])+":"+
					 Integer.parseInt(message[3])+":"+ Integer.parseInt(message[4]));
			 
			 calculatePath();
			for (Waypoint waypoint : shortestPath) {
				 System.out.println(waypoint.getX()+" "+ waypoint.getY());
				dos.writeUTF(waypoint.getX() + " " +waypoint.getY() +" " +waypoint.getHeading() );
				dos.flush();
				Delay.msDelay(100);
			} 
			dos.writeUTF("done");
			dos.flush();
			 break;
		 case "start":
			 pathFinder = new ShortestPathFinder(map);
			currentPosePotion = start.getPose();
			/*destionation = new Waypoint(Double.parseDouble(message[1]),
					Double.parseDouble(message[2]),
					Double.parseDouble(message[3]));*/
			//destionation = esbjerg;
			System.out.println("calc Line");
			 calculatePath();
			 for (Waypoint waypoint : shortestPath) {
				 
					dos.writeUTF(waypoint.getX() + " " +waypoint.getY() +" " +waypoint.getHeading() );
					dos.flush();
					Delay.msDelay(100);
				} 
				dos.writeUTF("done");
				dos.flush();
			 break;
		 case "return":
			 pathFinder = new ShortestPathFinder(map);
				currentPosePotion = new Waypoint(Float.parseFloat(message[1]),
						Float.parseFloat(message[2])).getPose();
				destionation = start;
				System.out.println("calc home"+ Float.parseFloat(message[1]) +" "
						+Float.parseFloat(message[2]));
				 calculatePath();
				 for (Waypoint waypoint : shortestPath) {
					 System.out.println(waypoint.getX()+" "+ waypoint.getY());
						dos.writeUTF(waypoint.getX() + " " +waypoint.getY() +" " +waypoint.getHeading() );
						dos.flush();
						Delay.msDelay(100);
					} 
					dos.writeUTF("done");
					dos.flush();
				 break;
		 case "postion":
			 System.out.println("postion: " + Integer.parseInt(message[1])+" "+ Integer.parseInt(message[2]));
				currentPosePotion = new Waypoint(Integer.parseInt(message[1]),
						Integer.parseInt(message[2])).getPose();
				
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
		
			//map.flip();
			pathFinder.lengthenLines(80);
		
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
		Graphics g = canvas.getGraphics();
		g.setColor(Color.RED);
		for (Line line : pathFinder.getMap()) 
		{ 
			g.drawLine((int)(line.x1/10), (int)(line.y1/10), (int)(line.x2/10), (int)(line.y2/10));
		}
		
		canvas.paint(g); 
	     
	   
		 
		
	}

}
