package VIEW;

import java.util.Scanner;

import DAO.UserDAO;


public class Index {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("=============���ũ�Ѹ�==============");
			System.out.println("1. �α���\n2. ȸ������\n3. ���α׷� ����\n===============================");
			int FVSelect = sc.nextInt();
			switch (FVSelect) {
				case 1:
					System.out.println("�α����������� �̵��մϴ�.\n================login===============\n");
					new LoginView();
					break;
				case 2:
					System.out.println("ȸ�������������� �̵��մϴ�.\n================Join===============\n");
					new JoinView();
					break;
				case 3:
					System.out.println("========���α׷� ����========"); 
					break;
			}if(FVSelect == 3) break;
		}
	}
}


//1. ȸ������
//(���� �� ��ȣ�帣 ���ùޱ�)
//2. �α���
//     -----����------
//1. ���ǰ˻�
//(�˻� ��� �ڼ��� �����ֱ�)
//(�� Ƚ���� ������ ���α׷� ����ڵ���
//�� Ƚ���� ���մϴ�)
//��Ƽ��Ʈ - ���� - �ٹ� - �� Ƚ��
//1. ���纸��
//2. ���θ޴���
//2. �� ���
//3. ��ȣ�帣 ���׵𼿷� ����
//(��� ����Ʈ�� �帣���� ���ø�
//�� �� �ֽ��ϴ�)
//4. ȸ�� ���� ����
//Ż��, �帣 ����
