package MapConstuctor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


import org.jfree.data.time.MovingAverage;
import org.opencv.core.Mat;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.geometry.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.LUDecomposition;

public class UltrasonicSensor implements Behavior {
	static MovePilot pilot;
	private static boolean suppressed;
	private RangeFinderAdapter ultrasonicAdapter;
	private float distanceone;
	private float distancetwo;
	private Wheel sonicmotor;
	private int  range = 25;
	private int travel = 250;
	private DataInputStream dis;
	private DataOutputStream dos;
	ShortestPathFinder pathFinder;
	LineMap map  = new LineMap(null, null);
	ForwardNDraw FND;
	private int sideOfSonicMotor = -1;
	
	Navigator navi;
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return ultrasonicAdapter.getRange()<range;
	}

	public UltrasonicSensor(RangeFinderAdapter rangefinderAdaptor, MovePilot pilot, Wheel motor,
			DataInputStream dis, DataOutputStream dos,Navigator navi) {
		this.ultrasonicAdapter = rangefinderAdaptor;
		this.pilot= pilot;
		this.sonicmotor = motor;
		this.navi = navi;
		this.dos = dos;
		
	}

	@Override
	public void action() {
		suppressed = false;
		sonicmotor.getMotor().rotate(45);
		distanceone = ultrasonicAdapter.getRange();
		sonicmotor.getMotor().rotate(-90);
		distancetwo = ultrasonicAdapter.getRange();
		if(distanceone <= 25 && distancetwo <= 25) {
			Sound.buzz();
		}else {
			if(distanceone >= distancetwo) {
				sonicmotor.getMotor().rotate(25);
				sideOfSonicMotor = 0;
			pilot.rotate(90);
		}else if(distanceone < distancetwo){
			sonicmotor.getMotor().rotate(65);
			sideOfSonicMotor = 1;
			pilot.rotate(-90);
		}
		}
		//FND = new ForwardNDraw(ultrasonicAdapter, pilot, range);
		
		
		//sonicmotor.getMotor().rotate(45);
		/* create new line 
		 *  
		 * float x = navi.getPoseProvider().getPose().getX();
		float y = navi.getPoseProvider().getPose().getY();
		float heading = navi.getPoseProvider().getPose().getHeading();
		Line newLine = new Line(
				(float) (Math.sin(heading)*x-30),(float)( Math.cos(heading)*x-30)
				,(float)( Math.sin(heading)*x+30), (float)(Math.cos(heading)*x+30));
		
		Line [] lines = map.getLines();
		if(lines == null)
		{
			lines = new Line[] {newLine};
		}
		else {
		lines[lines.length] = newLine;}
		map= new LineMap(lines, null);
		pathFinder = new ShortestPathFinder(map);
		pathFinder.lengthenLines(1);
		try {
			navi.setPath(pathFinder.findRoute(navi.getPoseProvider().getPose(), new Waypoint(0,5000)));
		} catch (DestinationUnreachableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/*Path path = navi.getPath();
		Path newPath= new Path();
		newPath.add(new Waypoint(navi.getPoseProvider().getPose().getX(),
				navi.getPoseProvider().getPose().getY()+100));
		newPath.addAll(path);
		navi.setPath(newPath);*/
	//	pilot.forward();
		while(!suppressed && pilot.isMoving())
		{
			LCD.drawString("Ulrasonicsensor", 0, 5);
			
			LCD.drawString(ultrasonicAdapter.getRange()+"", 0, 6);
				
			/*try {
				 x = navi.getPoseProvider().getPose().getX();
				 y = navi.getPoseProvider().getPose().getY();
				double wayX = navi.getWaypoint().getX();
				double wayY = navi.getWaypoint().getY();
				dos.writeUTF(x+" "
						+y+" "
						+ wayX+" "
						+ wayY);
				dos.flush();
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			navi.followPath();*/
			Thread.yield();
		}
		pilot.stop();
		/*
		while(!suppressed && pilot.isMoving())
		Thread.yield();*/
		
		if(sideOfSonicMotor == 0) {
			sonicmotor.getMotor().rotate(30);
		}else {
			sonicmotor.getMotor().rotate(-30);
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		suppressed = true;
		// TODO Auto-generated method stub
		
	}

}
