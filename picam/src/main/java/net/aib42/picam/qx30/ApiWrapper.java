package net.aib42.picam.qx30;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ApiWrapper {
	private HttpClient httpClient;
	private Gson gson;
	private JsonParser parser;

	@SuppressWarnings("serial")
	public static class InvalidStatusCodeException extends IOException {
		public InvalidStatusCodeException(int statusCode) {
			super("Invalid response HTTP status code " + statusCode);
		}
	}

	public ApiWrapper() {
		httpClient = HttpClients.createDefault();
		gson = new Gson();
		parser = new JsonParser();
	}

	public JsonElement makeRequest(String cameraUrl, ApiRequest.Request request) throws IOException {
		String url = cameraUrl + "/" + request.endpoint;
		String body = gson.toJson(request.body);

		System.out.println(body);

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
		HttpResponse response = httpClient.execute(post);
		int responseStatusCode = response.getStatusLine().getStatusCode();
		String responseBody = EntityUtils.toString(response.getEntity());

		System.out.println(responseBody);

		if (responseStatusCode != 200) {
			throw new InvalidStatusCodeException(responseStatusCode);
		}

		return parser.parse(responseBody);
	}
}
