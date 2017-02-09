import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.NoughtsAndCrossesBoard;
import models.Square;

public class Main extends Application{

	private NoughtsAndCrossesBoard _board;
	private Game _game;
	private GameHistory _history;

	private static final String _file = System.getProperty("user.dir") + "/.Resources/history.ser";

	private ArrayList<Node> _squares = new ArrayList<>();
	private Stage _stage;
	private Pane _app = new Pane();
	private Pane _gamePane = new Pane();
	private AnimationTimer _timer;

	private boolean _playersTurn = true;
	private int _turn = 1;

	public static void main(String[] args) {
		//clearData();
		//runGames(1000);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();

		Scene scene = new Scene(_app);
		scene.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				handleMouseClick(event);
			}
		});

		// stage
		_stage = primaryStage;
		_stage.setTitle("Noughts and Crosses");
		_stage.setScene(scene);
		_stage.show();

		// animation timer
		_timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				if(!(_playersTurn)){
					AITurn();
				}
			}
		};

		_timer.start();
	}

	protected void AITurn() {
		ArrayList<NextMove> possibleNextMoves = new ArrayList<>();
		for(Square s : _board.getPossibleMoves()){
			possibleNextMoves.add(new NextMove(s,_turn));
		}

		if(!(possibleNextMoves.size() == 0)){

			String player = "";
			String desiredResult = "";
			String notDesiredResult = "";
			if(_turn % 2 == 1){
				player = "playerOne";
				desiredResult = "Player 1 Won";
				notDesiredResult = "Player 2 Won";
			}
			else{
				player = "playerTwo";
				desiredResult = "Player 2 Won";
				notDesiredResult = "Player 1 Won";
			}

			for(Game g : _history.getHistory()){
				if((_hasSameMoves(g)) && (_game.getMoves().size() < g.getMoves().size())){

					// get next move and result of game
					Move nextMove = g.getMoves().get(_game.getMoves().size());
					String result = g.getResult();

					// for each possible move record win percentage
					for(NextMove nm : possibleNextMoves){
						if(nm.isSameAs(nextMove)){
							nm.increment(result,desiredResult);

							// if next move results in winning, set wins()
							if(g.getResult().equals(desiredResult) && g.getMoves().size() == _game.getMoves().size() + 1){
								nm.wins();
								System.out.println("win");
							}
							// if move after next move has resulted in a loss, set loses()
							else if(g.getResult().equals(notDesiredResult) && g.getMoves().size() == _game.getMoves().size() + 2){
								nm.loses();
								System.out.println("loss");
							}
						}
					}

				}
			}



			// get move with the best rank
			NextMove moveToDo = null;
			int i = (int)(Math.random() * possibleNextMoves.size() + 1);
			moveToDo = possibleNextMoves.get(i - 1);
			for(NextMove move : possibleNextMoves){
				if(move.isBetterThan(moveToDo)){
					moveToDo = move;
				}
			}

			// if it is the first move just go randomly for now
			/*if(_turn == 1){
				i = (int)(Math.random() * possibleNextMoves.size() + 1);
				moveToDo = possibleNextMoves.get(i - 1);
			}*/

			Square toGo = moveToDo.getSquare();
			System.out.println(moveToDo.getRank());

			int num = showAITurn(toGo);

			// store whether player went first or second
			// set square as filled
			Square square = _board.getSquares().get(num);
			if(player == "playerOne"){
				square.setAsFilled("Player One");
			}
			else{
				square.setAsFilled("Player Two");
			}

			// store move
			_game.addMove(new Move(square, player, _turn));

			// remove from possible squares
			_board.removeFromPossibleMoves(square);

			// increment move number
			_turn++;

			handleGameEnd();

			_playersTurn = true;
		}
	}

	private int showAITurn(Square toGo){
		Integer num = -1;

		// first row
		Rectangle r = new Rectangle();
		if(toGo.toString().equals("0,0")){
			r = (Rectangle)_squares.get(0);
			num = 0;
		}

		else if(toGo.toString().equals("0,1")){
			r = (Rectangle)_squares.get(1);
			num = 1;
		}

		else if(toGo.toString().equals("0,2")){
			r = (Rectangle)_squares.get(2);
			num = 2;
		}

		else if(toGo.toString().equals("1,0")){
			r = (Rectangle)_squares.get(3);
			num = 3;
		}

		else if(toGo.toString().equals("1,1")){
			r = (Rectangle)_squares.get(4);
			num = 4;
		}

		else if(toGo.toString().equals("1,2")){
			r = (Rectangle)_squares.get(5);
			num = 5;
		}

		// third row
		else if(toGo.toString().equals("2,0")){
			r = (Rectangle)_squares.get(6);
			num = 6;
		}

		else if(toGo.toString().equals("2,1")){
			r = (Rectangle)_squares.get(7);
			num = 7;
		}

		else if(toGo.toString().equals("2,2")){
			r = (Rectangle)_squares.get(8);
			num = 8;
		}

		// show AI move
		r.setFill(Color.BLUE);

		return num;
	}

	private void handleGameEnd(){
		// check if someone has won
		if(_board.getResult().equals("Player 1 Won") || _board.getResult().equals("Player 2 Won") || _turn > 9){
			_board.endGame();
			// get result of game
			String result = _board.getResult();

			// set result
			_game.setResult(result);

			// record game
			_history.addGame(_game);

			// write history to file
			FileManipulator.writeHistoryTofile(_history, _file);

			// TODO end GUI too
			_stage.close();
		}
	}

	private boolean _hasSameMoves(Game game){
		for(Move m : _game.getMoves()){
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

	protected void handleMouseClick(Event event) {
		if(_playersTurn){
			MouseEvent me = (MouseEvent) event;
			double horizontal = me.getSceneX();
			double vertical = me.getSceneY();

			Integer num = -1;

			// first row
			Rectangle r = new Rectangle();
			if(horizontal <= 200 && vertical <= 200){
				r = (Rectangle)_squares.get(0);
				num = 0;
			}

			else if(horizontal <= 400 && vertical <= 200){
				r = (Rectangle)_squares.get(1);
				num = 1;
			}

			else if(horizontal <= 600 && vertical <= 200){
				r = (Rectangle)_squares.get(2);
				num = 2;
			}

			else if(horizontal <= 200 && vertical <= 400){
				r = (Rectangle)_squares.get(3);
				num = 3;
			}

			else if(horizontal <= 400 && vertical <= 400){
				r = (Rectangle)_squares.get(4);
				num = 4;
			}

			else if(horizontal <= 600 && vertical <= 400){
				r = (Rectangle)_squares.get(5);
				num = 5;
			}

			// third row
			else if(horizontal <= 200 && vertical <= 600){
				r = (Rectangle)_squares.get(6);
				num = 6;
			}

			else if(horizontal <= 400 && vertical <= 600){
				r = (Rectangle)_squares.get(7);
				num = 7;
			}

			else if(horizontal <= 600 && vertical <= 600){
				r = (Rectangle)_squares.get(8);
				num = 8;
			}
			if(r.getFill().equals(Color.WHITE)){
				// show where player has gone
				r.setFill(Color.RED);

				// store whether player went first or second
				// set square as filled
				Square square = _board.getSquares().get(num);
				String player = "";
				if(_turn % 2 == 1){
					player = "playerOne";
					square.setAsFilled("Player One");
				}
				else{
					player = "playerTwo";
					square.setAsFilled("Player Two");
				}

				// store move
				_game.addMove(new Move(square, player, _turn));

				// remove from possible squares
				_board.removeFromPossibleMoves(square);

				// increment move number
				_turn++;

				// check if someone has won
				if(_board.getResult().equals("Player 1 Won") || _board.getResult().equals("Player 2 Won") || _turn > 9){
					_board.endGame();
					// get result of game
					String result = _board.getResult();

					// set result
					_game.setResult(result);

					// record game
					_history.addGame(_game);

					// write history to file
					FileManipulator.writeHistoryTofile(_history, _file);

					// TODO end GUI too
					_stage.close();
				}

				_playersTurn = false;
			}
		}

	}

	private void initialize() {
		_history = FileManipulator.readHistoryFromFile(_file);

		_app.getChildren().add(_gamePane);

		_game = new Game();

		_board = new NoughtsAndCrossesBoard();


		Rectangle rect = new Rectangle(600,600, Color.GREY);
		_gamePane.getChildren().add(rect);

		_squares.add(createEntity(10, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 10, 180, 180, Color.WHITE));
		_squares.add(createEntity(10, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 210, 180, 180, Color.WHITE));
		_squares.add(createEntity(10, 410, 180, 180, Color.WHITE));
		_squares.add(createEntity(210, 410, 180, 180, Color.WHITE));
		_squares.add(createEntity(410, 410, 180, 180, Color.WHITE));

	}

	private Node createEntity(int x, int y, int w, int h, Color colour) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(colour);

		_gamePane.getChildren().add(entity);
		return entity;
	}

	private static void clearData(){
		GameHistory gh = new GameHistory();
		String file = System.getProperty("user.dir") + "/.Resources/history.ser";

		FileManipulator.writeHistoryTofile(gh, file);

		System.out.println("Game history cleared");
	}

	private static void runGames(int numberOfGames){
		String file = System.getProperty("user.dir") + "/.Resources/history.ser";
		GameHistory history = FileManipulator.readHistoryFromFile(file);
		GameHistory newHistory = new GameHistory();

		for(int i = 0; i < numberOfGames; i++){
			NoughtsAndCrossesBoard board = new NoughtsAndCrossesBoard();

			Game game = new Game();

			boolean turnCounter = true;
			int moveNumber = 1;


			while(!(board.isFull())){
				// goInRandomPlace that can be gone in
				// TODO change this so it checks history
				//int square = (int)(Math.random() * board.getPossibleMoves().size() + 1);
				//Square toGo = board.getPossibleMoves().get(square - 1);

				String player = "";
				String desiredResult = "";
				String notDesiredResult = "";
				if(moveNumber % 2 == 1){
					player = "playerOne";
					desiredResult = "Player 1 Won";
					notDesiredResult = "Player 2 Won";
				}
				else{
					player = "playerTwo";
					desiredResult = "Player 2 Won";
					notDesiredResult = "Player 1 Won";
				}

				ArrayList<NextMove> possibleNextMoves = new ArrayList<>();

				for(Square s : board.getPossibleMoves()){
					possibleNextMoves.add(new NextMove(s,moveNumber));
				}

				for(Game g : history.getHistory()){
					if((game.hasSameMoves(g)) && (game.getMoves().size() < g.getMoves().size())){
						// get next move and result of game

						Move nextMove = g.getMoves().get(game.getMoves().size());
						String result = g.getResult();

						// for each possible move record win percentage
						//TODO check this
						for(NextMove nm : possibleNextMoves){
							if(nm.isSameAs(nextMove)){
								nm.increment(result,desiredResult);
							}

							// if next move results in winning, set wins()
							if(g.getResult().equals(desiredResult) && g.getMoves().size() == game.getMoves().size() + 1){
								nm.wins();
							}
							// if move after next move has resulted in a loss, set loses()
							else if(g.getResult().equals(notDesiredResult) && g.getMoves().size() == game.getMoves().size() + 2){
								nm.loses();
							}
						}
					}
				}

				// TODO check this
				int square = (int)(Math.random() * possibleNextMoves.size() + 1);
				NextMove moveToDo = possibleNextMoves.get(square - 1);
				for(NextMove move : possibleNextMoves){
					if(move.isBetterThan(moveToDo)){
						moveToDo = move;
					}
				}

				// if it the first move just go randomly for now
				if(moveNumber == 1){
					square = (int)(Math.random() * possibleNextMoves.size() + 1);
					moveToDo = possibleNextMoves.get(square - 1);
				}

				Square toGo = moveToDo.getSquare();
				if(moveNumber == 2){
					System.out.println(toGo.toString());
				}

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
			newHistory.addGame(game);
			//history.addGame(game);

			System.out.println("Finished game " + i + " of " + numberOfGames);
		}
		// write history to file
		FileManipulator.writeHistoryTofile(newHistory, file);
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
