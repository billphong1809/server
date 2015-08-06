package phong.android.com.main;

import phong.android.com.control.ServerControl;
import phong.android.com.view.ServerViewChat;

public class MainRun {

	public static void main(String[] args) {
		ServerViewChat serverView = new ServerViewChat();
		new ServerControl(serverView);
	}

}
