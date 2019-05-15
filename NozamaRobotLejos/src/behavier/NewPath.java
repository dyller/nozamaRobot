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

public class NewPath implements Behavior{
	boolean suppress = false;
	DataInputStream dis;
	DataOutputStream dos;
	Navigator _navi;
	Path path = new Path();
	public NewPath(DataInputStream dis, DataOutputStream dos, Navigator navi) {
		// TODO Auto-generated constructor stub
		this.dis = dis;
		this.dos = dos;
		_navi = navi;
	}

	@Override
	public boolean takeControl() {
		
		// TODO Auto-generated method stub
		return _navi.getPath().isEmpty();
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		path.clear();
		try {
			dos.writeUTF("start");
			dos.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	while(!suppress)
	{
		
		try {
			String message = dis.readUTF();
			String[] messageSplit = message.split(" ");
			if(!messageSplit[0].equals("done"))
			{	
				if(path.isEmpty())
				{
					_navi.getPoseProvider().setPose(
							new Pose(Float.parseFloat(messageSplit[0]),
									Float.parseFloat(messageSplit[1]),
									Float.parseFloat(messageSplit[2])));
				}
				path.add(new Waypoint(Double.parseDouble(messageSplit[0]),
						Double.parseDouble(messageSplit[1])));
			
			}
			else {
				System.out.println("set new path");
				_navi.setPath(path);
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
