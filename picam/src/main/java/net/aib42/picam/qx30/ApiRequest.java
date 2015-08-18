package net.aib42.picam.qx30;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiRequest {
	private static final String CAMERA_ENDPOINT = "sony/camera";
	private static final String CONTENT_ENDPOINT = "sony/avContent";

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

	public Request.Body createBody(String version, String method, List<Object> params) {
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
		return createRequest(CAMERA_ENDPOINT, createBody("1.0", "actZoom", asParams("in", "1shot")));
	}

	public Request zoomOut() {
		return createRequest(CAMERA_ENDPOINT, createBody("1.0", "actZoom", asParams("out", "1shot")));
	}

	public Request setShootMode(String shootMode) {
		return createRequest(CAMERA_ENDPOINT, createBody("1.0", "setShootMode", asParams(shootMode)));
	}

	public Request startRecording() {
		return createRequest(CAMERA_ENDPOINT, createBody("1.0", "startMovieRec", asParams()));
	}

	public Request stopRecording() {
		return createRequest(CAMERA_ENDPOINT, createBody("1.0", "stopMovieRec", asParams()));
	}
	public Request getContentList(int startIndex) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uri", "storage:memoryCard1");
		params.put("stIdx", startIndex);
		params.put("cnt", 1);
		params.put("view", "date");
		params.put("sort", "descending");

		return createRequest(
			CONTENT_ENDPOINT,
			createBody(
				"1.3",
				"getContentList",
				asParams(params)
			)
		);
	}
}
