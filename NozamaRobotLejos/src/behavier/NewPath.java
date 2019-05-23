package behavier;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class NewPath implements Behavior{
	boolean suppress = false;
	DataInputStream dis;
	DataOutputStream dos;
	Navigator _navi;
	boolean home = true;
	boolean go = false;
	Path path = new Path();
	public NewPath(DataInputStream dis, DataOutputStream dos, Navigator navi, boolean go) {
		// TODO Auto-generated constructor stub
		this.dis = dis;
		this.dos = dos;
		_navi = navi;
		this.go = go;
	}

	@Override
	public boolean takeControl() {
		
		// TODO Auto-generated method stub
		return _navi.getPath().isEmpty();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		suppress = false;
		path.clear();
		if(home) {
		try {
			dos.writeUTF("start");
			dos.flush();
			home = false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}}
		else {
				try {
					dos.writeUTF("return " + _navi.getPoseProvider().getPose().getX() + " "
							+ _navi.getPoseProvider().getPose().getY());
					dos.flush();
					home = true;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
		boolean first = true;
	while(!suppress)
	{
		
		try {
			String message = dis.readUTF();
			String[] messageSplit = message.split(" ");
			if(!messageSplit[0].equals("done"))
			{	
				if(first)
				{
					first= false;
				}
				else {
				path.add(new Waypoint(Double.parseDouble(messageSplit[0]),
						Double.parseDouble(messageSplit[1])));
			System.out.println(messageSplit[0]+" "+ messageSplit[1]);
				}
			}
			else {
				System.out.println("set new path");
				_navi.setPath(path);
				System.out.println("go = true");
				go = true;
				suppress = true;
			}
			
		} catch (IOException e) {
			
		}
		
	}
		
	}

	@Override
	public void suppress() {
		suppress = true;
		// TODO Auto-generated method stub
		
	}

}
