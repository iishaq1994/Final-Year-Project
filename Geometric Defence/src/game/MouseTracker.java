package game;

// Imports
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseTracker implements MouseMotionListener, MouseListener {

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
	
	// Selects the tower from the shop once it has been pressed
	public void mousePressed(MouseEvent e) {
		Screen.shop.click(e.getButton());
	}

	public void mouseReleased(MouseEvent e) {

	}
	
	// Keeps track of the mouse when its being dragged
	public void mouseDragged(MouseEvent e) {
		Screen.mse = new Point(e.getX() - ((Frame.size.width - Screen.myWidth) / 2), e.getY() - (Frame.size.height - Screen.myHeight - (Frame.size.width - Screen.myWidth) / 2));
	}
	// Keeps track of the new location of the mouse when the user stops moving it

	public void mouseMoved(MouseEvent e) {
		Screen.mse = new Point(e.getX() - ((Frame.size.width - Screen.myWidth) / 2), e.getY() - (Frame.size.height - Screen.myHeight - (Frame.size.width - Screen.myWidth) / 2));
	}

}