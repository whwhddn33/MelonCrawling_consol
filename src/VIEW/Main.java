package VIEW;

import java.util.ArrayList;
import java.util.Scanner;

import DAO.MelonDAO;
import DAO.Session;
import DAO.UserDAO;
import DTO.SongDTO;

public class Main {
	public Main() {
		Scanner sc = new Scanner(System.in);
		MelonDAO mda = new MelonDAO();
		UserDAO uda = new UserDAO();
		int choice = 0;
		while(true) {
		if(Session.get("session_idx") == null) {
			System.out.println("로그인후 이용해주세요");
			break;
		}else {
			System.out.println("메인페이지 접속");
		}
			System.out.println("=======================MelonCrawling=====================");
			System.out.println("       1.음악검색                       2.찜목록                    3.선호장르 스테디셀러");
			System.out.println("\n");
			System.out.println("             4.회원정보수정                     5.로그아웃          ");
			System.out.println("========================================================");
			choice = sc.nextInt();
			if (choice == 5) { 
				System.out.println("로그아웃합니다.");
				Session.put("session_idx", null);
				break;
			}
				switch (choice) {
				case 1:
					System.out.println("음악검색으로 이동합니다.");
					MelonDAO.MelonSearch();
					break;
				case 2:
					System.out.println("찜목록으로 이동합니다");
					
					Integer sessionidx = new Integer(Session.get("session_idx"));
					int cnt = 0;
					for (SongDTO i : mda.getSteamList(sessionidx)) {
						cnt++;
						System.out.println(cnt+" "+i);
					}
					break;
				case 3 :
					System.out.println("선호장르 스테디셀러로 이동합니다.");
					mda.showSteadySellerList();
					break;
				case 4 :
					System.out.println("회원정보를 수정합니다.");
					new modifyView();
					break;
				}
		}
	}
}
