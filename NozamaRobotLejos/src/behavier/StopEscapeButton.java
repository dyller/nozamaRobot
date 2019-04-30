package behavier;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.robotics.subsumption.Behavior;

public class StopEscapeButton implements Behavior {
	public boolean clicked = false;

	public StopEscapeButton () {
		//this.clicked = escape;
		Button.ESCAPE.addKeyListener(new lejos.hardware.KeyListener() {

		
			@Override
			public void keyPressed(Key k) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(Key k) {
				// TODO Auto-generated method stub
				clicked =true;
			}
		});
	}
	private boolean suppressed = false; 
	
	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return clicked;
	}

	@Override
	public void action() {
		suppressed = false;
		// TODO Auto-generated method stub
	  System.exit(0);
	}
	

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		suppressed = true;
	}

}
