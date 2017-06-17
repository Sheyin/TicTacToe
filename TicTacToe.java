import java.util.Scanner;
import static java.util.Arrays.fill;

public class TicTacToe {

	public static void main (String[] args) {
		char[] grid = {' ',' ',' ', ' ',' ', ' ',' ', ' ',' '};
		int moveCount = 1;
		boolean victory = false;
		boolean playerturn = coinFlip();
		do {
			printGrid(grid);
			if (playerturn) {
				playerMove(grid);
				playerturn = false;
					}
			else {	// playerturn == false
				System.out.println("Computer turn!");
				compMove(grid);
				playerturn = true;
				}
			moveCount++;
			} while((moveCount < 10) && (!victory));
		declareVictory('T');
		}
		

	// coinFlip(): Determines who goes first by rolling a random number and determining if odd/even
	public static boolean coinFlip() {
		int randnum = (int)(Math.random() * 100);
		System.out.println("Random: " + randnum);
		// if odd
		if (randnum % 2 == 1) {
			return true;
			}
		// if even
		else {
			return false;
			}
		}
	
	// addPoint(): Adds an O or X to the grid (if player or computer)
	public static void addPoint(char[] grid, int spot, boolean playerTurn) {
		if (playerTurn)
			grid[spot] = 'O';
		else	// comp turn
			grid[spot] = 'X';
			
		victoryCheck(grid);
		return;
		}
		
	// legalMove(): Checks if the desired input is legal or not (within grid range, or occupied)
	public static boolean legalMove(char[] grid, int spot) {
		if ((grid[spot] == ' ') && (spot >= 0) && (spot < 10))
			return true;
		else
			return false;
		}
		
	// playerMove(): Overall function handling player movement
	public static void playerMove(char[] grid) {
		int move = 0;
		boolean isLegal = false;
		do {
			System.out.print("Select a spot: ");
			move = playerInput(grid);
			isLegal = legalMove(grid, move);
			
			if (isLegal) {
				addPoint(grid, move, true);
				return;
			}
			else {
				System.out.println("This spot is taken.  Try another spot.");
					}
		} while(!isLegal);
		return;	//is this needed?
	}

		
	public static int playerInput(char[] grid){
		Scanner user_input = new Scanner(System.in);
		String playerInput = user_input.nextLine();
		char toTest = playerInput.charAt(0);
		boolean validInput = inputCheck(toTest);
		do {
			if (validInput) {
				int spot = Character.getNumericValue(toTest);
				//int spot = parseInt(user_input[0]-1);
				return (spot-1);
				}
			else { 	// invalid input
				printGrid(grid);
				System.out.println("Invalid input.  Please pick a square by choosing a number.");
				playerInput = user_input.nextLine();
				}
			} while (!validInput);
			return -1; //is this needed?
		}
		
	public static boolean inputCheck (int toCheck) {	// don't forget to subtract 1 when checking
	/* fix me
		try {	// this blocks test if it is an integer at all
			//Integer.parseInt(String); 
			} 
		catch(NumberFormatException e) { 
			return false; 
			}
		int checkMe = (toCheck.charAt(0) - 1);	*/
		return true;
		}
		
	// testing out void return
	public static void compMove (char[] grid) {		// decides optimal move - victory, defend, some move in same row, then random move
		boolean isLegal = false;
		int spot = twoCheck(grid);

		if (spot == -1) {
			do {	// random move
				spot = (int)((Math.random() * 10) -1);
				isLegal = legalMove(grid,spot);
				} while(!isLegal);
			}
		addPoint(grid,spot,false);
		return;
		}
	
	public static int twoCheck (char[] grid) {
		int[] row = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};		// these locations define the 8 rows (victory conditions)
		int[] rowTest = new int[3];
		int i = 0;
		int moveHere = -1;
		do {
			int j = 0;
			rowTest[j] = row[i];
			rowTest[j+1] = row[i+1];
			rowTest[j+2] = row[i+2];
			moveHere = rowTest(grid, rowTest, 'X');
			//System.out.println("Debug: moveHere = " + moveHere);
				if (moveHere >= 0) {
					return moveHere; }
			i = i + 3;
			} while (i < 24);
			
			// assume no XX found
			i = 0;	//resetting counter
			
		do {
			int j = 0;
			rowTest[j] = row[i];
			rowTest[j+1] = row[i+1];
			rowTest[j+2] = row[i+2];
			moveHere = rowTest(grid, rowTest, 'O');
				if (moveHere >= 0) {
					return moveHere; }
			i = i + 3;
			} while (i < 24);	
			
		return -1;
	}
	
	public static int rowTest(char[] grid, int[] row, char testFor) {		// Tests the given row for victory/defend conditions.
		int testcount = 0;
		int blankspot = 0;
		int blanklocation = -1;
		for (int i = 0; i< 3; i++) {
			if (grid[row[i]] == testFor) {
				testcount++;
			}
			if (grid[row[i]] == ' ') {
				blankspot++;
				blanklocation = row[i];
			}
		}
		
		if ((testcount == 2) && (blankspot == 1)) {
				return blanklocation;
			}
		else {
			return -1;	// no pairs found, or filled
			}
	}


	// victoryCheck(): This is called after every move to see if there is a winner or not.
	// It works by counting the number of X's, O's, and blank spaces
	public static boolean victoryCheck (char [] grid) {
		int[] row = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};		// these locations define the 8 rows (victory conditions)
		int xCount = 0;
		int oCount = 0;
		int tiecount = 0;
		int nullCount = 0;
		
		// Since there are only 8 possible combinations, with 3 characters each, go through each of the 24 characters defined in row
		for (int rowcount = 0; rowcount < 24; rowcount++) {
				if (grid[row[rowcount]] == ' ') {
					nullCount++;
					}
				if (grid[row[rowcount]] == 'X') {
					xCount++;
					tiecount++;
					}
				if (grid[row[rowcount]] == 'O') {
					oCount++;
					tiecount++;
					}
	
		// If a victory was detected, print grid and play ending sequence (see if it is the last character in a row defined in row)
		if ((rowcount + 1) % 3 == 0) {
			if (xCount == 3) {
				printGrid(grid);
				declareVictory('X');
				}
			if (oCount == 3) {
				printGrid(grid);
				declareVictory('O');
				}
			if (tiecount == 24) {
				printGrid(grid);
				declareVictory('T');
				}
				
			// resetting counters
			xCount = 0;
			oCount = 0;
			nullCount = 0;
			}
		}	
		return false;
	}


	// declareVictory(): prints winning text and ends game
	public static void declareVictory(char winner) {
		switch (winner) {
			case 'O': {
				System.out.println("You win!");
				System.exit(0);
				}
			case 'X': {
				System.out.println("You lose!");
				System.exit(0);
				}
			default: {
				System.out.println("It's a tie!");
				System.exit(0);
				}
			}
		}
		
	public static void printGrid(char[] grid) {
		System.out.println(grid[0] + " | " + grid[1] + " | " + grid[2]);
		System.out.println("_________");
		System.out.println(grid[3] + " | " + grid[4] + " | " + grid[5]);
		System.out.println("_________");
		System.out.println(grid[6] + " | " + grid[7] + " | " + grid[8]);
		return;
		}
}
