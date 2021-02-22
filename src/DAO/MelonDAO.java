package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import DTO.SongDTO;
import DTO.UserDTO;

public class MelonDAO {
	public static String id = "webdriver.chrome.driver";
	public static String path = "C:/chromedriver.exe";
	
	Connection conn = null;
	PreparedStatement pstm = null;
	ResultSet rs = null;
	String sql = null;
	Scanner sc = new Scanner(System.in);
	static MelonDAO mda = new MelonDAO();
	public MelonDAO() {
		conn = DBConnection.getConnection();
	}
	
	public static void MelonSearch() {
		Scanner sc = new Scanner(System.in);
		System.setProperty(id, path);
		ChromeOptions options = new ChromeOptions();
		String url = "https://www.melon.com/index.htm";
		options.setCapability("ignoreProtectedModeSettings", true);
		options.addArguments("headless");
		WebDriver driver = new ChromeDriver(options);
//			------------------------------------------------
		
		String searchKeyword;
		int choice;
		
			while(true) {
			driver.get(url);
			WebElement searchInput = driver.findElement(By.id("top_search"));
			searchInput.click();
			System.out.println("검색명을 입력해주세요");
//				searchKeyword = "싸운날";
				searchKeyword = sc.next();
			searchInput.sendKeys(searchKeyword);
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			WebElement searchBtn = driver.findElement(By.className("search_m"));
			searchBtn.click();
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			int cnt = 1;

			while(true) {
				int choice2 = 0;
				int cnt2  = 0;
				String songText;
				String artistText;
				String album;
				WebElement musicInfo = driver.findElement(By.tagName("tbody"));
				
				List<WebElement> lineList = musicInfo.findElements(By.tagName("tr"));
	
				
				for (WebElement webElement : lineList) {//재목, 아티스트, 앨범별로 저장
					songText = webElement.findElement(By.className("fc_gray")).getText();
					artistText = webElement.findElement(By.cssSelector("a.fc_mgray")).getText();
					album = webElement.findElement(By.cssSelector("td:nth-child(5)>div>div>a")).getText();
					System.out.print(cnt+" 번 "+songText+"\t\t"+artistText+"\t\t"+album+"\t\t찜 : "+mda.steamCnt(songText, artistText, album)+"\n");
					cnt++;
				}
					System.out.println("1. 가사보기 2. 찜하기 3. 다른노래 검색 4. 메인 돌아가기");
					cnt = 1;
					choice = sc.nextInt();
					if (choice == 3 || choice == 4) break;
					switch (choice) {
					case 1://가사보기
						System.out.println("리스트중 번호를 입력하세요");
						choice2 = sc.nextInt();
						cnt2 = 0;
						for (WebElement webElement : lineList) {
							cnt2++;
							if (choice2 == cnt2) {							
								try {Thread.sleep(2000);} catch (InterruptedException e) {}
								webElement.findElement(By.className("btn_icon_detail")).click();
								try {
									WebElement lyricsDiv = driver.findElement(By.id("d_video_summary"));
									System.out.println(lyricsDiv.getText());
								} catch (Exception e) {
									System.out.println("등록된 가사가 없습니다.");
								}finally {
								driver.navigate().back();
								}
								break;
							}
						}
						break;
					case 2://찜하기
						System.out.println("리스트중 번호를 입력하세요");
						choice2 = sc.nextInt();
						cnt2 = 0;
						for (WebElement webElement : lineList) {
							cnt2++;
							if (choice2 == cnt2) {							
								try {Thread.sleep(2000);} catch (InterruptedException e) {}
								songText = webElement.findElement(By.className("fc_gray")).getText();
								artistText = webElement.findElement(By.cssSelector("a.fc_mgray")).getText();
								album = webElement.findElement(By.cssSelector("td:nth-child(5)>div>div>a")).getText();
								webElement.findElement(By.className("btn_icon_detail")).click();
								String link = driver.getCurrentUrl();
//								System.out.println(link);
								System.out.print(cnt+" 번 "+songText+"\t\t"+artistText+"\t\t"+album+"\n");
								mda.steamList(songText, artistText, album, link);
								driver.navigate().back();
								break;
							}
						}
					}
				}if (choice == 4) {
					driver.close();
					break;
				}
			}
	}
	
	public void steamList(String songText, String artistText, String album,String link) {
		int check = 0;
		int getidx = 0;
		getidx = mda.getSongIdx();
		sql = "SELECT * FROM STEAMLIST WHERE USERIDX = ? AND SONGLINK = ?";
			try {
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, Session.get("session_idx"));
				pstm.setString(2, link);
				rs = pstm.executeQuery();
				if (rs.next()) check = 1;
			}catch (SQLException e){
				System.out.println(e);
			}finally {
				try {
					rs.close();
					pstm.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
			if (check == 1) {
				System.out.println("이미 찜되었습니다.");
			}else {
			sql = "INSERT INTO STEAMLIST VALUES (?,?,?,?,?,?)";
			try {
				pstm = conn.prepareStatement(sql);
				pstm.setInt(1, getidx);
				pstm.setInt(2,Session.get("session_idx"));
				pstm.setString(3, songText);
				pstm.setString(4, artistText);
				pstm.setString(5, album);
				pstm.setString(6, link);			
				rs = pstm.executeQuery();
				System.out.println("찜목록에 등록되었습니다.");
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
	
	public int getSongIdx() {//getIdIdx() : 아이디 Idx 부여 메소드
		sql = "SELECT COUNT(*) FROM steamList"; 								// 멤버 전체 인원 카운트 쿼리
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
		sql = "SELECT steamidx FROM steamList order by steamidx";				// useridx번호 오름차순 정렬 쿼리
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
	
	public ArrayList<SongDTO> getSteamList(Integer sessionidx) {
		int idx = sessionidx;
		ArrayList<SongDTO> steamlist = new ArrayList<>();
		sql = "SELECT * FROM STEAMLIST WHERE USERIDX = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1,idx);
			rs = pstm.executeQuery();
			while(rs.next()) {
					steamlist.add(new SongDTO(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
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
		
		return steamlist;
	}
	
	public int steamCnt(String songText,String artistText,String album) {
		int result = 0;
		sql = "select count(*) from steamlist where songname = ? and artist = ? and album = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, songText);
			pstm.setString(2, artistText);
			pstm.setString(3, album);
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
				System.out.println(e);
			}
		}
		return result;
	}

	public void showSteadySellerList() {
		Scanner sc = new Scanner(System.in);
		System.setProperty(id, path);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		String url = mda.userSteadySellerlink(Session.get("session_idx"));
		options.setCapability("ignoreProtectedModeSettings", true);
		WebDriver driver = new ChromeDriver(options);
		driver.get(url);
		WebElement steadySellerTbody = driver.findElement(By.className("d_song_list"));
		List<WebElement> steadySellerTrList = steadySellerTbody.findElements(By.cssSelector("table>tbody>tr"));
		System.out.println("순위 "+"\t\t"+"곡명"+"\t\t"+"아티스트");
		for (WebElement i : steadySellerTrList) {
			String songRank = i.findElement(By.cssSelector("div > span.rank")).getText();
			String songName = i.findElement(By.cssSelector("span > a")).getText();
			String artist = i.findElement(By.cssSelector("div > div > div > a")).getText();
			System.out.println(songRank+"위 "+"\t\t"+songName+"\t\t"+artist);
		}
		driver.close();
	}
	
	public String userSteadySellerlink(int sessionidx) {
		sql = "select usergenre1,usergenre2 from members where useridx = ?";
		String gn1 = "";
		String gn2 = "";
		String gncode = "";
		String result = "";
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, sessionidx);
			rs = pstm.executeQuery();
			if (rs.next()) {
				gn1 = rs.getString(1);
				gn2 = rs.getString(2);
			}
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
		
		if (gn1.equals("한국대중음악")) {
			if (gn2.equals("발라드")) gncode = "0100";
			else if (gn2.equals("댄스")) gncode = "0200";
			else if (gn2.equals("랩/힙합")) gncode = "0300";
			else if (gn2.equals("R&B/Soul")) gncode = "0400";
			else if (gn2.equals("인디음악")) gncode = "0500";
			else if (gn2.equals("록/메탈")) gncode = "0600";
			else if (gn2.equals("트로트")) gncode = "0700";
			else if (gn2.equals("포크/블루스")) gncode = "0800";
		}else if (gn1.equals("해외POP음악")) {
			if (gn2.equals("POP")) gncode = "0900";
			else if (gn2.equals("록/메탈")) gncode = "1000";
			else if (gn2.equals("일렉트로니카")) gncode = "1100";
			else if (gn2.equals("랩/힙합")) gncode = "1200";
			else if (gn2.equals("R&B/Soul")) gncode = "1300";
			else if (gn2.equals("포크/블루스/컨트리")) gncode = "1400";
		}else { 
			if (gn2.equals("OST")) gncode = "1500";
			else if (gn2.equals("클래식")) gncode = "1600";
			else if (gn2.equals("재즈")) gncode = "1700";
			else if (gn2.equals("뉴에이지")) gncode = "1800";
			else if (gn2.equals("J-POP")) gncode = "1900";
			else if (gn2.equals("월드뮤직")) gncode = "2000";
			else if (gn2.equals("CCM")) gncode = "2100";
			else if (gn2.equals("어린이/태교")) gncode = "2200";
			else if (gn2.equals("종교음악")) gncode = "2300";
			else if (gn2.equals("국악")) gncode = "2400";
		}
		result = "https://www.melon.com/genre/song_list.htm?gnrCode=GN"+gncode+"&steadyYn=Y";
		return result;
	}

}