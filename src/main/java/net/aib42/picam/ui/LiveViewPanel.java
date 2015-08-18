package net.aib42.picam.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.aib42.picam.MainApp;
import net.aib42.picam.qx30.LiveviewStreamer;

@SuppressWarnings("serial")
public class LiveViewPanel extends JPanel implements ActionListener {
	private MainApp mainApp;
	private String functionName;

	private LiveviewImagePanel liveviewImagePanel;
	private JButton startButton;

	private boolean liveviewStarted = false;

	public LiveViewPanel(MainApp mainApp, String functionName) {
		this.mainApp = mainApp;
		this.functionName = functionName;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		liveviewImagePanel = new LiveviewImagePanel();
		liveviewImagePanel.setPreferredSize(new Dimension(640, 480));
		add(liveviewImagePanel);

		add(setupControls());
	}

	private JPanel setupControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "START") {
			if (!liveviewStarted) {
				mainApp.startLiveview(new MainApp.LiveviewUpdater() {
					@Override
					public void update(LiveviewStreamer streamer) throws IOException {
						liveviewImagePanel.update(streamer);
					}
				}, functionName);
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
