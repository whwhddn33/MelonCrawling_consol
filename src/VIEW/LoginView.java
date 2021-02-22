package VIEW;

import java.util.Scanner;

import DAO.Session;
import DAO.UserDAO;
import DTO.UserDTO;

public class LoginView {
	public LoginView() {
		UserDAO uda = new UserDAO();
		Scanner sc = new Scanner(System.in);
		System.out.println("아이디를 입력해주세요 : ");
//		String userId = "whwhddn33";
		String userId = sc.next();
		System.out.println("비밀번호를 입력해주세요 : ");
//		String userPw = "621621";
		String userPw = sc.next();
		userPw = uda.get256Hash(userPw);
		UserDTO session = uda.login(userId, userPw);
		if (session==null) {
			System.out.println("로그인 실패하였습니다./ 아이디 비밀번호를 확인해주세요");
		}else {
			System.out.println("로그인중입니다.");
			Session.put("session_idx", session.getUserIdx());
			System.out.println("로그인완료");
			new Main();
		}

	}
}
