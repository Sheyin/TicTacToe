import java.util.Scanner;
import static java.util.Arrays.fill;

public class ttt {

	public static void main (String[] args) {
		char[] grid = {' ',' ',' ', ' ',' ', ' ',' ', ' ',' '};
		int moveCount = 1;
		int victory = -1;
		boolean playerturn = CoinFlip();
		do {
			System.out.println("Debug: main(): movecount=" + moveCount);
			PrintGrid(grid);
			if (playerturn) {
				grid = PlayerMove(grid);
				playerturn = false;
					}
			else {	// playerturn == false
				grid = CompMove(grid);
				playerturn = true;
				}
			victory = VictoryCheck(grid);
			moveCount++;
			} while((moveCount < 10) && (victory < 0));
		DeclareVictory(victory);
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
	
	public static char[] AddPoint(char[] grid, int spot, boolean playerTurn) {
		if (playerTurn)
			grid[spot] = 'O';
		else	// comp turn
			grid[spot] = 'X';
		return grid;
		}
		
	public static boolean LegalMove(char[] grid, int spot) {
		if ((grid[spot] == ' ') && (spot >= 0) && (spot <=10))
			return true;
		else
			return false;
		}
		
	public static char[] PlayerMove(char[] grid) {
		int move = 0;
		boolean isLegal = false;
		do {
			System.out.println("Select a tile.");
			move = PlayerInput(grid);
			isLegal = LegalMove(grid, move);
			
			if (isLegal) {
				grid = AddPoint(grid, move, true);
				return grid;
			}
			else {
				System.out.println("This spot is taken.  Try another spot.");
					}
		} while(!isLegal);
		return grid;	//is this needed?
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

	public static char[] CompMove (char[] grid) {		// decides optimal move - victory, defend, some move in same row, then random move
		int [] summary;
		boolean isLegal = false;
		summary = MoveCheck(grid);
		int spot = FindSpot(summary);
		System.out.println("Debug: CompMove processing spot (" + spot + ") from FindSpot");
		if (spot == -1) {
			do {	// random move
				spot = (int)((Math.random() * 10) -1);
				System.out.println("Debug: CompMove() spot(random) = " + spot);
				isLegal = LegalMove(grid,spot);
				System.out.println("Debug: CompMove() isLegal=" + isLegal);
				} while(!isLegal);
			}
		else {	// need to turn spot (a row) into a location
			System.out.println("Debug: CompMove() sending spot (" + spot + ") to IDRow");
			int [] rownumbers = IDRow(spot);	// this returns the array locations in grid of that row
			spot = FindBlank(grid, rownumbers);
			System.out.println("Debug: CompMove: spot=" + spot);
			}
		grid = AddPoint(grid,spot,false);
		return grid;
		}
		
	public static int FindBlank(char[] grid, int[] row) {
		char toTest;
		System.out.println("Debug: FindBlank");
		System.out.println("Debug: The row is: " + row[0] + row[1] + row[2]);
		
		for (int i=0;i<3;i++){
			toTest = grid[row[i]];
			System.out.println("Debug: FindBlank() toTest=" + toTest);
			if (toTest == ' '){
				System.out.println("Debug: FindBlank() returning row[i]:" + row[i]);
				return row[i]; }
		}
		return -1;	// is this needed?
	}

	public static int VictoryCheck (char [] grid) {
		System.out.println("Debug: Starting VictoryCheck, Calling on MoveCheck()");
		int[] summary = MoveCheck(grid);
		for(int i=0; i<16; i++){
			if (summary[i] == 3) {
				System.out.println("Debug: VictoryCheck found 3 in a row");
				return i;
				}
			}
			// no one won yet
				System.out.println("Debug: VictoryCheck found nothing (-1)");
				return -1;
	}
	
	public static void DeclareVictory(int winner) {
	System.out.println("Debug: DeclareVictory() winner=" + winner);
		if (winner > 7) {		//player wins
			System.out.println("You win!");
			}
		else {
			if ((winner >= 0) && (winner < 8)) {	// computer wins
				System.out.println("You lose!");
				}
			else {	//draw (?)
				System.out.println("It's a draw!");
				}
			}
	}
		
	public static int[] MoveCheck (char[] grid) {		// summarize each of the rows (counts)
		//System.out.println("Debug: Starting MoveCheck()");
		int[] row = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};		// these locations define the 8 rows (victory conditions)
		int[] sortedresults = new int[24];
		fill(sortedresults,0);
		int XCount = 0;
		int OCount = 0;
		int nullCount = 0;
		//int[] unsorted = new int[24];
		//fill(unsorted,0);
		int rowcount = 0;
		//this 
		for (int i = 0; i < 24; i++){
			//System.out.println("Debug: MoveCheck starting().  Loop #" + i + ", row[i]=" + row[i]);
			if (grid[row[i]] == 'X'){
				XCount++; }
			if (grid[row[i]] == 'O'){
				OCount++; }
			if (grid[row[i]] == ' '){
				nullCount++; }
			
			if (((i+1)%3 == 0) && (i!=0)){
				//System.out.println("Debug: MoveCheck writing to sortedresults. i=" + i);
				sortedresults[rowcount] = XCount;		// 0-7
				sortedresults[rowcount+8] = OCount;		// 8-15
				sortedresults[rowcount+16] = nullCount;	// 16-23
				rowcount++;
				//System.out.println("Debug: Calling on SortResults()");
				//sortedresults = SortResults (unsorted);
				
				//reset counters
				XCount = 0;
				OCount = 0;
				nullCount = 0;
				}
			}
		//System.out.println("Debug: MoveCheck() returning sortedresults");
		return sortedresults;
		}
		
	/* Might not need this method after all.
	public static int[] SortResults(int[] unsorted) {
		int[] sorted = new int[24];
		int i = 0;
		int j = 0;
		do {
			sorted[j] = unsorted[i];
			sorted[j+8] = unsorted[i+1];
			sorted[j+16] = unsorted[i+2];
			i = i+3;
			j++;
			} while (i<24);	//this may be problematic
			System.out.println("Debug: SortResults() returning sorted");
		return sorted;
	} */
	
	public int[] TestSpots(){		//this just returns the pattern of rows used on the grid to match up spots.
		//rownum 1: int[] row1 = [0, 1, 2];
		//rownum 2: int[] row2 = [3, 4, 5];
		//rownum 3: int[] row3 = [6, 7, 8];
		//rownum 4: int[] col1 = [0, 3, 6];
		//rownum 5: int[] col2 = [1, 4, 7];
		//rownum 6: int[] col3 = [2, 5, 8];
		//rownum 7: int[] diag1 = [0, 4, 8];
		//rownum 8: int[] diag2 = [2, 4, 6];
		int[] testspots = {0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6};
		return testspots;
	}
	
	public static int FindSpot(int[] summary) {	//this is searching for particular parameters.  i<8 means XX_, i<16 means OO_.
		System.out.println("Debug: Calling on FindSpot()");
		System.out.println("Debug: FindSpot() summary");
		for (int i=0; i<8; i++) {	// this is just checking for two X's and a blank
			if ((summary[i] == 2) && (summary[i+8] == 1)) {
				return i; }		//this is the row where there is a legal move - need to find the blank spot
				// nothing... just let it increment i
		}
		for (int i=8; i<16; i++) {	// this is now searching for two O's and a blank.  Most likely to be used.
			if ((summary[i] == 2) && (summary[i+8] == 1)) {
				return i; }		//this is the row where there is a legal move - need to find the blank spot
				// nothing... just let it increment i
		}
		return -1;		//no spots found matching this pattern.
	}
	
	public static int[] IDRow(int rownumber) {		// this identifies the row and returns the spots.
		//rownum 1: int[] row1 = [0, 1, 2];			// method will know which spots to look in grid
		//rownum 2: int[] row2 = [3, 4, 5];
		//rownum 3: int[] row3 = [6, 7, 8];
		//rownum 4: int[] col1 = [0, 3, 6];
		//rownum 5: int[] col2 = [1, 4, 7];
		//rownum 6: int[] col3 = [2, 5, 8];
		//rownum 7: int[] diag1 = [0, 4, 8];
		//rownum 8: int[] diag2 = [2, 4, 6];
		System.out.println("Debug: IDRow() rownumber=" + rownumber);
		if (rownumber > 7) {
			rownumber = rownumber - 8;
			System.out.println("Debug: IDRow: new rownumber=" + rownumber);
			}
			
		switch (rownumber) {
			case 0: {
				int[] thisrow = {0, 1, 2};
				return thisrow;	
				}
			case 1: {
				int[] thisrow = {3, 4, 5};
				return thisrow;	
				}
			case 2: {
				int[] thisrow = {6, 7, 8};
				return thisrow;	
				}
			case 3: {
				int[] thisrow = {0, 3, 6};
				return thisrow;	
				}
			case 4: {
				int[] thisrow = {1, 4, 7};
				return thisrow;	
				}
			case 5: {
				int[] thisrow = {2, 5, 8};
				return thisrow;	
				}
			case 6: {
				int[] thisrow = {0, 4, 8};
				return thisrow;	
				}
			case 7: {
				int[] thisrow = {2, 4, 6};
				return thisrow;	
				}
			default: {
				int[] thisrow = {0, 0, 0};
				System.out.println("Debug: IDRow() fell into default.  Error!");
				return thisrow;	
				}
		}
	}
	
	public char[] RandomMove (char[] grid) {
		int move = 0;
		boolean isLegal = false;
		do {
			move = (int)(Math.random()*10);
			isLegal = LegalMove(grid, move); 
				} while (!isLegal);
		grid = AddPoint(grid, move, false);
		return grid;
	}
	
		public static void PrintGrid(char[] grid) {
		System.out.println(grid[0] + " | " + grid[1] + " | " + grid[2]);
		System.out.println("_________");
		System.out.println(grid[3] + " | " + grid[4] + " | " + grid[5]);
		System.out.println("_________");
		System.out.println(grid[6] + " | " + grid[7] + " | " + grid[8]);
		return;
		}
		
		/* experimental new code here
		public static void PrintGrid(char[] grid) {
		System.out.println(1 + " | " + 2 + " | " + 3);
		System.out.println("_________");
		System.out.println(4 + " | " + 5 + " | " + 6);
		System.out.println("_________");
		System.out.println(7 + " | " + 8 + " | " + 9);
		return;
		} */
	
}
