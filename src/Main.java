import models.NoughtsAndCrossesBoard;
import models.Square;

public class Main {

	public static void main(String args[]){
		displayStats();
	}

	private static void clearData(){
		GameHistory gh = new GameHistory();
		String file = System.getProperty("user.dir") + "/.Resources/history.ser";

		FileManipulator.writeHistoryTofile(gh, file);

		System.out.println("Game history cleared");
	}

	private static void runGames(int numberOfGames){
		for(int i = 0; i < numberOfGames; i++){
			NoughtsAndCrossesBoard board = new NoughtsAndCrossesBoard();

			String file = System.getProperty("user.dir") + "/.Resources/history.ser";
			GameHistory history = FileManipulator.readHistoryFromFile(file);
			//history = new GameHistory();

			Game game = new Game();

			boolean turnCounter = true;
			int moveNumber = 1;


			while(!(board.isFull())){
				// goInRandomPlace that can be gone in
				int square = (int)(Math.random() * board.getPossibleMoves().size() + 1);
				Square toGo = board.getPossibleMoves().get(square - 1);

				// record move in game object (square + player + move number)
				// set square as filled
				Move move = null;
				if(turnCounter){
					move = new Move(toGo, "playerOne", moveNumber);
					toGo.setAsFilled("Player One");
				}
				else{
					move = new Move(toGo, "playerTwo", moveNumber);
					toGo.setAsFilled("Player Two");
				}
				game.addMove(move);


				// remove from possible squares
				board.removeFromPossibleMoves(toGo);

				// swap player
				if(turnCounter){
					turnCounter = false;
				}
				else{
					turnCounter = true;
				}

				// increment move number
				moveNumber++;

				// check if someone has won
				if(board.getResult().equals("Player 1 Won") || board.getResult().equals("Player 2 Won")){
					board.endGame();
				}
			}

			// get result of game
			String result = board.getResult();

			// set result
			game.setResult(result);

			// record game
			history.addGame(game);

			// write history to file
			FileManipulator.writeHistoryTofile(history, file);

			System.out.println("Finished game " + i + " of " + numberOfGames);
		}
	}

	private static void displayStats(){
		String file = System.getProperty("user.dir") + "/.Resources/history.ser";
		GameHistory history = FileManipulator.readHistoryFromFile(file);

		int p1 = 0;
		int p2 = 0;
		int draw = 0;

		for(Game g : history.getHistory()){
			if(g.getResult().equals("Player 1 Won")){
				p1++;
			}
			else if(g.getResult().equals("Player 2 Won")){
				p2++;
			}
			else if(g.getResult().equals("Draw")){
				draw++;
			}
		}

		System.out.println("Player One Wins: " + p1);
		System.out.println("Player Two Wins: " + p2);
		System.out.println("Draws: " + draw);
	}
	
}
