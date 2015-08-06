package phong.android.com.thread;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import phong.android.com.control.ServerControl;
import phong.android.com.entity.ListPeople;
import phong.android.com.entity.ListUserLogin;
import phong.android.com.entity.MessageWithIP;
import phong.android.com.entity.MutiUsersAdd;
import phong.android.com.entity.UserLogin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ThreadChat extends Thread {

	private Socket socket;
	private ServerControl serverControl;
	UserLogin me;
	UserLogin friend;

	public ThreadChat(Socket socket, ServerControl serverControl) {
		super();
		this.socket = socket;
		this.serverControl = serverControl;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			while (true) {
				Object obj = (Object) ois.readObject();
				if (obj instanceof String) {
					String str = (String) obj;
					serverControl.serverView.showMessage("\n TASKING : "
							+ obj.toString());
					JSONObject jsonObject = new JSONObject(str);
					String flag = jsonObject.getString("flag");
					serverControl.serverView
							.showMessage("\n ########## FLAG TO SERVER : "
									+ flag + " ##########");
					if (flag.equals("login")) {
						handingLogin(str);
					} else if (flag.equals("msg")) {
						handingMessage(str);
					} else if (flag.equals("logout")) {
						handingLogout(str);
					} else if (flag.equals("add_friends")) {
						handingAddFriendToDatabase(str);
					} else if (flag.equals("accept")) {
						handingAcceptAddFriend(str);
					} else if (flag.equals("change_status")) {
						handingChangeStatus(str);

					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Server Error>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ex);

		} finally {
			serverControl.removeClient(socket);
			stopThread();
		}
	}

	@SuppressWarnings("deprecation")
	public void stopThread() {
		this.stop();
	}

	public Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;
	}

	private void handingLogin(String jsonToServer) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonToServer);
			String usersname = jsonObject.getString("usersName");

			String pass = jsonObject.getString("passWord");
			UserLogin userLogin = new UserLogin(usersname, pass, null, null,
					null);
			if (serverControl.checkLoginFromInput(userLogin) != null) {
				serverControl.hashConnect.put(usersname, socket);
				for (String key : serverControl.hashConnect.keySet()) {
					serverControl.serverView.showMessage("\n"
							+ "HASHTABLE HAS : " + key);
				}
				userLogin = serverControl.checkLoginFromInput(userLogin);
				String jsonBooleanCheckLogin = getGson().toJson(userLogin);
				Vector<UserLogin> vtUsersLogin = serverControl
						.getAllListFriend(usersname);
				ListPeople listPeople = new ListPeople(vtUsersLogin);
				String jsonListFrient = getGson().toJson(listPeople);
				serverControl.sendToOne(jsonListFrient + "|"
						+ jsonBooleanCheckLogin, socket
						.getRemoteSocketAddress().toString());

			} else {
				userLogin = new UserLogin(null, null, "false", null, null);
				String jsonBooleanCheckLogin = getGson().toJson(userLogin);
				serverControl.sendToOne(jsonBooleanCheckLogin, socket
						.getRemoteSocketAddress().toString());
			}
		} catch (JSONException e) {

			e.printStackTrace();
			System.out.println("ERROR HANDING LOGIN : " + e.toString());
		}
	}

	private void handingMessage(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String receivedObj = jsonObject.getString("received");
			JSONObject jsonObjectReceived = new JSONObject(receivedObj);
			String received = jsonObjectReceived.getString("usersName");
			String senderObj = jsonObject.getString("sender");
			JSONObject jsonObjectSender = new JSONObject(senderObj);
			String sender = jsonObjectSender.getString("usersName");
			String msg = jsonObject.getString("message");
			serverControl.serverView.showMessage("\n" + "MESSAGE : " + msg
					+ " || SENDER : " + sender + " || RECEIVED : " + received);

			Vector<String> vtString = serverControl
					.addToVector(serverControl.hashConnect);

			for (int i = 0; i < vtString.size(); i++) {

				if (vtString.get(i).equals(received)) {
					Socket socket = serverControl.hashConnect.get(received);

					me = new UserLogin(sender, null, null, null, null);
					friend = new UserLogin(received, null, null, null, null);

					MessageWithIP msgIP = new MessageWithIP(msg, me, friend,
							"msg");

					String jsonMsgToClient = getGson().toJson(msgIP);

					serverControl.sendToOne(jsonMsgToClient, socket
							.getRemoteSocketAddress().toString());

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("ERROR HANDING MESSAGE : " + e.toString());

		}

	}

	@SuppressWarnings("deprecation")
	private void handingLogout(String json) {
		JSONObject jsonObject;
		try {

			jsonObject = new JSONObject(json);

			String usernameLogout = jsonObject.getString("usersName");

			serverControl.serverView.showMessage("USERS LOGOUT : "
					+ usernameLogout);
			Vector<String> vtString = serverControl
					.addToVector(serverControl.hashConnect);

			for (int i = 0; i < vtString.size(); i++) {
				if (vtString.get(i).equals(usernameLogout)) {
					Socket socket = serverControl.hashConnect
							.get(usernameLogout);
					UserLogin userLogout = new UserLogin(usernameLogout, null,
							"logout_success", null, null);
					String logoutJson = getGson().toJson(userLogout);

					serverControl.sendToOne(logoutJson, socket
							.getRemoteSocketAddress().toString());
					serverControl.outputStream.remove(socket);

					this.stop();
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("EXCEPTION " + e);

		}
	}

	private void handingAddFriendToDatabase(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String userFriendAdd = jsonObject.getString("friend");
			JSONObject jsonObjectFriendAdd = new JSONObject(userFriendAdd);
			String friendAdd = jsonObjectFriendAdd.getString("usersName");
			String userMeAdd = jsonObject.getString("me");
			JSONObject jsonObjectMeAdd = new JSONObject(userMeAdd);
			String meAdd = jsonObjectMeAdd.getString("usersName");
			serverControl.serverView.showMessage("\n " + " USER ADD : " + meAdd
					+ " FRIEND ADD : " + friendAdd);
			if ((serverControl.getUsersLogin(friendAdd) != null)
					&& (serverControl.checkFriend(meAdd, friendAdd) == false)) {
				Vector<String> vtString = serverControl
						.addToVector(serverControl.hashConnect);
				for (int i = 0; i < vtString.size(); i++) {
					if (vtString.get(i).equals(friendAdd)) {
						Socket socket = serverControl.hashConnect
								.get(friendAdd);
						me = new UserLogin(meAdd, null, null, null, null);
						friend = new UserLogin(friendAdd, null, null, null,
								null);
						MutiUsersAdd mutiAdduser = new MutiUsersAdd(me, friend,
								"add_success");
						String addJson = getGson().toJson(mutiAdduser);
						serverControl.sendToOne(addJson, socket
								.getRemoteSocketAddress().toString());
						serverControl.serverView
								.showMessage("\n DATA TO CLIENT ADD WITH FLAG ADD FRIEND \n "
										+ addJson);
					}
				}
			} else {
				Vector<String> vtString = serverControl
						.addToVector(serverControl.hashConnect);
				for (int i = 0; i < vtString.size(); i++) {

					if (vtString.get(i).equals(meAdd)) {
						Socket socket = serverControl.hashConnect.get(meAdd);
						UserLogin usersLogin = new UserLogin(null, null,
								"failure", null, null);
						String addJson = getGson().toJson(usersLogin);
						serverControl.sendToOne(addJson, socket
								.getRemoteSocketAddress().toString());
						serverControl.serverView
								.showMessage("\n DATA TO CLIENT ADD WITH FLAG ADD FRIEND \n "
										+ addJson);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION " + e);

		}
	}

	private void handingAcceptAddFriend(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			String meObjAccept = jsonObject.getString("me");
			String friendObjAccept = jsonObject.getString("friend");

			JSONObject jsonObjectMe = new JSONObject(meObjAccept);

			JSONObject jsonObjectFriend = new JSONObject(friendObjAccept);

			String meAdd = jsonObjectMe.getString("usersName");

			String friendAdd = jsonObjectFriend.getString("usersName");

			serverControl.addFriend(meAdd, friendAdd);
			serverControl.addFriend(friendAdd, meAdd);
			serverControl.serverView.showMessage("\n USER ACCEPT : " + meAdd
					+ " USERS FRIEND : " + friendAdd);
			Vector<String> vtString = serverControl
					.addToVector(serverControl.hashConnect);
			for (int i = 0; i < vtString.size(); i++) {
				serverControl.serverView
						.showMessage("\n DATA TO CLIENT ADD WITH FLAG DONE" + i);
				if (vtString.get(i).equals(friendAdd)) {
					Socket socket = serverControl.hashConnect.get(friendAdd);

					me = new UserLogin(meAdd, null, null, null, null);
					friend = new UserLogin(friendAdd, null, null, null, null);

					MutiUsersAdd mutiUser = new MutiUsersAdd(me, friend, "done");

					String addJson = getGson().toJson(mutiUser);
					serverControl.serverView
							.showMessage("\n DATA TO CLIENT ADD WITH FLAG DONE"
									+ addJson);

					serverControl.sendToOne(addJson, socket
							.getRemoteSocketAddress().toString());
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void handingChangeStatus(String json) {

		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(json);
			String usersname = jsonObject.getString("usersName");
			String newStatus = jsonObject.getString("status");
			serverControl.serverView
					.showMessage("\n NEW STATUS : " + newStatus);

			if (serverControl.changeStatus(newStatus, usersname)) {

				Vector<String> vtString = serverControl
						.addToVector(serverControl.hashConnect);
				for (int i = 0; i < vtString.size(); i++) {
					if (vtString.get(i).equals(usersname)) {
						Socket socket = serverControl.hashConnect
								.get(usersname);

						Vector<UserLogin> vtUsersLogin = serverControl
								.getFriendAffterUpdate(usersname);

						ListUserLogin listPeople = new ListUserLogin(
								vtUsersLogin, "change_success");

						String jsonToclient = getGson().toJson(listPeople);

						serverControl.serverView
								.showMessage("\n DATA TO CLIENT ADD WITH FLAG CHANGE STATUS SUCCESS"
										+ jsonToclient);
						serverControl.sendToOne(jsonToclient, socket
								.getRemoteSocketAddress().toString());
					}

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
