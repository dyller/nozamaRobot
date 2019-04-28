package recalli;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class recallibrate {
	static int acceleration = 50;
	static Wheel leftWheel = WheeledChassis.modelWheel(Motor.C, 55.130).offset(-55.80);
	static Wheel rigthWheel = WheeledChassis.modelWheel(Motor.B, 55.5).offset(55.80);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rigthWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(chassis);
		pilot.setAngularAcceleration(acceleration);
		pilot.setLinearAcceleration(acceleration);
		pilot.rotate(-90);
		pilot.rotate(90);

	}

}
