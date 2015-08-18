package net.aib42.picam.ui;

import net.aib42.picam.MainApp;

public class Driver {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainApp mainApp = new MainApp();
				MainWindow mainWindow = new MainWindow(mainApp);
			}
		});
	}
}
