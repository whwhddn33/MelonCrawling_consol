package VIEW;

import java.util.Scanner;

import DAO.Session;
import DAO.UserDAO;

public class modifyView {
	public modifyView() {
		UserDAO uda = new UserDAO();
		Scanner sc = new Scanner(System.in);
		int choice;
		while(true) {
		System.out.println(" 1.���̵� ����\n 2.��й�ȣ ����\n 3.����ó ����\n 4.��ȣ�帣 ����\n 5.Ż���ϱ� 6.�ڷΰ���");
		choice = sc.nextInt();
			if (choice==1) {
				System.out.println("���̵� �����մϴ�.\n�����Ͻ� ���̵� �Է����ּ���\n ���̵� �Է� : ");
				String newUserId = sc.next();
				if (uda.idcheck(newUserId) == 0) {
					uda.idModify(newUserId, Session.get("session_idx"));
					System.out.println("����Ϸ�Ǿ����ϴ�.");
					System.out.println("�ٽ� �α������ּ���");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
				}else {
					System.out.println("�ߺ��� ���̵��Դϴ�.");
				}
			}else if(choice == 2) {
				System.out.println("��й�ȣ�� �����մϴ�.\n ���̵�� ��й�ȣ�� �Է����ּ���\n ���̵� �Է� : ");
				String userId = sc.next();
				System.out.println("��й�ȣ�� �Է����ּ���");
				String userPw = sc.next();
				if (uda.loginCheck(userId, uda.get256Hash(userPw)) == 1) {
					System.out.println("�����Ǿ����ϴ�.");
					System.out.println("������ ��й�ȣ�� �Է����ּ���");
					String newUserPw = uda.get256Hash(sc.next());
					uda.pwModify(newUserPw, Session.get("session_idx"));
					System.out.println("����Ǿ����ϴ�.");
					System.out.println("�ٽ� �α������ּ���");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
					
				}else System.out.println("���̵� ��й�ȣ�� Ȯ�����ּ���");
				
			}else if(choice == 3) {
				System.out.println("����ó�� �����մϴ�. \n ������ ����ó�� �Է����ּ��� : ");
				String newUserPhone = sc.next();
				if (uda.phoneCheck(newUserPhone)) {
					uda.phoneModify(newUserPhone, Session.get("session_idx"));
					System.out.println("����Ǿ����ϴ�.");
					break;
				}else System.out.println("�޴�����ȣ�� Ȯ�����ּ���  (ex.01011112222)");
				
			}else if(choice == 4) {
				int choiceGenre1;
				while(true) {
					System.out.println("��ȣ�帣�� �����մϴ�. \n �帣�� �������ּ���");
					System.out.println("1. �ѱ��������� \n2. �ؿ�POP���� \n3. �׿��α��帣");
					choiceGenre1 = sc.nextInt();
					if (choiceGenre1<4)	break;
					else System.out.println("�߸��Է��ϼ˽��ϴ� .�ٽ��Է��ϼ���");
				}
				String[] userGenre = uda.favoritGenre(choiceGenre1);
				String newUserGenre1 = userGenre[0];
				String newUserGenre2 = userGenre[1];
				uda.genreModify(newUserGenre1, newUserGenre2, Session.get("session_idx"));
				System.out.println("����Ǿ����ϴ�.");
				break;
			}
			
			else if(choice == 5) {
				System.out.println("Ż���Ͻðڽ��ϱ�.(y,n)");
				String question = sc.next().toLowerCase();
				if (question.equals("y")) {
					System.out.println("Ż�� �����մϴ�.");
					uda.tal(Session.get("session_idx"));
					System.out.println("Ż�� �Ϸ�Ǿ����ϴ�.");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
				}
				else break;
			}
			else if(choice == 6) break;
			else System.out.println("�߸��Է��Ͽ����ϴ�. �ٽ� �Է����ּ���");
		}
	}
}
