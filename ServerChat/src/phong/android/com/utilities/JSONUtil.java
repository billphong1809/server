package phong.android.com.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	private static final String FLAG_SELF = "self";
	private static final String FLAG_NEW = "new";
	private static final String FLAG_MESSAGE = "message";
	private static final String FLAG_EXIT = "exit";

	public String getClientDetailsJson(String sessionId, String message) {
		String json = null;

		try {
			JSONObject jObj = new JSONObject();
			jObj.put("flag", FLAG_SELF);
			jObj.put("sessionId", sessionId);
			jObj.put("message", message);

			json = jObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public String getSendAllMessageJson(String sessionId, String fromName,
			String message) {
		String json = null;

		try {
			JSONObject jObj = new JSONObject();
			jObj.put("flag", FLAG_MESSAGE);
			jObj.put("sessionId", sessionId);
			jObj.put("name", fromName);
			jObj.put("message", message);
			json = jObj.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public String getClientExitJson(String sessionId, String name,
			String message, int onlineCount) {
		String json = null;

		try {
			JSONObject jObj = new JSONObject();
			jObj.put("flag", FLAG_EXIT);
			jObj.put("name", name);
			jObj.put("sessionId", sessionId);
			jObj.put("message", message);
			jObj.put("onlineCount", onlineCount);

			json = jObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public String getNewClientJson(String sessionId, String name,
			String message, int onlineCount) {
		String json = null;

		try {
			JSONObject jObj = new JSONObject();
			jObj.put("flag", FLAG_NEW);
			jObj.put("name", name);
			jObj.put("sessionId", sessionId);
			jObj.put("message", message);
			jObj.put("onlineCount", onlineCount);

			json = jObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}
