package net.aib42.picam.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.aib42.picam.qx30.LiveviewStreamer;

import com.example.sony.cameraremote.utils.SimpleHttpClient;
import com.example.sony.cameraremote.utils.SimpleLiveviewSlicer;
import com.example.sony.cameraremote.utils.SimpleLiveviewSlicer.Payload;

public class MainWindow implements ActionListener {
	private String cameraUrl = "http://188.59.135.36:5000";

	private SimpleLiveviewSlicer slicer = new SimpleLiveviewSlicer();

	private JPanel mainPanel = new JPanel();
	private LiveviewImagePanel imagePanel = new LiveviewImagePanel();
	private JTextField urlTextField;
	private Thread liveViewThread;
	private boolean runThread;
	private JButton startButton;
	private LiveviewStreamer requester;

	public MainWindow() {
		slicer = new SimpleLiveviewSlicer();
		requester = new LiveviewStreamer();

		JFrame frame = new JFrame("picam");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupMainPanel();
		frame.getContentPane().add(mainPanel);

		frame.pack();
		frame.setVisible(true);
	}

	private void setupMainPanel() {
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		imagePanel.setPreferredSize(new Dimension(640, 480));
		mainPanel.add(imagePanel);

		mainPanel.add(createControls());
	}

	private JPanel createControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.LINE_AXIS));

		urlTextField = new JTextField(cameraUrl);
		controls.add(urlTextField);

		startButton = new JButton("Start");
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
		if (e.getActionCommand() == "START") {
			if (liveViewThread == null) {
				cameraUrl = urlTextField.getText();
				System.out.println("Starting live view from " + cameraUrl);
				try {
					requester.start(cameraUrl);
				} catch (IOException ex) {
					ex.printStackTrace(System.err);
				}
				liveViewThread = new Thread() {
					@Override
					public void run() {
						try {
							while (runThread) {
								imagePanel.update(requester);
								mainPanel.repaint();
							}
						} catch (IOException ex) {
							ex.printStackTrace(System.err);
						}
						System.out.println("Live view stopped");
					}
				};
				runThread = true;
				liveViewThread.start();
				startButton.setText("Stop");
			} else {
				System.out.println("Stopping live view");
				runThread = false;
				liveViewThread = null;
				slicer.close();
				startButton.setText("Start");
			}
		}

		else if (e.getActionCommand() == "ZOOM-IN") {
			try {
				SimpleHttpClient.httpPost(cameraUrl + "/sony/camera", "{\"id\":1,\"method\":\"actZoom\",\"params\":[\"in\",\"1shot\"],\"version\":\"1.0\"}");
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		} else if (e.getActionCommand() == "ZOOM-OUT") {
			try {
				SimpleHttpClient.httpPost(cameraUrl + "/sony/camera", "{\"id\":1,\"method\":\"actZoom\",\"params\":[\"out\",\"1shot\"],\"version\":\"1.0\"}");
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}

	}
}