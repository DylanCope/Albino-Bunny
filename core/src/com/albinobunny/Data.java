package com.albinobunny;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Data implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int scores[] = {0, 0, 0, 0, 0};
	private int lastScore = 0;
	private int gamesPlayed = 0;
	private int totalScore = 0;
	private float secondsPlayed = 0;
	
	public static void saveData(Data data) throws IOException
	{
		FileHandle file = Gdx.files.local("data.dat");
		OutputStream out = null;
		try{
			file.writeBytes(serialize(data), false);
		}catch(Exception ex){
			System.out.println(ex.toString());
		}finally{
			if(out != null) try{out.close();} catch(Exception ex){}
		}
	}
	
	public static Data readData() throws IOException, ClassNotFoundException
	{
		Data data = null;
		FileHandle file = Gdx.files.local("data.dat");
		data = (Data) deserialize(file.readBytes());
		return data;
	}

	public static byte[] serialize(Object obj) throws IOException
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}
	
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream b = new ByteArrayInputStream(bytes); 
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
	
	
	public int getScore(int i) {
		return scores[i];
	}
	
	public int getLastScore() {
		return lastScore;
	}
	
	public void setLastScore(int score) {
		lastScore = score;
	}
	
	public void addScore(int score, float playTime) {
		lastScore = score;
		totalScore += score;
		gamesPlayed++;
		secondsPlayed += playTime / 1000;
		if (score >= scores[0]){
			scores[4] = scores[3];
			scores[3] = scores[2];
			scores[2] = scores[1];
			scores[1] = scores[0];
			scores[0] = score;
		}
		else if (score >= scores[1]){
			scores[4] = scores[3];
			scores[3] = scores[2];
			scores[2] = scores[1];
			scores[1] = score;
		}
		else if (score >= scores[2]){
			scores[4] = scores[3];
			scores[3] = scores[2];
			scores[2] = score;
		}
		else if (score >= scores[3]){
			scores[4] = scores[3];
			scores[3] = score;
		}
		else if (score > scores[4]){
			scores[4] = score;
		}
	}

	
	public int getGamesPlayed() {
		return gamesPlayed;
	}

	
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	
	public int getTotalScore() {
		return totalScore;
	}

	
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	
	public float getSecondsPlayed() {
		return secondsPlayed;
	}

	
	public void setSecondsPlayed(float secondsPlayed) {
		this.secondsPlayed = secondsPlayed;
	}
	
}