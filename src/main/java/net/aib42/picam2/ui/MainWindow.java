package net.aib42.picam2.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.aib42.picam2.qx30.Requester;

public class MainWindow implements ActionListener {
	private Requester req;
	private JPanel mainPanel = new JPanel();
	private ImagePanel imagePanel = new ImagePanel();

	public MainWindow() throws IllegalStateException, IOException {
		req = new Requester();
		req.makeRequest();
		
		JFrame frame = new JFrame("picam2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupMainPanel();
		frame.getContentPane().add(mainPanel);

		frame.pack();
		frame.setVisible(true);
	}

	private void setupMainPanel() {
		imagePanel.setPreferredSize(new Dimension(640, 480));
		mainPanel.add(imagePanel);

		JButton startButton = new JButton("Start");
		startButton.setActionCommand("START");
		startButton.addActionListener(this);
		mainPanel.add(startButton, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "START") {
			try {
				imagePanel.update(req.getNextJpegInputStream());
				mainPanel.repaint();
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}
	}
}
