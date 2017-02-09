import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable{
	private ArrayList<Move> _moves;
	private String _result;
	
	public Game(){
		_moves = new ArrayList<>();
		_result = "";
	}
	
	public void addMove(Move move){
		_moves.add(move);
	}
	
	public void setResult(String result){
		_result = result;
	}
	
	public String toString(){
		String output = "";
		for(Move m : _moves){
			output += "\n" + m.toString();
		}
		return _moves.size() + " moves, \n" +output + "\n" + _result;
	}
	
	public ArrayList<Move> getMoves(){
		return _moves;
	}
	
	public String getResult(){
		return _result;
	}

	public boolean hasSameMoves(Game game) {
		for(Move m : this.getMoves()){
			boolean contains = false;
			for(Move historyMove : game.getMoves()){
				if(m.isSameMove(historyMove)){
					contains = true;
				}
			}
			if(contains == false){
				return false;
			}
		}
		return true;
	}
}
