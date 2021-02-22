package VIEW;

import java.util.Scanner;

import DAO.UserDAO;


public class Index {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("=============멜론크롤링==============");
			System.out.println("1. 로그인\n2. 회원가입\n3. 프로그램 종료\n===============================");
			int FVSelect = sc.nextInt();
			switch (FVSelect) {
				case 1:
					System.out.println("로그인페이지로 이동합니다.\n================login===============\n");
					new LoginView();
					break;
				case 2:
					System.out.println("회원가입페이지로 이동합니다.\n================Join===============\n");
					new JoinView();
					break;
				case 3:
					System.out.println("========프로그램 종료========"); 
					break;
			}if(FVSelect == 3) break;
		}
	}
}


//1. 회원가입
//(가입 시 선호장르 선택받기)
//2. 로그인
//     -----메인------
//1. 음악검색
//(검색 결과 자세히 보여주기)
//(찜 횟수는 본인의 프로그램 사용자들의
//찜 횟수를 말합니다)
//아티스트 - 제목 - 앨범 - 찜 횟수
//1. 가사보기
//2. 메인메뉴로
//2. 찜 목록
//3. 선호장르 스테디셀러 보기
//(멜론 사이트의 장르음악 들어가시면
//볼 수 있습니다)
//4. 회원 정보 수정
//탈퇴, 장르 수정
