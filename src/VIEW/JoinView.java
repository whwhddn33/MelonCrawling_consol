package VIEW;

import java.util.Scanner;

import DAO.UserDAO;
import DTO.UserDTO;

public class JoinView {
	public JoinView() {
		UserDAO uda = new UserDAO();
		String userId;
		String userPw;
		String userPhone;
		int choiceGenre1 =0;
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("����� ���̵� �Է����ּ���.");
			userId =sc.next();
			
			if((uda.idcheck(userId) != 0))System.out.println("���̵� �ߺ��˴ϴ�.�ٽ� �Է����ּ���");
			else break;
		}
		while(true) {
			System.out.println("��й�ȣ�� �Է����ּ��� (4�ڸ��̻� ���).");
			userPw =sc.next(); 
			if (!uda.passLengthCheck(userPw))System.out.println("��й�ȣ�� �ʹ� ª���ϴ�");
			else {
				userPw = uda.get256Hash(userPw);
				break;
			}
		}
		
		System.out.println("������ �Է����ּ���");
		String userName =sc.next();
		
		while(true) {
			System.out.println("�ڵ�����ȣ�� �Է����ּ���(���ڸ������� ��ȣ���Է� : 01012345801)");
			userPhone = sc.next();
			if(!uda.phoneCheck(userPhone))System.out.println("���ڸ� ������ �޴�����ȣ�� �Է����ּ���");
			else break;
		}
		
		while(true) {
			System.out.println("��ȣ�帣�� �������ּ���");
			System.out.println("1. �ѱ��������� \n2. �ؿ�POP���� \n3. �׿��α��帣");
			choiceGenre1=sc.nextInt();
			if (choiceGenre1<4)	break;
			else System.out.println("�߸��Է��ϼ˽��ϴ� .�ٽ��Է��ϼ���");
		}
		String[] userGenre = uda.favoritGenre(choiceGenre1);
		String userGenre1 = userGenre[0];
		String userGenre2 = userGenre[1];

		UserDTO newUser = new UserDTO(userId, userPw, userName, userPhone, userGenre1, userGenre2);
		uda.join(newUser);
	}
}
