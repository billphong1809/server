package phong.android.com.entity;

import java.io.Serializable;

public class ResutlData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String result;

	public ResutlData() {

	}

	public ResutlData(String result) {
		super();
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 
	 */

}
