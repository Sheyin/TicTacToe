import java.util.Scanner;

class TicTacToe {

	public static void main (String[] args) {
		char[] grid;
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
		
	void PrintGrid(char[] grid) {
		//char[9] fgrid = ConvGrid(grid);
		System.out.println("/n " + grid[0] + " | " + grid[1] + " | " + grid[2]);
		System.out.println("/n " + grid[3] + " | " + grid[4] + " | " + grid[5]);
		System.out.println("/n " + grid[6] + " | " + grid[7] + " | " + grid[8]);
		return;
		}
	
	/* char[] ConvGrid(int[] grid) {
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
		} */
	
	char[] AddPoint(char[] grid, int spot, boolean playerTurn) {
		if (playerturn)
			grid[spot] = 'O';
		else	// comp turn
			grid[spot] = 'X';
		return grid;
		}
		
	boolean LegalMove(char[] grid, int spot) {
		if ((grid[spot] == ' ') && (spot >= 0) && (spot <=10))
			return true;
		else
			return false;
		}
		
	char[] PlayerMove(char[] grid) {
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

	char[] CompMove (char[] grid) {		// decides optimal move - victory, defend, some move in same row, then random move
		int [] results = MoveCheck(grid);
		int spot = CheckResults(results,2)
		
		if (VictoryMoveCheck(grid)) {
			grid = VictoryMove(grid);
			}
		if (OneXRow(grid)) {		// try to do one in same row
			grid = AddtoNextX(grid);
			}
		else {						//random move - ex. first move
			grid = RandomMove(grid);
			}
		return grid;
		}
		
	int [] MoveCheck (char[] grid) {		// summarize each of the rows
		int[] testspots = [0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6];
		int[] sortedresults;
		int XCount = 0;
		int OCount = 0;
		int nullCount = 0;
		for (int i = 0, i < 25, i++){
			if (grid[i] == 'X'){
				XCount++; }
			if (grid[i] == 'O'){
				OCount++; }
			if (grid[i] == ' '){
				nullCount++; }
			rowcount++;
			
			if ((i+1)%3 == 0){
				rawresult[i-2] = XCount;
				rawresult[i-1] = OCount;
				rawresult[i] = nullCount;
				i = i+3;
				sortedresults = SortResults (rawresults);
				
				//reset counters
				XCount = 0;
				OCount = 0;
				nullCount = 0;
				}
			}
		return sortedresults;
		}
		
	int[] SortResults(int[] unsorted) {
		int[] sorted;
		do {
			sorted[i] = unsorted[i];
			sorted[i+8] = unsorted[i+1];
			sorted[i+16] = unsorted[i+2];
			i = i+3;
			} while (i<24);
		return sorted;
	}
	
	int[] TestSpots(){		//this just returns the pattern of rows used on the grid to match up spots.
		//rownum 1: int[] row1 = [0, 1, 2];
		//rownum 2: int[] row2 = [3, 4, 5];
		//rownum 3: int[] row3 = [6, 7, 8];
		//rownum 4: int[] col1 = [0, 3, 6];
		//rownum 5: int[] col2 = [1, 4, 7];
		//rownum 6: int[] col3 = [2, 5, 8];
		//rownum 7: int[] diag1 = [0, 4, 8];
		//rownum 8: int[] diag2 = [2, 4, 6];
		int[] testspots = [0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6];
		return testspots;
	}
	
	int FindSpot(int[] summary) {	//this is searching for particular parameters.  i<8 means XX_, i<16 means OO_.
		for (int i=0, i<8, i++) {	// this is just checking for two X's and a blank
			if ((summary[i] == 2) && (summary[i+8] == 1)) {
				return i; }		//this is the row where there is a legal move - need to find the blank spot
			else
				// nothing... just let it increment i
			}
		for (int i=8, i<16, i++) {	// this is now searching for two O's and a blank.  Most likely to be used.
			if ((summary[i] == 2) && (summary[i+8] == 1)) {
				return i; }		//this is the row where there is a legal move - need to find the blank spot
			else
				// nothing... just let it increment i
			}
		return -1;		//no spots found matching this pattern.
	}
	
	int[] IDRow(int rownumber) {		// this identifies the row that should be moved to and returns the spots.
		//rownum 1: int[] row1 = [0, 1, 2];
		//rownum 2: int[] row2 = [3, 4, 5];
		//rownum 3: int[] row3 = [6, 7, 8];
		//rownum 4: int[] col1 = [0, 3, 6];
		//rownum 5: int[] col2 = [1, 4, 7];
		//rownum 6: int[] col3 = [2, 5, 8];
		//rownum 7: int[] diag1 = [0, 4, 8];
		//rownum 8: int[] diag2 = [2, 4, 6];
		int[] thisrow;
		switch (rownumber) {
			case 1:
				thisrow = [0, 1, 2];
			case 2:
				thisrow = [3, 4, 5];
			case 3:
				thisrow = [6, 7, 8];
			case 4:
				thisrow = [0, 3, 6];
			case 5:
				thisrow = [1, 4, 7];
			case 6:
				thisrow = [2, 5, 8];
			case 7:
				thisrow = [0, 4, 8];
			case 8:
				thisrow = [2, 4, 6];
		}
		return thisrow;		// method will know which spots to look in grid
	}

	int FindBlankSpot(int[] spots, char[] grid) {
		for(int i = 0, i < 3, i++) {
			if (grid[spots[i]] == ' ') {
				return spots[i]; }
			else
				//redo for loop
		}
	}
	
	int[] RandomMove (int[] grid) {
		isLegal = LegalMove(grid, move);
		grid = AddPoint(grid, move, false);
		return grid;
	}
	
		// left the following deprecated methods until I'm sure I won't need them anymore.
	
	int CheckResults (int[] results, int pattern) {		//searches results array for specified pattern
		int spot = 0;
		return spot;
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
	
	int RowChecker (int[] grid                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
}
