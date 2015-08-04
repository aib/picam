package net.aib42.picam2.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.example.sony.cameraremote.utils.SimpleHttpClient;
import com.example.sony.cameraremote.utils.SimpleLiveviewSlicer;
import com.example.sony.cameraremote.utils.SimpleLiveviewSlicer.Payload;

public class MainWindow implements ActionListener {
	private SimpleLiveviewSlicer slicer = new SimpleLiveviewSlicer();

	private JPanel mainPanel = new JPanel();
	private ImagePanel imagePanel = new ImagePanel();

	public MainWindow() throws IllegalStateException, IOException {
		slicer = new SimpleLiveviewSlicer();
		slicer.open("http://192.168.8.200:5000/liveview/liveviewstream");

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
		mainPanel.add(startButton);

		JButton zoomIn = new JButton("Zoom +");
		zoomIn.setActionCommand("ZOOM-IN");
		zoomIn.addActionListener(this);
		mainPanel.add(zoomIn);

		JButton zoomOut = new JButton("Zoom -");
		zoomOut.setActionCommand("ZOOM-OUT");
		zoomOut.addActionListener(this);
		mainPanel.add(zoomOut);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "START") {
			new Thread() {
				@Override
					public void run() {
					try {
						while (true) {
							Payload pl = slicer.nextPayload();
							InputStream is = new ByteArrayInputStream(pl.jpegData);
							imagePanel.update(is);
							mainPanel.repaint();
						}
					} catch (IOException ex) {
						ex.printStackTrace(System.err);
					}
				}
			}.start();
		}

		else if (e.getActionCommand() == "ZOOM-IN") {
			try {
				SimpleHttpClient.httpPost("http://192.168.8.200:5000/sony/camera", "{\"id\":1,\"method\":\"actZoom\",\"params\":[\"in\",\"1shot\"],\"version\":\"1.0\"}");
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		} else if (e.getActionCommand() == "ZOOM-OUT") {
			try {
				SimpleHttpClient.httpPost("http://192.168.8.200:5000/sony/camera", "{\"id\":1,\"method\":\"actZoom\",\"params\":[\"out\",\"1shot\"],\"version\":\"1.0\"}");
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}

	}
}