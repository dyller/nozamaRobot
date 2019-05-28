package behavier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.geometry.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class UltraSonic implements Behavior {

	RangeFinderAdapter _ultrasonicAdapter;
	Wheel _sonicMotor;
	Navigator _navi;
	int _heading;
	DataInputStream dis;
	DataOutputStream dos;
	boolean supressed;
	private int minDistance = 10;
	Pose _naviPose;
	boolean objectRight = false;
	boolean objectLeft = false;
	boolean done = true;
	Path path = new Path();
	public UltraSonic(RangeFinderAdapter ultrasonicAdapter, Wheel sonicMotor,
			Navigator navi, DataInputStream dis, DataOutputStream dos) {
		// TODO Auto-generated constructor stub
		this._ultrasonicAdapter = ultrasonicAdapter;
		this._sonicMotor = sonicMotor;
		this._navi = navi;
		this.dis = dis;
		this.dos = dos;
		//this._go = go;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		//return false;
		return _ultrasonicAdapter.getRange() < minDistance ||!done;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		Delay.msDelay(20);
		done = false;

		_navi.stop();
		_navi.clearPath();
		supressed = false;
		System.out.println("action");
		LCD.clear();
		LCD.drawString("Obstacle detected", 0, 5);
		_sonicMotor.getMotor().rotate(15);
		objectRight = _ultrasonicAdapter.getRange() <= 15;
		Delay.msDelay(1000);
		_sonicMotor.getMotor().rotate(-30);
		objectLeft = _ultrasonicAdapter.getRange() <= 15;
		_sonicMotor.getMotor().rotate(15);
		_naviPose = _navi.getPoseProvider().getPose();
		checkHeading();
		printLine();
	}

	private void printLine() {
		done = false;
		boolean first = true;
		System.out.println("printline");
		if (objectRight && objectLeft) {
			System.out.println("heading: " + _heading);
			LCD.clear();
			LCD.drawString("Huge obstacle detected", 0, 5);
			postion();
			switch(_heading) {
			case 0:
				try {
					dos.writeUTF("newLine "
							+(int)(_naviPose.getX()+10) +" " 
							+(int)(_naviPose.getY()+350) +" "
							+(int)(_naviPose.getX()+10) +" "
							+((int)_naviPose.getY()-350));
					dos.flush();

					Delay.msDelay(100);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 1:
				try {
					dos.writeUTF("newLine "
							+(int)(_naviPose.getX()+350) +" " 
							+(int)(_naviPose.getY()+10) +" "
							+(int)(_naviPose.getX()-350) +" "
							+(int)(_naviPose.getY()+10));
					dos.flush();

					Delay.msDelay(100);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 2:
				try {
					dos.writeUTF("newLine "
							+(int)(_naviPose.getX()-10) +" " 
							+(int)(_naviPose.getY()+350) +" "
							+(int)(_naviPose.getX()-10) +" "
							+(int)(_naviPose.getY()-350));
					dos.flush();

					Delay.msDelay(100);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 3:
				try {
					dos.writeUTF("newLine "
							+(int)(_naviPose.getX()+350) +" " 
							+(int)(_naviPose.getY()-10) +" "
							+(int)(_naviPose.getX()-350) +" "
							+(int)(_naviPose.getY()-10));
					dos.flush();

					Delay.msDelay(100);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			}
			LCD.clear();
			LCD.drawString("Line added", 0, 5);
			while(!supressed) {
			try {
				String message = dis.readUTF();
				String[] messageSplit = message.split(" ");
				if(!messageSplit[0].equals("done"))
				{	
					if(first)
					{
						first= false;
					}
					System.out.println("set new path:" +Double.parseDouble(messageSplit[0] )+":"+
							Double.parseDouble(messageSplit[1]));
					path.add(new Waypoint(Double.parseDouble(messageSplit[0]),
							Double.parseDouble(messageSplit[1])));
				
				}
				else {
					_navi.setPath(path);
					done = true;
					supressed = true;
				}
				
			} catch (IOException e) {
				
			}
		} }else {
			if (objectRight) {
				LCD.clear();
				LCD.drawString("Right obstacle detected", 0, 5);
			} else if (objectLeft) {
				LCD.clear();
				LCD.drawString("Left obstacle detected", 0, 5);
			}
		}
		done = true;
	}

	private void checkHeading() {
		// TODO Auto-generated method stub
		int angleOfHeading = (int) _naviPose.getHeading();
		System.out.println("angle: " + _naviPose.getHeading());
		switch(((angleOfHeading+45)%360)/90) {
			case 0://315 too 44
				_heading = 0;
			break;
			case 1: //45 to 134
				_heading = 1;
			break;
			case 2: //135 to 224
				_heading = 2;
			break;
			case 3: //225 to 314
				_heading = 3;
			break;
		}
	}
	public void postion()
	{
		try {
			dos.writeUTF("postion "
					+(int)(_navi.getPoseProvider().getPose().getX()) +" " 
					+(int)(_navi.getPoseProvider().getPose().getY()));
			dos.flush();

			Delay.msDelay(100);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		supressed = true;
	}

}
