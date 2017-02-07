import java.io.Serializable;

import models.Square;

public class Move implements Serializable{
	private Square _square;
	private String _player;
	private int _moveNumber;
	
	public Move(Square square, String player, int move){
		_square = square;
		_player = player;
		_moveNumber = move;
	}
	
	public String toString(){
		return _square.toString() + ": " +_player;
	}
}
