package net.aib42.picam.ui;

public class Driver {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow main = new MainWindow();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		});
	}
}
