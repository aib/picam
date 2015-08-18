package net.aib42.picam.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.aib42.picam.MainApp;
import net.aib42.picam.qx30.LiveviewStreamer;

public class MainWindow implements ActionListener {
	private MainApp mainApp;

	private LiveviewImagePanel liveviewImagePanel;
	private JTextField urlTextField;
	private JButton startButton;

	private boolean liveviewStarted = false;

	public MainWindow(MainApp mainApp) {
		this.mainApp = mainApp;

		JFrame frame = new JFrame("picam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(setupLiveviewPanel());

		frame.pack();
		frame.setVisible(true);
	}

	private JPanel setupLiveviewPanel() {
		JPanel liveviewPanel = new JPanel();
		liveviewPanel.setLayout(new BoxLayout(liveviewPanel, BoxLayout.PAGE_AXIS));

		liveviewImagePanel = new LiveviewImagePanel();
		liveviewImagePanel.setPreferredSize(new Dimension(640, 480));
		liveviewPanel.add(liveviewImagePanel);
		liveviewPanel.add(setupLiveviewControls());

		return liveviewPanel;
	}

	private JPanel setupLiveviewControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

		urlTextField = new JTextField(mainApp.getCameraUrl());
		controls.add(urlTextField);

		startButton = new JButton();
		updateStartButton();
		startButton.setActionCommand("START");
		startButton.addActionListener(this);
		controls.add(startButton);

		JButton zoomIn = new JButton("Zoom +");
		zoomIn.setActionCommand("ZOOM-IN");
		zoomIn.addActionListener(this);
		controls.add(zoomIn);

		JButton zoomOut = new JButton("Zoom -");
		zoomOut.setActionCommand("ZOOM-OUT");
		zoomOut.addActionListener(this);
		controls.add(zoomOut);

		return controls;
	}

	public void actionPerformed(ActionEvent e) {
		mainApp.setCameraUrl(urlTextField.getText());

		if (e.getActionCommand() == "START") {
			if (!liveviewStarted) {
				mainApp.startLiveview(new MainApp.LiveviewUpdater() {
					@Override
					public void update(LiveviewStreamer streamer) throws IOException {
						liveviewImagePanel.update(streamer);
					}
				});
				liveviewStarted = true;
			} else {
				mainApp.stopLiveview();
				liveviewStarted = false;
			}
			updateStartButton();
		} else if (e.getActionCommand() == "ZOOM-IN") {
			mainApp.zoomIn();
		} else if (e.getActionCommand() == "ZOOM-OUT") {
			mainApp.zoomOut();
		}
	}

	private void updateStartButton() {
		if (liveviewStarted) {
			startButton.setText("Stop");
		} else {
			startButton.setText("Start");
		}
	}
}
