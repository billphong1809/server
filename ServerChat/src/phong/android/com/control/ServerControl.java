package phong.android.com.control;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import phong.android.com.dao.UserDAO;
import phong.android.com.entity.UserLogin;
import phong.android.com.thread.ThreadChat;
import phong.android.com.view.ServerViewChat;

public class ServerControl {

	public ServerViewChat serverView;
	private ServerSocket serverSocket;
	private int port = 1111;
	public UserDAO userDAO;
	public Hashtable<Socket, ObjectOutputStream> outputStream = new Hashtable<Socket, ObjectOutputStream>();
	public Hashtable<String, Socket> hashConnect = new Hashtable<String, Socket>();

	public ServerControl(ServerViewChat serverView) {
		this.serverView = serverView;
		userDAO = new UserDAO();
		try {
			this.serverSocket = new ServerSocket(port);
			this.serverView
					.showMessage("====> Server Chat Mutithreading is running......in |+| "
							+ InetAddress.getLocalHost()
							+ " |+| Port :  "
							+ serverSocket.getLocalPort());
			this.serverView
					.showMessage("\n==========================================================================");
			listening();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listening() {
		while (true) {
			try {
				Socket clientSocketnew = serverSocket.accept();

				this.serverView.showMessage("\n<<=====>>CLIENT CONNECTED : "
						+ clientSocketnew);
				ObjectOutputStream outToClient = new ObjectOutputStream(
						clientSocketnew.getOutputStream());
				outputStream.put(clientSocketnew, outToClient);

				ThreadChat thread = new ThreadChat(clientSocketnew, this);
				thread.start();
			} catch (Exception e) {
				serverView.showMessage(e.toString());
			}
		}
	}

	public void removeClient(Socket socket) {
		try {
			this.serverView.showMessage("\n-----Client disconnect : " + socket);
			outputStream.remove(socket);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToOne(Object obj, String ipAddress) {
		synchronized (outputStream) {
			Enumeration<Socket> socket = outputStream.keys();
			while (socket.hasMoreElements()) {
				Socket client = (Socket) socket.nextElement();
				if (!client.getRemoteSocketAddress().toString()
						.equals(ipAddress)) {
					continue;
				}
				ObjectOutputStream clientStreamOut = (ObjectOutputStream) outputStream
						.get(client);
				try {
					clientStreamOut.writeObject(obj);
					clientStreamOut.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void sendToAll(Object obj) {
		synchronized (outputStream) {
			for (Enumeration<ObjectOutputStream> e = outputStream.elements(); e
					.hasMoreElements();) {
				ObjectOutputStream clientStreamOut = (ObjectOutputStream) e
						.nextElement();
				try {
					clientStreamOut.writeObject(obj);
					clientStreamOut.flush();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public UserLogin checkLoginFromInput(UserLogin usersLogin) {
		return userDAO.checkLogin(usersLogin);
	}

	public Vector<UserLogin> getAllListFriend(String usersname) {
		return userDAO.getListFriend(usersname);
	}

	public Vector<UserLogin> getFriendAffterUpdate(String usersname) {
		return userDAO.getUserAfterUpdate(usersname);
	}

	public synchronized Vector<String> addToVector(Hashtable<String, Socket> map) {
		Vector<String> vtString = new Vector<String>();

		for (String key : hashConnect.keySet()) {

			vtString.add(key);

		}

		return vtString;

	}

	public boolean addFriend(String usernameMe, String usernameFriend) {

		return userDAO.addFriendToDataBase(usernameMe, usernameFriend);
	}

	public UserLogin getUsersLogin(String usersname) {
		return userDAO.getUserLogin(usersname);

	}

	public boolean checkFriend(String usersnameMe, String usersnameFriend) {
		return userDAO.checkFriend(usersnameMe, usersnameFriend);
	}

	public boolean changeStatus(String newStatus, String usersName) {
		return userDAO.changeStatus(newStatus, usersName);

	}

}
