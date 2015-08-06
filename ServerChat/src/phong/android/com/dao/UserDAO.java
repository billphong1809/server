package phong.android.com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import phong.android.com.entity.UserLogin;
import phong.android.com.utilities.DataConnect;

public class UserDAO {

	public synchronized UserLogin checkLogin(UserLogin usersLogin) {
		String login = "SELECT * FROM tblusers WHERE usersname = ? AND password = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(login);
			ps.setString(1, usersLogin.getUsersName());
			ps.setString(2, usersLogin.getPassWord());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				usersLogin = new UserLogin();
				usersLogin.setUsersName(rs.getString("usersname"));
				usersLogin.setAvatar(rs.getString("avartar"));
				usersLogin.setStatus(rs.getString("status"));

			}
		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}
		return usersLogin;
	}

	public synchronized Vector<UserLogin> getListFriend(String usersname) {
		Vector<UserLogin> vtUserLogin = new Vector<UserLogin>();
		String sql = "SELECT chat_users.tblusers.usersname,chat_users.tblusers.avartar,chat_users.tblusers.status "
				+ "FROM chat_users.tblusers,chat_users.tblfriends "
				+ "WHERE chat_users.tblfriends.usersname= ? "
				+ "AND chat_users.tblusers.usersname = chat_users.tblfriends.friends";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, usersname);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				UserLogin usersLogin = new UserLogin();
				usersLogin.setUsersName(rs.getString("usersname"));
				usersLogin.setAvatar(rs.getString("avartar"));
				usersLogin.setStatus(rs.getString("status"));
				vtUserLogin.add(usersLogin);
			}
		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}

		return vtUserLogin;

	}

	public synchronized Vector<UserLogin> getUserAfterUpdate(String usersName) {

		Vector<UserLogin> vtUserLogin = new Vector<UserLogin>();
		String sql = "SELECT chat_users.tblusers.usersname,chat_users.tblusers.avartar,chat_users.tblusers.status "
				+ "FROM chat_users.tblusers,chat_users.tblfriends "
				+ "WHERE chat_users.tblfriends.usersname= ? "
				+ "AND chat_users.tblusers.usersname = chat_users.tblfriends.friends";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, usersName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				UserLogin usersLogin = new UserLogin();
				usersLogin.setUsersName(rs.getString("usersname"));
				usersLogin.setAvatar(rs.getString("avartar"));
				usersLogin.setStatus(rs.getString("status"));
				vtUserLogin.add(usersLogin);
			}
		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}

		return vtUserLogin;

	}

	public synchronized boolean addFriendToDataBase(String usersnameMe,
			String usersnameFriend) {

		String addSql = "INSERT INTO chat_users.tblfriends(usersname,friends) VALUES (?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(addSql);
			ps.setString(1, usersnameMe);
			ps.setString(2, usersnameFriend);
			ps.executeUpdate();
			return true;

		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}

		return false;

	}

	public synchronized UserLogin getUserLogin(String usersname) {

		UserLogin usersLogin = new UserLogin();
		String sqlGetUser = "SELECT chat_users.tblusers.usersname,chat_users.tblusers.avartar,chat_users.tblusers.status "
				+ "FROM chat_users.tblusers "
				+ "WHERE chat_users.tblusers.usersname= ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(sqlGetUser);
			ps.setString(1, usersname);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				usersLogin.setUsersName(rs.getString("usersname"));
				usersLogin.setAvatar(rs.getString("avartar"));
				usersLogin.setStatus(rs.getString("status"));

			}

		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}

		return usersLogin;

	}

	@SuppressWarnings("unused")
	public synchronized boolean checkFriend(String usersnameMe,
			String usersnameFriend) {

		ArrayList<String> arrString = new ArrayList<String>();
		String sql = "SELECT chat_users.tblfriends.friends"
				+ " FROM chat_users.tblfriends"
				+ " WHERE  chat_users.tblfriends.usersname=? ";

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, usersnameMe);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				arrString.add(rs.getString("friends"));
			}

			for (int i = 0; i < arrString.size(); i++) {
				if (arrString.get(i).equals(usersnameFriend)) {
					return true;
				}

			}
		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}
		return false;
	}

	public synchronized boolean changeStatus(String newStatus, String usersname) {
		String changeStatus = "UPDATE chat_users.tblusers SET status = ? WHERE chat_users.tblusers.usersname = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement(changeStatus);
			ps.setString(1, newStatus);
			ps.setString(2, usersname);
			ps.executeUpdate();
			return true;

		} catch (Exception e) {
			System.out.println("Error  : " + e.getMessage());
		}

		return false;

	}

	 public static void main(String[] args) {
	 UserDAO u = new UserDAO();
	 if(u.changeStatus("vanphonghy", "Done")){
	 System.out.println("ok");
	 }else{
	 System.out.println("no");
	 }
	 }

}
