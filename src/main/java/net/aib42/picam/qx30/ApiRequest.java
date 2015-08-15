package net.aib42.picam.qx30;

import java.util.Arrays;
import java.util.List;

public class ApiRequest {
	private static final String CAMERA_ENDPOINT = "sony/camera";

	private String version = "1.0";
	private int id = 1;

	public static class Request {
		public static class Body {
			public String version;
			public int id;
			public String method;
			public List<Object> params;
		}

		public String endpoint;
		public Body body; 
	}

	public static List<Object> asParams(Object... params) {
		return Arrays.asList(params);
	}

	public Request.Body createBody(String method, List<Object> params) {
		Request.Body body = new Request.Body();
		body.version = version;
		body.id = id;
		body.method = method;
		body.params = params;
		return body;
	}

	public Request createRequest(String endpoint, Request.Body body) {
		Request request = new Request();
		request.endpoint = endpoint;
		request.body = body;
		return request;
	}

	public Request zoomIn() {
		return createRequest(CAMERA_ENDPOINT, createBody("actZoom", asParams("in", "1shot")));
	}

	public Request zoomOut() {
		return createRequest(CAMERA_ENDPOINT, createBody("actZoom", asParams("out", "1shot")));
	}
}
