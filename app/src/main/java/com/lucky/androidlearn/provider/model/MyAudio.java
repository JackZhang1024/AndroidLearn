package com.lucky.androidlearn.provider.model;

import android.graphics.Bitmap;

public class MyAudio {
	private String title , path , artist , album ;
	private long duration ;
	private Bitmap albumCover ;

	public MyAudio(String title, String path, String artist, String album , long duration) {
		super();
		this.title = title;
		this.path = path;
		this.artist = artist;
		this.album = album;
		this.duration = duration ;
	}
	public MyAudio(String title, String path, String artist, String album , long duration , Bitmap albumCover) {
		super();
		this.title = title;
		this.path = path;
		this.artist = artist;
		this.album = album;
		this.duration = duration ;
		this.albumCover = albumCover ;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
	
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public Bitmap getAlbumCover() {
		return albumCover;
	} 
	@Override
	public String toString() {
		return "MyAudio [title=" + title + ", path=" + path + ", artist="
				+ artist + ", album=" + album + ", duration=" + duration
				+ ", albumCover=" + albumCover + "]";
	}

	
}
