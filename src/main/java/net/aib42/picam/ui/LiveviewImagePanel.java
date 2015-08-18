package net.aib42.picam.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import net.aib42.picam.qx30.LiveviewStreamer;

@SuppressWarnings("serial")
public class LiveviewImagePanel extends JPanel {
	BufferedImage image;

	protected void update(LiveviewStreamer lvs) throws IOException {
		LiveviewStreamer.JpegPayload payload = lvs.getNextJpeg();
		image = ImageIO.read(new ByteArrayInputStream(payload.bytes));
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
