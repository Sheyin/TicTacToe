import java.util.Scanner;

class TicTacToe {

	public static void main (String[] args) {
		int[] grid;
		int moveCount = 0;
		boolean victory = false;
		boolean playerturn = CoinFlip();
		do {
			PrintGrid(grid);
			if (playerturn) {
				grid = PlayerMove(grid);
				playerturn = false;
					}
			else {	// playerturn == false
				grid = CompMove(grid);
				playerturn = false;
				}
			victory = VictoryCheck(grid);
			movecount++;
			} while((movecount < 10) || (!victory));
		DeclareVictory(grid);
		}
		
	boolean CoinFlip() {
		int randnum = (int)(Math.rand());
		if (randnum < 0.45) {
			return true;
			}
		else {	// >0.45
			return false;
			}
		}
		
	void PrintGrid(int[] grid) {
		char[9] fgrid = ConvGrid(grid);
		System.out.println("/n " + fgrid[0] + " | " + fgrid[1] + " | " + fgrid[2]);
		System.out.println("/n " + fgrid[3] + " | " + fgrid[4] + " | " + fgrid[5]);
		System.out.println("/n " + fgrid[6] + " | " + fgrid[7] + " | " + fgrid[8]);
		return;
		}
	
	char[] ConvGrid(int[] grid) {
		char [] fgrid;
		for (int i=0, i<10, i++) {
			if (grid[i] == -1) {
				fgrid[i] = 'O';
				}
			if (grid[i] == -2) {
				fgrid[i] == 'X';
				}
			else {	// no mark / empty
				fgrid[i] == " ";
				}
			}
		return fgrid;
		}
	
	int[] AddPoint(int[] grid, int spot, boolean playerTurn) {
		if (playerturn)
			grid[spot] = -1;
		else	// comp turn
			grid[spot] = -2;
		return grid;
		}
		
	boolean LegalMove(int[] grid, int spot) {
		if ((grid[spot] == 0) && (spot >= 0) && (spot <=10))
			return true;
		else
			return false;
		}
		
	int[] PlayerMove(int[] grid) {
		int move = 0;
		boolean isLegal = false;
		do {
			move = PlayerInput();
			isLegal = LegalMove(grid, move);
			
			if (isLegal) {
				grid = AddPoint(grid, move, true);
				return grid;
			}
			else {
				System.out.println("This spot is taken.  Try another spot.");
					}
		} while(!isLegal)
	}

		
	int PlayerInput(){
		Scanner user_input = new Scanner(System.in);
		String playerInput = user_input.next();
		boolean validInput = InputCheck(user_input.charAt(0));
		do {
			if (validInput) {
				int spot = parseInt(user_input[0]-1);
				return spot;
				}
			else { 	// invalid input
				System.out.println("Invalid input.  Please pick a square by choosing a number.");
				String playerInput = user_input.next();
				}
			} while (!validInput)
		}
		
	boolean InputCheck (String toCheck) {	// don't forget to subtract 1 when checking
		try {	// this blocks test if it is an integer at all
			Integer.parseInt(toCheck); 
			} 
		catch(NumberFormatException e) { 
			return false; 
			}
		int checkMe = (toCheck.charAt(0) - 1);	
		return true;
		}

	int[] CompMove (int[] grid) {		// victory, defend, some move in same row, then random move
		if (VictoryMoveCheck(grid)) {
			grid = VictoryMove(grid);
			}
		if (DefendMoveCheck(grid)) {
			grid = DefendMove(grid);
			}
		if (OneXRow(grid)) {		// try to do one in same row
			grid = AddtoNextX(grid);
			}
		else {						//random move - ex. first move
			grid = RandomMove(grid);
			}
		return grid;
		}
		
	boolean VictoryMoveCheck (int[] grid) {
		return true; // temp
	}
	
	int[] VictoryMove (int[] grid) {
		isLegal = LegalMove(grid, move);
		grid = AddPoint(grid, move, false);
	}
	
	boolean DefendMoveCheck (int[] grid) {
		return true; // temp
	}
	
	int[] DefendMove (int[] grid) {
		isLegal = LegalMove(grid, move);
		grid = AddPoint(grid, move, false);
	}
	
	boolean OneXRow (int[] grid) {
		return true; // temp
	}
	
	int[] AddtoNextX (int[] grid) {
		isLegal = LegalMove(grid, move);
		grid = AddPoint(grid, move, false);
	}
	
	int[] RandomMove (int[] grid) {
		isLegal = LegalMove(grid, move);
		grid = AddPoint(grid, move, false);
	}
}
