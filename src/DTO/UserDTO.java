package DTO;

public class UserDTO {
	private int userIdx;
	private String userId;
	private String userPw;
	private String userName;
	private String userPhone;
	private String userGenre1;
	private String userGenre2;
	
	public UserDTO() {}
	
	

	public UserDTO(String userId, String userPw, String userName, String userPhone, String userGenre1,
			String userGenre2) {
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userGenre1 = userGenre1;
		this.userGenre2 = userGenre2;
	}
	
	
	

	public UserDTO(int userIdx, String userId, String userPw, String userName, String userPhone, String userGenre1,
			String userGenre2) {
		this.userIdx = userIdx;
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userGenre1 = userGenre1;
		this.userGenre2 = userGenre2;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getUserPw() {
		return userPw;
	}



	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserPhone() {
		return userPhone;
	}



	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}



	public String getUserGenre1() {
		return userGenre1;
	}



	public void setUserGenre1(String userGenre1) {
		this.userGenre1 = userGenre1;
	}



	public String getUserGenre2() {
		return userGenre2;
	}



	public void setUserGenre2(String userGenre2) {
		this.userGenre2 = userGenre2;
	}

	


	public int getUserIdx() {
		return userIdx;
	}



	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}



	public boolean equals(Object obj) {
		if (obj instanceof UserDTO) {
			UserDTO target = (UserDTO)obj;
			if (target.equals(obj)) {
				return true;
			}
		}
		return false;
	}
	
}
