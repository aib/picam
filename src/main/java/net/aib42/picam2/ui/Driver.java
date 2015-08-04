package net.aib42.picam2.ui;

public class Driver {
	public static void main(String[] args) {
		System.out.println("Hello, World!");

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
