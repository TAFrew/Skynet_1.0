package models;

import java.io.Serializable;

public class Square implements Serializable{
	private int _xLocation;
	private int _yLocation;

	private boolean _filled;
	private String _playerThatHasGoneHere;
	
	public Square(int x, int y){
		_xLocation = x;
		_yLocation = y;
		_filled = false;
		_playerThatHasGoneHere = "Nobody";
	}
	
	public boolean isFilled(){
		return _filled;
	}
	
	public void setAsFilled(String player){
		_filled = true;
		_playerThatHasGoneHere = player;
	}
	
	public String filledBy(){
		return _playerThatHasGoneHere;
	}
	
	public boolean isInRow(int row){
		if(_xLocation == row){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isInColumn(int row){
		if(_xLocation == row){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String toString(){
		return _xLocation + "," + _yLocation;
	}
}
