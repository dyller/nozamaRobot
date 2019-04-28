import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import Main.MKeyListener;

public class canvas extends JFrame {
	static String filePath = "denmark.svg";
	int portNumber = 5000;
	String ipAdress = "192.168.137.15";
	boolean done = false;
	DataInputStream dis;
	DataOutputStream dos;
	Canvas canvas;
	int canvasX = 400;
	int canvasy = 400;
	
	
	 // constuctor 
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
            	
       		
            		// TODO Auto-generated method stub
            /*	g.setColor(Color.BLUE);
            	 g.drawLine(100, 10, 160, 10);
            		 g.drawLine(100, 70, 100, 10);
            		 g.drawLine(160, 70, 160, 10);
            		 g.drawLine(110, 70, 100, 70);
            		 g.drawLine(160, 70, 150, 70);
            		 g.drawLine(74, 150, 14, 150);
            		 g.drawLine(14, 210, 14, 150);
            		 
            		 g.drawLine(74, 160, 74, 150);
            		 g.drawLine(74, 210, 14, 210);
            		 g.drawLine(74, 200, 74, 210);
            		 g.drawLine(130, 260, 70, 260);
            		 g.drawLine(70, 320, 70, 260);
            		 g.drawLine(130, 320, 70, 320);
            		 g.drawLine(130, 270, 130, 260);
            		 g.drawLine(130, 310, 130, 320);
            		 g.drawLine(260, 150, 200, 150);
            		 g.drawLine(200, 150, 200, 160);
            		 g.drawLine(200, 210, 200, 200);
            		 g.drawLine(260, 210, 200, 210);
            		 g.drawLine(260, 200, 260, 210);
            		 g.drawLine(260, 160, 260, 150);
            		 g.drawLine(280, 330, 220, 330);
            		 g.drawLine(220, 340, 220, 330);
            		 g.drawLine(220, 380, 220, 390);
            		 g.drawLine(280, 390, 220, 390);
            		 g.drawLine(280, 380, 280, 390 );
            		 g.drawLine(280, 330, 280, 340);
            		 g.drawLine(390, 150, 330, 150);
            		 g.drawLine(330, 150, 330, 160);
            		 g.drawLine(330, 210, 340, 210);
            		 g.drawLine(330, 200, 330, 210 );
            		 g.drawLine(390, 210, 390, 150);
            		 g.drawLine(110, 160, 110, 70);
            		 g.drawLine(74, 160, 110, 160);
            		 g.drawLine(74, 200, 140, 200);
            		 g.drawLine(140, 270, 140, 200 );
            		 g.drawLine(130, 270, 140, 270);
            		 g.drawLine(130, 310, 140, 310);
            		 g.drawLine(140, 380, 140, 310);
            		 g.drawLine(140, 380, 220, 380);
            		 g.drawLine(180, 200, 180, 340 );
            		 g.drawLine(180, 200, 200, 200);
            		 g.drawLine(150, 160, 150, 70);
            		 g.drawLine(150, 160, 200, 160);
            		 g.drawLine(380, 210, 390, 210);
            		 
            		 g.drawLine(260, 160, 330, 160 );
            		 
            		 g.drawLine(180, 340, 220, 340);
            		 g.drawLine(330, 200, 260, 200);
            		 
            		 g.drawLine(340	, 340, 280, 340);
            		 g.drawLine(380, 380, 280, 380);
            		 g.drawLine(380, 380, 380, 210 );
            		 g.drawLine(340, 340, 340, 210);*/
            		// canvas.paint(g);
            	 // set color to red 
            
            	
  
            } 
        }; 
        canvas.addKeyListener(new MKeyListener());
  
        // set background 
        canvas.setBackground(Color.black); 
  
        add(canvas); 
        setSize(canvasX, canvasy); 
        
        show(); 
    } 
	public void paint (Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		 g2.setColor(Color.BLUE);
		
	 Line2D line = new Line2D.Float(100, 10, 160, 10);
		// g2.drawLine(100, 70, 100, 10);
		 g2.draw(line);
		 
	}
	
public static void main(String[] args) {
	 
	 
	Main main = new Main();
	//main.paint();
	//main.setup();
	main.communicate();
}
boolean hey = true;
public void communicate () 
{
  
	Socket s;
	try {
		s = new Socket(ipAdress , portNumber);
	

	System.out.println("Setup socket");
	
	 dis = new DataInputStream(s.getInputStream());
	 dos = new DataOutputStream(s.getOutputStream());
	while(hey)
	 {
		try {
	 String readMessage = dis.readUTF();
	 String[] cordinate = readMessage.split(" ");
	 if(readMessage.equals("done"))
	 {
		 System.out.print("done");
		 hey=false;
		 
		 }
	 else {
	 for(String cordi: cordinate)
	 {
		
	    
		 System.out.print(cordi+"|");
		
	 }
	 Graphics g = canvas.getGraphics();
		 g.setColor(Color.RED);
	     g.drawLine((int)Float.parseFloat(cordinate[0])/10, (int)Float.parseFloat(cordinate[1])/10,
	    		 (int)Float.parseFloat(cordinate[2])/10,(int) Float.parseFloat(cordinate[3])/10);
	     canvas.paint(g);
	 System.out.println();
	}
	 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	System.out.println("ready");
	painting();
	 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
}
private void setup() {
Graphics g = canvas.getGraphics();
 g.setColor(Color.BLUE);
 g.drawLine(100, 10, 160, 10);
 canvas.paint(g);
}

public void constuctMap()bg
{

}

public void painting()
{System.out.println("1");
 int previusX = 0;
 int previusY = 0; System.out.println("2");
 done = false;
 while(!done)
	{
	 System.out.println("3");
	
 try
 {
	 //LineMap map = new SVGMapLoader(new FileInputStream(filePath)).readLineMap();

 String readMessage = dis.readUTF();
 String[] cordinate = readMessage.split(" ");
 System.out.println(cordinate[0]);
 
 int newX= (int) Float.parseFloat(cordinate[0]);
 int newY= (int) Float.parseFloat(cordinate[1]);
 //int wayPointx = (int) Double.parseDouble(cordinate[2]);
 //int wayPointy = (int) Double.parseDouble(cordinate[3]);
 System.out.println(newX);
 System.out.println(newY);
 Graphics g = canvas.getGraphics();
 g.setColor(Color.RED);
 g.drawLine(previusX/10, previusY/10, newX/10, newY/10);
 /*g.setColor(Color.BLUE);
 g.drawLine(canvasX/2-((wayPointx-10)/5),canvasy/2- ((wayPointy)/5), canvasX/2-((wayPointx+10)/5),  canvasy/2-((wayPointy)/5));
 g.drawLine(canvasX/2-((wayPointx)/5),canvasy/2- ((wayPointy-10)/5), canvasX/2-((wayPointx)/5),  canvasy/2-((wayPointy+10)/5));
 */
 canvas.paint(g);
 previusX = newX;
 previusY = newY;
 }
 catch (Exception ex)
 {
	 System.out.println(ex);
 }}
	
}


/*
Scanner sc = new Scanner(System.in);
boolean running= true; 

while(running)
{
System.out.println("Say something to the robot");
if(sc.hasNext()) {
	   String token = sc.next();
	   
	}*/

//message.intValue();
//System.out.print(message.intValue());
/*dos.writeUTF(message);
dos.flush();*/

/*String readMessage = dis.readUTF();
if(readMessage.toLowerCase().equals("quit"))
{
	running = false ;
}
else
{
	System.out.println("Robot say: "+ readMessage);
}*/
public void sendMessage(String message) 
{
 
 System.out.println(message);
 try {
	dos.writeUTF(message);
	dos.flush();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
}

class MKeyListener extends KeyAdapter {
 
@Override
public void keyPressed(KeyEvent event) {

	 int keyCode = event.getKeyCode();
		switch( keyCode ) { 
	    case KeyEvent.VK_UP:
	    	hey =false;
	    	sendMessage("forward");
	        // handle up 
	        break;
	    case KeyEvent.VK_DOWN:
	    	sendMessage("backward");
	        // handle down 
	        break;
	    case KeyEvent.VK_LEFT:
	    	sendMessage("left");
	        // handle left
	        break;
	    case KeyEvent.VK_RIGHT :
	    	sendMessage("right");
	        // handle right
	        break;
	    case KeyEvent.VK_SPACE :
	    	sendMessage("stop");
	        // handle right
	    	done = true;
	        break;
	 }
}
}
