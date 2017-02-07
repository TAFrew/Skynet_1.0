package models;

import java.util.ArrayList;
import java.util.Arrays;

public class NoughtsAndCrossesBoard {
	private ArrayList<Square> _squares;

	private ArrayList<Square> _possibleSquares;

	public NoughtsAndCrossesBoard(){
		_squares = new ArrayList<>(Arrays.asList(
				new Square[]{
						new Square(0,0),
						new Square(0,1),
						new Square(0,2),
						new Square(1,0),
						new Square(1,1),
						new Square(1,2),
						new Square(2,0),
						new Square(2,1),
						new Square(2,2)
				}
				));
		_possibleSquares = new ArrayList<>();
		for(Square s : _squares){
			_possibleSquares.add(s);
		}
	}

	public boolean isFull(){
		for(Square s : _squares){
			if(!(s.isFilled())){
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<Square> getPossibleMoves(){
		return _possibleSquares;
	}

	public void removeFromPossibleMoves(Square toGo) {
		_possibleSquares.remove(toGo);
	}

	public String getResult() {
		ArrayList<Square> row1 = getRow(0);
		ArrayList<Square> row2 = getRow(1);
		ArrayList<Square> row3 = getRow(2);
		
		ArrayList<Square> col1 = getCol(0);
		ArrayList<Square> col2 = getCol(1);
		ArrayList<Square> col3 = getCol(2);
		
		ArrayList<Square> diag1 = getDiag(0);
		ArrayList<Square> diag2 = getDiag(1);
		if(isSelectedByPlayerOne(row1) || isSelectedByPlayerOne(row2) || isSelectedByPlayerOne(row3) || isSelectedByPlayerOne(col1) || isSelectedByPlayerOne(col2) || isSelectedByPlayerOne(col3) || isSelectedByPlayerOne(diag1) || isSelectedByPlayerOne(diag2)){
			return "Player 1 Won";
		}
		else if(isSelectedByPlayerTwo(row1) || isSelectedByPlayerTwo(row2) || isSelectedByPlayerTwo(row3) || isSelectedByPlayerTwo(col1) || isSelectedByPlayerTwo(col2) || isSelectedByPlayerTwo(col3) || isSelectedByPlayerTwo(diag1) || isSelectedByPlayerTwo(diag2)){
			return "Player 2 Won";
		}
		else{
			return "Draw";
		}
	}

	private ArrayList<Square> getRow(int i) {
		ArrayList<Square> squares = new ArrayList<>();
		for(Square s : _squares){
			if(s.isInRow(i)){
				squares.add(s);
			}
		}
		return squares;
	}
	
	private ArrayList<Square> getCol(int i) {
		ArrayList<Square> squares = new ArrayList<>();
		
		for(Square s : _squares){
			if(s.isInColumn(i)){
				squares.add(s);
			}
		}
		
		return squares;
	}

	private ArrayList<Square> getDiag(int i) {
		ArrayList<Square> squares = new ArrayList<>();
		
		if(i == 0){
			for(Square s: _squares){
				if(s.isInRow(0) && s.isInColumn(0)){
					squares.add(s);
				}
				if(s.isInRow(1) && s.isInColumn(1)){
					squares.add(s);
				}
				if(s.isInRow(2) && s.isInColumn(2)){
					squares.add(s);
				}
			}
		}
		else if(i == 1){
			for(Square s: _squares){
				if(s.isInRow(0) && s.isInColumn(2)){
					squares.add(s);
				}
				if(s.isInRow(1) && s.isInColumn(1)){
					squares.add(s);
				}
				if(s.isInRow(2) && s.isInColumn(0)){
					squares.add(s);
				}
			}

		}
		
		return squares;
	}
	
	private boolean isSelectedByPlayerOne(ArrayList<Square> squares) {
		for(Square s : squares){
			if(!(s.filledBy().equals("Player One"))){
				return false;
			}
		}
		return true;
	}
	
	private boolean isSelectedByPlayerTwo(ArrayList<Square> squares) {
		for(Square s : squares){
			if(!(s.filledBy().equals("Player Two"))){
				return false;
			}
		}
		return true;
	}

	public void endGame() {
		for(Square s : _possibleSquares){
			s.setAsFilled("Nobody");
		}
	}
}
