package DAO;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import DTO.UserDTO;

public class UserDAO {
	Connection conn = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;
	String sql = null;
	Scanner sc = new Scanner(System.in);
	public UserDAO() {
		conn = DBConnection.getConnection();
	}
	
	public void join(UserDTO newUser) {//ȸ���������� DB�Է� �޼���
		int newIdx = getIdIdx();
		sql = "INSERT INTO MEMBERS VALUES(?,?,?,?,?,?,?)";
			try {
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, newIdx);
				pstm.setString(2, newUser.getUserId());
				pstm.setString(3, newUser.getUserPw());
				pstm.setString(4, newUser.getUserName());
				pstm.setString(5, newUser.getUserPhone());
				pstm.setString(6, newUser.getUserGenre1());
				pstm.setString(7, newUser.getUserGenre2());
				rs = pstm.executeQuery();
				System.out.println("���ԿϷ�");
			} catch (SQLException e) {
				System.out.println(e);
			}finally {
				try {
					rs.close();
					pstm.close();
				} catch (SQLException e) {
					System.out.println("�� �� ���� ����");
				}
			}
	}
	
	public UserDTO login(String userId,String userPw) {
		sql = "SELECT * FROM MEMBERS WHERE USERID = ? AND USERPW = ?";
		UserDTO user = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, userId);
			pstm.setString(2, userPw);
			rs= pstm.executeQuery();
			
			if(rs.next()) {
			user = new UserDTO(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4) ,rs.getString(5) ,rs.getString(6),rs.getString(7));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("�� �� ���� ����");
			}
		}
		return user;
	}
	
	public int loginCheck(String userId,String userPw) { //�Է¹��� ���̵� �� pw�� ��ġ�ϸ� 1�����Ͽ� �հ�	
		String pwChecker = "";// sql���� �޾ƿ� ��й�ȣ�� ������ ���� ����
		sql = "SELECT USERPW FROM MEMBERS WHERE USERID = ?";// USERID �Է½� PW���� ���
		try {
			pstm = conn.prepareStatement(sql); //sql�� pstm�� ����
			pstm.setString(1, userId);			// sql���� userId ����
			System.out.println("�α��� ���� Ȯ����");	
			rs = pstm.executeQuery();
			if (rs.next()) {					//Ŀ���̵�
				pwChecker = rs.getString(1);	//üĿ�� pw���� ����
				if (pwChecker.equals(userPw)) {		//pwChecker�� userPw�� ������ �α��μ���
					return 1;
				}
			}else System.out.println("���������ʴ� ���̵��Դϴ�.");
		} catch (SQLException e) {
			System.out.println("SQL ����");
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("�� �� ���� ����");
			}
		}
		return 0;
	}
	
	public int idcheck(String userId) {//���̵� �ߺ�üũ
		int result = 0;
		sql = "SELECT COUNT(*) FROM MEMBERS WHERE USERID=?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, userId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("�� �� ���� ����");
			}
		}
		return result;
	}
	
	public boolean passLengthCheck(String userPw) {//�佺���� ���� üũ
		if(userPw.length()<4) {
			return false;
		}else {
		return true;
		}
	}
	
	public boolean phoneCheck(String userPhone) {//phoneCheck() : ���ѹ� ���� üũ
		if(!(userPhone.length() == 10 || userPhone.length() == 11))return false;
		else return true;
	}
	
	public int getIdIdx() {//getIdIdx() : ���̵� Idx �ο� �޼ҵ�
		sql = "SELECT COUNT(*) FROM MEMBERS"; 								// ��� ��ü �ο� ī��Ʈ ����
		int idxcount = 0; 													// idx ���� �ʱ�ȭ
		int result = 0; 													// result ���� �ʱ�ȭ
		int compare = 0;													// compare ���� �ʱ�ȭ
		try {																// ��ȸ���� �޾ƿ���
			pstm = conn.prepareStatement(sql); 								// pstm�� ���� �Է�
			rs = pstm.executeQuery(); 										// ���� ����
			if (rs.next()) {												// Ŀ�� �ű��
				idxcount = rs.getInt(1); 									// idxcount �� ��ü �ο��� ����
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("�� �� ���� ����");
			}
		}
		//(��ȸ���� + 1) ��ŭ Ŀ���̵� + �̵��� �� ���� + idx���� �����Ѱ� ��
		sql = "SELECT useridx FROM MEMBERS order by useridx";				// useridx��ȣ �������� ���� ����
		try {																//
			pstm = conn.prepareStatement(sql);								// pstm�� ���� �Է�
			rs = pstm.executeQuery();										// ���� ����
			for (int i = 0; i < idxcount+4; i++) {							// 0���� idxcount ��ŭ for���ӿ��� �˻�
				if(rs.next()) {												// �������� �������
					compare = rs.getInt(1);									// compare�� ù�ప �Է�
					if (!(i== compare)) {result = i;break;}					// i���� compare�� �ٸ��� result�� i�� ����
				}else result = idxcount+1;									// ���� UserIdx�� 0���� �ְ� ���ȣ����� idxcount�� compare�� ������������ idxcount+1���� result�� ����
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("�� �� ���� ����");
			}
		}
		return result;														// if, for�� ����� result�� ����
	}

	public String get256Hash(String userPw) {//��й�ȣ �ؽð����� ��ȯ
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(userPw.getBytes());
			byte bytes[] = md.digest();
			for (int i = 0; i < bytes.length; i++) {
				result.append(
						Integer.toString((bytes[i] & 0xff) + 0x100,16).substring(1)
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
		}
	
	public String[] favoritGenre(int choiceGenre1) {//�帣���ø޼ҵ�
		String[] result= new String[2];
		int choice2 = 0;
		
		while(true) {
			switch (choiceGenre1) {
			case 1:
				result[0] = "�ѱ���������";
				while(true) {
					System.out.println("�����帣�� �������ּ���");
					System.out.print("1. �߶��\n2. ��\n3. ��/����\n4. R&B/Soul\n"
							+ "5. �ε�����\n6. ��/��Ż\n7. Ʈ��Ʈ\n8. ��ũ/��罺");
					choice2 = sc.nextInt();
					switch (choice2) {
					case 1:
						result[1] = "�߶��";
						break;
					case 2:
						result[1] = "��";
						break;
					case 3:
						result[1] = "��/����";
						break;
					case 4:
						result[1] = "R&B/Soul";
						break;
					case 5:
						result[1] = "�ε�����";
						break;
					case 6:
						result[1] = "��/��Ż";
						break;
					case 7:
						result[1] = "Ʈ��Ʈ";
						break;
					case 8:
						result[1] = "��ũ/��罺";
						break;
					}if (choice2<9)break;
				}
				break;
			case 2:
				result[0] = "�ؿ�POP����";
					while(true) {
						System.out.println("�����帣�� �������ּ���");
						System.out.print("1.POP \n2.��/��Ż \n3.�Ϸ�Ʈ�δ�ī \n4.��/���� \n5.R&B/Soul \n6.��ũ/��罺/��Ʈ��");
						choice2 = sc.nextInt();
						switch (choice2) {
						case 1:
							result[1] = "POP";
							break;
						case 2:
							result[1] = "��/��Ż";
							break;
						case 3:
							result[1] = "�Ϸ�Ʈ�δ�ī";
							break;
						case 4:
							result[1] = "��/����";
							break;
						case 5:
							result[1] = "R&B/Soul";
							break;
						case 6:
							result[1] = "��ũ/��罺/��Ʈ��";
							break;
						}if (choice2<7)break;
					}
					break;
			case 3:
				result[0] = "�׿��α��帣";
				while(true) {
					System.out.println("�����帣�� �������ּ���");
					System.out.println("1.OST 2. Ŭ���� 3. ���� 4. J-POP 5.������� 6.CCM 7.���/�±� 8.�������� 9.����");
					choice2 = sc.nextInt();
					switch (choice2) {
					case 1:
						result[1] = "OST";
						break;
					case 2:
						result[1] = "Ŭ����";
						break;
					case 3:
						result[1] = "����";
						break;
					case 4:
						result[1] = "J-POP";
						break;
					case 5:
						result[1] = "�������";
						break;
					case 6:
						result[1] = "CCM";
						break;
					case 7:
						result[1] = "���/�±�";
						break;
					case 8:
						result[1] = "��������";
						break;
					case 9:
						result[1] = "����";
						break;
					}if (choice2<10)break;
				}
				break;
			}
			break;
		}
		return result;
	}

	public void idModify(String newUserId,int idx) {
		sql = "UPDATE MEMBERS SET USERID = ? where useridx = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, newUserId);
			pstm.setInt(2, idx);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void pwModify(String newUserPw,int idx) {
		sql = "UPDATE MEMBERS SET USERPW = ? where useridx = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, newUserPw);
			pstm.setInt(2, idx);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void phoneModify(String newUserPhone,int idx) {
		sql = "UPDATE MEMBERS SET USERPhone = ? where useridx = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, newUserPhone);
			pstm.setInt(2, idx);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	public void genreModify(String newUserGenre1,String newUserGenre2, int idx) {
		sql = "UPDATE MEMBERS SET USERGENRE1 = ?,USERGENRE2 = ? where useridx = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, newUserGenre1);
			pstm.setString(2, newUserGenre2);
			pstm.setInt(3, idx);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
	
	public void tal(int idx) {
		sql = "DELETE FROM STEAMLIST WHERE USERIDX =?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, idx);
			rs = pstm.executeQuery();
			sql = "DELETE FROM MEMBERS WHERE USERIDX = ?";
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, idx);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
	}
}
	


