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
	
	public void join(UserDTO newUser) {//회원가입정보 DB입력 메서드
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
				System.out.println("가입완료");
			} catch (SQLException e) {
				System.out.println(e);
			}finally {
				try {
					rs.close();
					pstm.close();
				} catch (SQLException e) {
					System.out.println("알 수 없는 오류");
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
				System.out.println("알 수 없는 오류");
			}
		}
		return user;
	}
	
	public int loginCheck(String userId,String userPw) { //입력받은 아이디 와 pw가 일치하면 1리턴하여 합격	
		String pwChecker = "";// sql에서 받아온 비밀번호를 저장할 공간 생성
		sql = "SELECT USERPW FROM MEMBERS WHERE USERID = ?";// USERID 입력시 PW정보 출력
		try {
			pstm = conn.prepareStatement(sql); //sql문 pstm에 저장
			pstm.setString(1, userId);			// sql문에 userId 전달
			System.out.println("로그인 정보 확인중");	
			rs = pstm.executeQuery();
			if (rs.next()) {					//커서이동
				pwChecker = rs.getString(1);	//체커에 pw정보 저장
				if (pwChecker.equals(userPw)) {		//pwChecker와 userPw가 같으면 로그인성공
					return 1;
				}
			}else System.out.println("존재하지않는 아이디입니다.");
		} catch (SQLException e) {
			System.out.println("SQL 오류");
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("알 수 없는 오류");
			}
		}
		return 0;
	}
	
	public int idcheck(String userId) {//아이디 중복체크
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
				System.out.println("알 수 없는 오류");
			}
		}
		return result;
	}
	
	public boolean passLengthCheck(String userPw) {//페스워드 길이 체크
		if(userPw.length()<4) {
			return false;
		}else {
		return true;
		}
	}
	
	public boolean phoneCheck(String userPhone) {//phoneCheck() : 폰넘버 길이 체크
		if(!(userPhone.length() == 10 || userPhone.length() == 11))return false;
		else return true;
	}
	
	public int getIdIdx() {//getIdIdx() : 아이디 Idx 부여 메소드
		sql = "SELECT COUNT(*) FROM MEMBERS"; 								// 멤버 전체 인원 카운트 쿼리
		int idxcount = 0; 													// idx 선언 초기화
		int result = 0; 													// result 선언 초기화
		int compare = 0;													// compare 선언 초기화
		try {																// 총회원수 받아오기
			pstm = conn.prepareStatement(sql); 								// pstm에 쿼리 입력
			rs = pstm.executeQuery(); 										// 쿼리 실행
			if (rs.next()) {												// 커서 옮기기
				idxcount = rs.getInt(1); 									// idxcount 에 전체 인원수 저장
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("알 수 없는 오류");
			}
		}
		//(총회원수 + 1) 만큼 커서이동 + 이동된 값 저장 + idx값과 저장한값 비교
		sql = "SELECT useridx FROM MEMBERS order by useridx";				// useridx번호 오름차순 정렬 쿼리
		try {																//
			pstm = conn.prepareStatement(sql);								// pstm에 쿼리 입력
			rs = pstm.executeQuery();										// 쿼리 실행
			for (int i = 0; i < idxcount+4; i++) {							// 0부터 idxcount 만큼 for문속에서 검사
				if(rs.next()) {												// 다음행이 있을경우
					compare = rs.getInt(1);									// compare에 첫행값 입력
					if (!(i== compare)) {result = i;break;}					// i번과 compare가 다르면 result에 i값 저장
				}else result = idxcount+1;									// 만약 UserIdx에 0값이 있고 빈번호가없어서 idxcount와 compare가 끝까지같으면 idxcount+1값을 result에 저장
			}
		} catch (SQLException e) {
			System.out.println(e);
		}finally {
			try {
				rs.close();
				pstm.close();
			} catch (SQLException e) {
				System.out.println("알 수 없는 오류");
			}
		}
		return result;														// if, for문 통과한 result값 리턴
	}

	public String get256Hash(String userPw) {//비밀번호 해시값으로 변환
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
	
	public String[] favoritGenre(int choiceGenre1) {//장르선택메소드
		String[] result= new String[2];
		int choice2 = 0;
		
		while(true) {
			switch (choiceGenre1) {
			case 1:
				result[0] = "한국대중음악";
				while(true) {
					System.out.println("세부장르를 선택해주세요");
					System.out.print("1. 발라드\n2. 댄스\n3. 랩/힙합\n4. R&B/Soul\n"
							+ "5. 인디음악\n6. 록/메탈\n7. 트로트\n8. 포크/블루스");
					choice2 = sc.nextInt();
					switch (choice2) {
					case 1:
						result[1] = "발라드";
						break;
					case 2:
						result[1] = "댄스";
						break;
					case 3:
						result[1] = "랩/힙합";
						break;
					case 4:
						result[1] = "R&B/Soul";
						break;
					case 5:
						result[1] = "인디음악";
						break;
					case 6:
						result[1] = "록/메탈";
						break;
					case 7:
						result[1] = "트로트";
						break;
					case 8:
						result[1] = "포크/블루스";
						break;
					}if (choice2<9)break;
				}
				break;
			case 2:
				result[0] = "해외POP음악";
					while(true) {
						System.out.println("세부장르를 선택해주세요");
						System.out.print("1.POP \n2.록/메탈 \n3.일렉트로니카 \n4.랩/힙합 \n5.R&B/Soul \n6.포크/블루스/컨트리");
						choice2 = sc.nextInt();
						switch (choice2) {
						case 1:
							result[1] = "POP";
							break;
						case 2:
							result[1] = "록/메탈";
							break;
						case 3:
							result[1] = "일렉트로니카";
							break;
						case 4:
							result[1] = "랩/힙합";
							break;
						case 5:
							result[1] = "R&B/Soul";
							break;
						case 6:
							result[1] = "포크/블루스/컨트리";
							break;
						}if (choice2<7)break;
					}
					break;
			case 3:
				result[0] = "그외인기장르";
				while(true) {
					System.out.println("세부장르를 선택해주세요");
					System.out.println("1.OST 2. 클래식 3. 재즈 4. J-POP 5.월드뮤직 6.CCM 7.어린이/태교 8.종교음악 9.국악");
					choice2 = sc.nextInt();
					switch (choice2) {
					case 1:
						result[1] = "OST";
						break;
					case 2:
						result[1] = "클래식";
						break;
					case 3:
						result[1] = "재즈";
						break;
					case 4:
						result[1] = "J-POP";
						break;
					case 5:
						result[1] = "월드뮤직";
						break;
					case 6:
						result[1] = "CCM";
						break;
					case 7:
						result[1] = "어린이/태교";
						break;
					case 8:
						result[1] = "종교음악";
						break;
					case 9:
						result[1] = "국악";
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
	


