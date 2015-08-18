package net.aib42.picam;
import java.io.IOException;

import net.aib42.picam.qx30.ApiRequest;
import net.aib42.picam.qx30.ApiWrapper;
import net.aib42.picam.qx30.LiveviewStreamer;


public class MainApp {
	public interface LiveviewUpdater {
		public void update(LiveviewStreamer streamer) throws IOException;
	}

	private String cameraUrl = "http://188.59.135.36:5000";

	private LiveviewStreamer streamer;
	private ApiRequest apiReq;
	private ApiWrapper apiWrapper;

	private Thread liveViewThread;
	private boolean runThread;
	
	public MainApp() {
		streamer = new LiveviewStreamer();
		apiReq = new ApiRequest();
		apiWrapper = new ApiWrapper();
		liveViewThread = null;
		runThread = false;
	}

	public String getCameraUrl() {
		return cameraUrl;
	}

	public void setCameraUrl(String cameraUrl) {
		this.cameraUrl = cameraUrl;
	}

	public void startLiveview(final LiveviewUpdater lvUpdater) {
		System.out.println("Starting live view from " + cameraUrl);
		try {
			streamer.start(cameraUrl);
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}

		liveViewThread = new Thread() {
			@Override
			public void run() {
				try {
					while (runThread) {
						lvUpdater.update(streamer);
					}
				} catch (IOException ex) {
					ex.printStackTrace(System.err);
				}
				System.out.println("Live view stopped");
			}
		};

		runThread = true;
		liveViewThread.start();		
	}

	public void stopLiveview() {
		System.out.println("Stopping live view");
		runThread = false;
		liveViewThread = null;
		streamer.stop();
	}

	public void zoomIn() {
		try {
			apiWrapper.makeRequest(cameraUrl, apiReq.zoomIn());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void zoomOut() {
		try {
			apiWrapper.makeRequest(cameraUrl, apiReq.zoomOut());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
