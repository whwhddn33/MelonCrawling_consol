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
		System.out.println(" 1.아이디 변경\n 2.비밀번호 변경\n 3.연락처 변경\n 4.선호장르 변경\n 5.탈퇴하기 6.뒤로가기");
		choice = sc.nextInt();
			if (choice==1) {
				System.out.println("아이디를 변경합니다.\n변경하실 아이디를 입력해주세요\n 아이디 입력 : ");
				String newUserId = sc.next();
				if (uda.idcheck(newUserId) == 0) {
					uda.idModify(newUserId, Session.get("session_idx"));
					System.out.println("변경완료되었습니다.");
					System.out.println("다시 로그인해주세요");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
				}else {
					System.out.println("중복된 아이디입니다.");
				}
			}else if(choice == 2) {
				System.out.println("비밀번호를 변경합니다.\n 아이디와 비밀번호를 입력해주세요\n 아이디 입력 : ");
				String userId = sc.next();
				System.out.println("비밀번호를 입력해주세요");
				String userPw = sc.next();
				if (uda.loginCheck(userId, uda.get256Hash(userPw)) == 1) {
					System.out.println("인증되었습니다.");
					System.out.println("변경할 비밀번호를 입력해주세요");
					String newUserPw = uda.get256Hash(sc.next());
					uda.pwModify(newUserPw, Session.get("session_idx"));
					System.out.println("변경되었습니다.");
					System.out.println("다시 로그인해주세요");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
					
				}else System.out.println("아이디 비밀번호를 확인해주세요");
				
			}else if(choice == 3) {
				System.out.println("연락처를 변경합니다. \n 변경할 연락처를 입력해주세요 : ");
				String newUserPhone = sc.next();
				if (uda.phoneCheck(newUserPhone)) {
					uda.phoneModify(newUserPhone, Session.get("session_idx"));
					System.out.println("변경되었습니다.");
					break;
				}else System.out.println("휴대폰번호를 확인해주세요  (ex.01011112222)");
				
			}else if(choice == 4) {
				int choiceGenre1;
				while(true) {
					System.out.println("선호장르를 변경합니다. \n 장르를 선택해주세요");
					System.out.println("1. 한국대중음악 \n2. 해외POP음악 \n3. 그외인기장르");
					choiceGenre1 = sc.nextInt();
					if (choiceGenre1<4)	break;
					else System.out.println("잘못입력하셧습니다 .다시입력하세요");
				}
				String[] userGenre = uda.favoritGenre(choiceGenre1);
				String newUserGenre1 = userGenre[0];
				String newUserGenre2 = userGenre[1];
				uda.genreModify(newUserGenre1, newUserGenre2, Session.get("session_idx"));
				System.out.println("변경되었습니다.");
				break;
			}
			
			else if(choice == 5) {
				System.out.println("탈퇴하시겠습니까.(y,n)");
				String question = sc.next().toLowerCase();
				if (question.equals("y")) {
					System.out.println("탈퇴를 진행합니다.");
					uda.tal(Session.get("session_idx"));
					System.out.println("탈퇴가 완료되었습니다.");
					Session.put("session_idx", null);
					System.out.println(Session.get("session_idx"));
					break;
				}
				else break;
			}
			else if(choice == 6) break;
			else System.out.println("잘못입력하였습니다. 다시 입력해주세요");
		}
	}
}
