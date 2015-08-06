package phong.android.com.entity;

import java.io.Serializable;

public class ValidateLogin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String validated;

	public ValidateLogin(String validated) {

		this.validated = validated;
	}

	public ValidateLogin() {

	}

	public String getValidated() {
		return validated;
	}

	public void setValidated(String validated) {
		this.validated = validated;
	}

}
