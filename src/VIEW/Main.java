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
			System.out.println("�α����� �̿����ּ���");
			break;
		}else {
			System.out.println("���������� ����");
		}
			System.out.println("=======================MelonCrawling=====================");
			System.out.println("       1.���ǰ˻�                       2.����                    3.��ȣ�帣 ���׵𼿷�");
			System.out.println("\n");
			System.out.println("             4.ȸ����������                     5.�α׾ƿ�          ");
			System.out.println("========================================================");
			choice = sc.nextInt();
			if (choice == 5) { 
				System.out.println("�α׾ƿ��մϴ�.");
				Session.put("session_idx", null);
				break;
			}
				switch (choice) {
				case 1:
					System.out.println("���ǰ˻����� �̵��մϴ�.");
					MelonDAO.MelonSearch();
					break;
				case 2:
					System.out.println("�������� �̵��մϴ�");
					
					Integer sessionidx = new Integer(Session.get("session_idx"));
					int cnt = 0;
					for (SongDTO i : mda.getSteamList(sessionidx)) {
						cnt++;
						System.out.println(cnt+" "+i);
					}
					break;
				case 3 :
					System.out.println("��ȣ�帣 ���׵𼿷��� �̵��մϴ�.");
					mda.showSteadySellerList();
					break;
				case 4 :
					System.out.println("ȸ�������� �����մϴ�.");
					new modifyView();
					break;
				}
		}
	}
}
