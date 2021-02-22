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
			System.out.println("�˻����� �Է����ּ���");
//				searchKeyword = "�ο";
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
	
				
				for (WebElement webElement : lineList) {//���, ��Ƽ��Ʈ, �ٹ����� ����
					songText = webElement.findElement(By.className("fc_gray")).getText();
					artistText = webElement.findElement(By.cssSelector("a.fc_mgray")).getText();
					album = webElement.findElement(By.cssSelector("td:nth-child(5)>div>div>a")).getText();
					System.out.print(cnt+" �� "+songText+"\t\t"+artistText+"\t\t"+album+"\t\t�� : "+mda.steamCnt(songText, artistText, album)+"\n");
					cnt++;
				}
					System.out.println("1. ���纸�� 2. ���ϱ� 3. �ٸ��뷡 �˻� 4. ���� ���ư���");
					cnt = 1;
					choice = sc.nextInt();
					if (choice == 3 || choice == 4) break;
					switch (choice) {
					case 1://���纸��
						System.out.println("����Ʈ�� ��ȣ�� �Է��ϼ���");
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
									System.out.println("��ϵ� ���簡 �����ϴ�.");
								}finally {
								driver.navigate().back();
								}
								break;
							}
						}
						break;
					case 2://���ϱ�
						System.out.println("����Ʈ�� ��ȣ�� �Է��ϼ���");
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
								System.out.print(cnt+" �� "+songText+"\t\t"+artistText+"\t\t"+album+"\n");
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
				System.out.println("�̹� ��Ǿ����ϴ�.");
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
				System.out.println("���Ͽ� ��ϵǾ����ϴ�.");
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
	
	public int getSongIdx() {//getIdIdx() : ���̵� Idx �ο� �޼ҵ�
		sql = "SELECT COUNT(*) FROM steamList"; 								// ��� ��ü �ο� ī��Ʈ ����
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
		sql = "SELECT steamidx FROM steamList order by steamidx";				// useridx��ȣ �������� ���� ����
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
		System.out.println("���� "+"\t\t"+"���"+"\t\t"+"��Ƽ��Ʈ");
		for (WebElement i : steadySellerTrList) {
			String songRank = i.findElement(By.cssSelector("div > span.rank")).getText();
			String songName = i.findElement(By.cssSelector("span > a")).getText();
			String artist = i.findElement(By.cssSelector("div > div > div > a")).getText();
			System.out.println(songRank+"�� "+"\t\t"+songName+"\t\t"+artist);
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
		
		if (gn1.equals("�ѱ���������")) {
			if (gn2.equals("�߶��")) gncode = "0100";
			else if (gn2.equals("��")) gncode = "0200";
			else if (gn2.equals("��/����")) gncode = "0300";
			else if (gn2.equals("R&B/Soul")) gncode = "0400";
			else if (gn2.equals("�ε�����")) gncode = "0500";
			else if (gn2.equals("��/��Ż")) gncode = "0600";
			else if (gn2.equals("Ʈ��Ʈ")) gncode = "0700";
			else if (gn2.equals("��ũ/��罺")) gncode = "0800";
		}else if (gn1.equals("�ؿ�POP����")) {
			if (gn2.equals("POP")) gncode = "0900";
			else if (gn2.equals("��/��Ż")) gncode = "1000";
			else if (gn2.equals("�Ϸ�Ʈ�δ�ī")) gncode = "1100";
			else if (gn2.equals("��/����")) gncode = "1200";
			else if (gn2.equals("R&B/Soul")) gncode = "1300";
			else if (gn2.equals("��ũ/��罺/��Ʈ��")) gncode = "1400";
		}else { 
			if (gn2.equals("OST")) gncode = "1500";
			else if (gn2.equals("Ŭ����")) gncode = "1600";
			else if (gn2.equals("����")) gncode = "1700";
			else if (gn2.equals("��������")) gncode = "1800";
			else if (gn2.equals("J-POP")) gncode = "1900";
			else if (gn2.equals("�������")) gncode = "2000";
			else if (gn2.equals("CCM")) gncode = "2100";
			else if (gn2.equals("���/�±�")) gncode = "2200";
			else if (gn2.equals("��������")) gncode = "2300";
			else if (gn2.equals("����")) gncode = "2400";
		}
		result = "https://www.melon.com/genre/song_list.htm?gnrCode=GN"+gncode+"&steadyYn=Y";
		return result;
	}

}