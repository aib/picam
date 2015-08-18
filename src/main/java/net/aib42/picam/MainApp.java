package net.aib42.picam;
import java.io.IOException;

import net.aib42.picam.qx30.ApiRequest;
import net.aib42.picam.qx30.ApiWrapper;

public class MainApp {
	private String cameraUrl = "http://188.59.135.36:5000";

	private ApiRequest apiReq;
	private ApiWrapper apiWrapper;

	public MainApp() {
		apiReq = new ApiRequest();
		apiWrapper = new ApiWrapper();
	}

	public String getVersionString() {
		return "0.0.3";
	}

	public String getCameraUrl() {
		return cameraUrl;
	}

	public void setCameraUrl(String cameraUrl) {
		this.cameraUrl = cameraUrl;
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

	public void startRecording() {
		try {
			apiWrapper.makeRequest(cameraUrl, apiReq.startRecording());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}

	public void stopRecording() {
		try {
			apiWrapper.makeRequest(cameraUrl, apiReq.stopRecording());
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
	}
}
