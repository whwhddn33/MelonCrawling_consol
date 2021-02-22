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
			System.out.println("사용할 아이디를 입력해주세요.");
			userId =sc.next();
			
			if((uda.idcheck(userId) != 0))System.out.println("아이디가 중복됩니다.다시 입력해주세요");
			else break;
		}
		while(true) {
			System.out.println("비밀번호를 입력해주세요 (4자리이상 사용).");
			userPw =sc.next(); 
			if (!uda.passLengthCheck(userPw))System.out.println("비밀번호가 너무 짧습니다");
			else {
				userPw = uda.get256Hash(userPw);
				break;
			}
		}
		
		System.out.println("성함을 입력해주세요");
		String userName =sc.next();
		
		while(true) {
			System.out.println("핸드폰번호를 입력해주세요(문자를제외한 번호만입력 : 01012345801)");
			userPhone = sc.next();
			if(!uda.phoneCheck(userPhone))System.out.println("문자를 제외한 휴대폰번호를 입력해주세요");
			else break;
		}
		
		while(true) {
			System.out.println("선호장르를 선택해주세요");
			System.out.println("1. 한국대중음악 \n2. 해외POP음악 \n3. 그외인기장르");
			choiceGenre1=sc.nextInt();
			if (choiceGenre1<4)	break;
			else System.out.println("잘못입력하셧습니다 .다시입력하세요");
		}
		String[] userGenre = uda.favoritGenre(choiceGenre1);
		String userGenre1 = userGenre[0];
		String userGenre2 = userGenre[1];

		UserDTO newUser = new UserDTO(userId, userPw, userName, userPhone, userGenre1, userGenre2);
		uda.join(newUser);
	}
}
