package behavier;

import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.ColorAdapter;

public class DectectObject {
	ColorAdapter _colorAdaptor;

	public boolean checkIfObject(EV3 brick) {
		try (EV3ColorSensor ev3ColoerSensor = new EV3ColorSensor(brick.getPort("S3"));) {
			_colorAdaptor = new ColorAdapter(ev3ColoerSensor);
			if (_colorAdaptor.getColorID() == Color.NONE) {
				LCD.clear();
				LCD.drawString("Object detected", 0, 4);
				return true;
			} else {
				LCD.clear();
				LCD.drawString("Object not detected", 0, 4);
				return false;
			}
		}
	}
}
