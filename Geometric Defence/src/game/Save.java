package game;

// Imports
import java.io.File;
import java.util.Scanner;

public class Save {
	public void loadSave(File loadPath) {
		try {
			Scanner loadScanner = new Scanner(loadPath);

			// First loads the number of enemies you need to kill to advance to the next level
			while (loadScanner.hasNext()) {
				Screen.killsToWin = loadScanner.nextInt();
				// Makes sure that the files loading have the correct number of rows and columns which can be found in the stage class
				for (int y = 0; y < Screen.stage.cell.length; y++)
					for (int x = 0; x < Screen.stage.cell[0].length; x++) {
						Screen.stage.cell[y][x].groundID = loadScanner.nextInt();
					}
				
				for (int y = 0; y < Screen.stage.cell.length; y++)
					for (int x = 0; x < Screen.stage.cell[0].length; x++) {
						Screen.stage.cell[y][x].airID = loadScanner.nextInt();
					}
			}
			
			loadScanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}