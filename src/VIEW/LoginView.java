package VIEW;

import java.util.Scanner;

import DAO.Session;
import DAO.UserDAO;
import DTO.UserDTO;

public class LoginView {
	public LoginView() {
		UserDAO uda = new UserDAO();
		Scanner sc = new Scanner(System.in);
		System.out.println("���̵� �Է����ּ��� : ");
//		String userId = "whwhddn33";
		String userId = sc.next();
		System.out.println("��й�ȣ�� �Է����ּ��� : ");
//		String userPw = "621621";
		String userPw = sc.next();
		userPw = uda.get256Hash(userPw);
		UserDTO session = uda.login(userId, userPw);
		if (session==null) {
			System.out.println("�α��� �����Ͽ����ϴ�./ ���̵� ��й�ȣ�� Ȯ�����ּ���");
		}else {
			System.out.println("�α������Դϴ�.");
			Session.put("session_idx", session.getUserIdx());
			System.out.println("�α��οϷ�");
			new Main();
		}

	}
}
