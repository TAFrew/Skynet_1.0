import models.Square;

public class NextMove {
	private Square _square;
	private int _turn;
	private int _wins;
	private int _losses;
	private int _draws;
	
	private boolean _winThisTurn;
	private boolean _loseNextTurn;

	public NextMove(Square square, int turn){
		_square = square;
		_turn = turn;
		_wins = 0;
		_losses = 0;
		_draws = 0;
		_winThisTurn = false;
		_loseNextTurn = false;
	}

	public void wins(){
		_winThisTurn = true;
	}
	
	public void loses(){
		_loseNextTurn = true;
	}
	
	public String toString(){
		return _square.toString();
	}

	public Square getSquare(){
		return _square;
	}

	public void increment(String result, String desiredResult) {
		if(result.equals("Draw")){
			_draws++;
		}
		else if(result.equals(desiredResult)){
			_wins++;
		}
		else{
			_losses++;
		}
	}

	public double winPercentage(){
		if(_wins == 0 && _draws == 0 && _losses == 0){
			return 1.0;
		}
		else{
			return _wins / (double)(_wins + _losses + _draws);
		}
	}

	public double drawPercentage(){
		if(_wins == 0 && _draws == 0 && _losses == 0){
			return 1.0;
		}
		else{
			return _draws / (double)(_wins + _losses + _draws);
		}
	}
	
	private double lossesPercentage() {
		if(_wins == 0 && _draws == 0 && _losses == 0){
			return 0.0;
		}
		else{
			return _losses / (double)(_wins + _losses + _draws);
		}
	}


	public boolean isBetterThan(NextMove move) {
		double thisMoveRank = this.getRank();
		double moveRank = move.getRank();
		
		if(this._winThisTurn){
			return true;
		}
		else if(this._loseNextTurn){
			return false;
		}
		else if(thisMoveRank > moveRank){
			return true;
		}
		else if((thisMoveRank == moveRank) && (this._wins + this._losses + this._draws) < (move._wins + move._losses + move._draws)){
			return true;
		}
		
		/*if(this.lossesPercentage() < move.lossesPercentage()){
			return true;
		}
		else if((this.lossesPercentage() == move.lossesPercentage() && this.winPercentage() > move.winPercentage())){
			return true;
		}
		else if((this.winPercentage() == move.winPercentage() && this.lossesPercentage() == move.lossesPercentage())){
			if(this._losses < move._losses){
				return true;
			}
		}*/
		return false;
	}

	public double getRank() {
		if(_winThisTurn){
			return 1.0;
		}
		else if(_loseNextTurn){
			return 0.0;
		}
		else if(_losses == 0){
			return _wins + 0.5 * _draws;
		}
		else{
			return (_wins + (0.5 * _draws)) / _losses;
		}
	}

	// this is wrong Tim. move numbers do not matter.
	// done
	public boolean isSameAs(Move nextMove) {
		if(this.toString().equals(nextMove.getLocation().toString())){
			return true;
		}
		else{
			return false;
		}
	}
}
