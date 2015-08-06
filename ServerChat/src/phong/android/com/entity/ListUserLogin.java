package phong.android.com.entity;

import java.io.Serializable;
import java.util.Vector;

public class ListUserLogin implements Serializable {
	private Vector<UserLogin> vtUsersLogin;
	private String flag;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListUserLogin() {

	}

	public ListUserLogin(Vector<UserLogin> vtUsersLogin, String flag) {
		super();
		this.vtUsersLogin = vtUsersLogin;
		this.flag = flag;
	}

	public Vector<UserLogin> getVtUsersLogin() {
		return vtUsersLogin;
	}

	public void setVtUsersLogin(Vector<UserLogin> vtUsersLogin) {
		this.vtUsersLogin = vtUsersLogin;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
