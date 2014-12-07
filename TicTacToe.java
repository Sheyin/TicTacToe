import java.util.Scanner;
import static java.util.Arrays.fill;

public class ttt {

	public static void main (String[] args) {
		char[] grid = {' ',' ',' ', ' ',' ', ' ',' ', ' ',' '};
		int moveCount = 1;
		boolean victory = false;
		boolean playerturn = CoinFlip();
		do {
			PrintGrid(grid);
			if (playerturn) {
				PlayerMove(grid);
				playerturn = false;
					}
			else {	// playerturn == false
				System.out.println("Computer turn!");
				CompMove(grid);
				playerturn = true;
				}
			moveCount++;
			} while((moveCount < 10) && (!victory));
		DeclareVictory('T');
		}
		
	public static boolean CoinFlip() {
		int randnum = (int)(Math.random());
		if (randnum < 0.45) {
			return true;
			}
		else {	// >0.45
			return false;
			}
		}
	
	public static void AddPoint(char[] grid, int spot, boolean playerTurn) {
		if (playerTurn)
			grid[spot] = 'O';
		else	// comp turn
			grid[spot] = 'X';
			
		VictoryCheck(grid);
		return;
		}
		
	public static boolean LegalMove(char[] grid, int spot) {
		if ((grid[spot] == ' ') && (spot >= 0) && (spot < 10))
			return true;
		else
			return false;
		}
		
	public static void PlayerMove(char[] grid) {
		int move = 0;
		boolean isLegal = false;
		do {
			System.out.print("Select a spot: ");
			move = PlayerInput(grid);
			isLegal = LegalMove(grid, move);
			
			if (isLegal) {
				AddPoint(grid, move, true);
				return;
			}
			else {
				System.out.println("This spot is taken.  Try another spot.");
					}
		} while(!isLegal);
		return;	//is this needed?
	}

		
	public static int PlayerInput(char[] grid){
		Scanner user_input = new Scanner(System.in);
		String playerInput = user_input.nextLine();
		char toTest = playerInput.charAt(0);
		boolean validInput = InputCheck(toTest);
		do {
			if (validInput) {
				int spot = Character.getNumericValue(toTest);
				//int spot = parseInt(user_input[0]-1);
				return (spot-1);
				}
			else { 	// invalid input
				PrintGrid(grid);
				System.out.println("Invalid input.  Please pick a square by choosing a number.");
				playerInput = user_input.nextLine();
				}
			} while (!validInput);
			return -1; //is this needed?
		}
		
	public static boolean InputCheck (int toCheck) {	// don't forget to subtract 1 when checking
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
	public static void CompMove (char[] grid) {		// decides optimal move - victory, defend, some move in same row, then random move
		boolean isLegal = false;
		int spot = TwoCheck(grid);

		if (spot == -1) {
			do {	// random move
				spot = (int)((Math.random() * 10) -1);
				isLegal = LegalMove(grid,spot);
				} while(!isLegal);
			}
		AddPoint(grid,spot,false);
		return;
		}
	
	public static int TwoCheck (char[] grid) {
		int[] row = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};		// these locations define the 8 rows (victory conditions)
		int[] rowTest = new int[3];
		int i = 0;
		int moveHere = -1;
		do {
			int j = 0;
			rowTest[j] = row[i];
			rowTest[j+1] = row[i+1];
			rowTest[j+2] = row[i+2];
			moveHere = RowTest(grid, rowTest, 'X');
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
			moveHere = RowTest(grid, rowTest, 'O');
			System.out.println("moveHere = " + moveHere);
				if (moveHere >= 0) {
					return moveHere; }
			i = i + 3;
			} while (i < 24);	
			
		return -1;
	}
	
	public static int RowTest(char[] grid, int[] row, char testFor) {		// Tests the given row for victory/defend conditions.
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
	
	public static boolean VictoryCheck (char [] grid) {
		int[] row = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};		// these locations define the 8 rows (victory conditions)
		int XCount = 0;
		int OCount = 0;
		int tiecount = 0;
		int nullCount = 0;
		for (int rowcount = 0; rowcount < 24; rowcount++) {
				if (grid[row[rowcount]] == ' ') {
					nullCount++;
					}
				if (grid[row[rowcount]] == 'X') {
					XCount++;
					tiecount++;
					}
				if (grid[row[rowcount]] == 'O') {
					OCount++;
					tiecount++;
					}
	
		if ((rowcount + 1) % 3 == 0) {
			if (XCount == 3) {
				PrintGrid(grid);
				DeclareVictory('X');
				}
			if (OCount == 3) {
				PrintGrid(grid);
				DeclareVictory('O');
				}
			if (tiecount == 24) {
				PrintGrid(grid);
				DeclareVictory('T');
				}
				
			// resetting counters
			XCount = 0;
			OCount = 0;
			nullCount = 0;
			}
		}	
		return false;
	}
	
	public static void DeclareVictory(char winner) {
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
		
	public static void PrintGrid(char[] grid) {
		System.out.println(grid[0] + " | " + grid[1] + " | " + grid[2]);
		System.out.println("_________");
		System.out.println(grid[3] + " | " + grid[4] + " | " + grid[5]);
		System.out.println("_________");
		System.out.println(grid[6] + " | " + grid[7] + " | " + grid[8]);
		return;
		}
}
