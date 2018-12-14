package com.bjsxt.puzzle;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name=null;
	private int moveCount=0;
	private int rank =0;
	private Date scoreDate =null;
	private int gameLevel =0;
	private long gameTime=0;

	public User(String name, int moveCount, Date scoreDate, int gameLevel,long gameTime) {
		super();
		this.name = name;
		this.moveCount = moveCount;
		this.scoreDate = scoreDate;
		this.gameLevel= gameLevel;
		this.gameTime = gameTime;
	}


	public User() {
		super();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (gameLevel != other.gameLevel)
			return false;
		if (gameTime != other.gameTime)
			return false;
		if (moveCount != other.moveCount)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "User [name=" + name + ", moveCount=" + moveCount + ", gameLevel="
				+ gameLevel + ", gameTime=" + gameTime + ", scoreDate=" + scoreDate + "]";
	}
	public int getMoveCount() {
		return moveCount;
	}
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public Date getScoreDate() {
		return scoreDate;
	}
	public void setScoreDate(Date scoreDate) {
		this.scoreDate = scoreDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}
	public long getGameTime() {
		return gameTime;
	}
	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
}
