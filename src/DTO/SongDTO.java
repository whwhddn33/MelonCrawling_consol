package DTO;

public class SongDTO {
	private int steamIdx;
	private int userIdx;
	private String songName;
	private String artist;
	private String album;
	private String songLink;

	
	public SongDTO() {}

	public SongDTO(String songName, String artist, String album, String songLink) {
		this.songName = songName;
		this.artist = artist;
		this.album = album;
		this.songLink = songLink;
	}
	
	
	
	public SongDTO(String songName, String artist, String album) {
		this.songName = songName;
		this.artist = artist;
		this.album = album;
	}

	public int getSteamIdx() {
		return steamIdx;
	}
	public int getUserIdx() {
		return userIdx;
	}
	public String getSongName() {
		return songName;
	}
	public String getArtist() {
		return artist;
	}
	public String getAlbum() {
		return album;
	}
	public String getSongLink() {
		return songLink;
	}

	public boolean equals(Object obj) {
		if (obj instanceof SongDTO) {
			SongDTO target= (SongDTO)obj;
			if(target.equals(obj)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return songName+" "+artist+" "+album;
	}
}
