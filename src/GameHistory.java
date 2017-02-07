import java.io.Serializable;
import java.util.ArrayList;

public class GameHistory implements Serializable{
	private ArrayList<Game> _games;
	
	public GameHistory(){
		_games = new ArrayList<>();
	}
	
	public void addGame(Game game){
		_games.add(game);
	}

	public ArrayList<Game> getHistory() {
		return _games;
	}
}
