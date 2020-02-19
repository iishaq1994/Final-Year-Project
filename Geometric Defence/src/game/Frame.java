package game;

// Imports
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String title = "Geometric Defence";
	public static Dimension size = new Dimension(850, 650);

	public Frame() {
		
		// Sets title of the frame to the string above
		setTitle(title);
		
		// Sets the size of the window according to the measurements above
		setSize(size);
		
		// Stops the window from being resized
		setResizable(false);
		
		// Places the window in the centre of the screen
		setLocationRelativeTo(null);
		
		// Closes the window when the 'x' in the right hand corner is pressed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		init();
	}

	public void init() {
		setLayout(new GridLayout(1, 1, 0, 0));

		Screen screen = new Screen(this);
		add(screen);

		setVisible(true);
	}

	public static void main(String[] args) {
		Frame frame = new Frame();
	}
}