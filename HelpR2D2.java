import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HelpR2D2 extends SearchProblem{

	private static final int MAX_CELLS = 2;
	static Random random = new Random();
	static int rocksNumber; //pressure pad number = rocks number
	static int obstaclesNumber;
	R2D2State initialState1; 
	static int m =3;//random.nextInt(MAX_CELLS)+3; //rows
	static int n =3;//random.nextInt(MAX_CELLS)+3; //columns
	static int noOfExpandedNodes=0;
	public HelpR2D2(R2D2State initialState, int pathCost, char operators) {
		super(initialState, pathCost, operators);
	}

	public R2D2State getInitialState1() {
		return initialState1;
	}

	public void setInitialState1(R2D2State initialState1) {
		this.initialState1 = initialState1;
	}
	
	@Override
	public ArrayList<Node> Expand(Node n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int pathCost(Node n) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean goalTest(Node n) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	/**
	 * genGrid: produces a randomized grid with specific dimensions m and n where the number of 
	 * rocks = number of pressure pads, 1 agent and 1 teleportal.
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static String[][] genGrid() throws FileNotFoundException, UnsupportedEncodingException{
		String[][] grid = new String [m][n];
		

		String[][] gridCom = new String [m][n];
		for (int i =0; i<m; i++){
			for(int j=0 ; j<n ; j++){
				gridCom[i][j]=(i + "," + j);
			}
		}
		String[][] arr2 = shuffle(gridCom);
		//Agent and Teleportal positions
		String agentPosition = "agent(" + arr2[0][0].split(",")[0] +  "," + arr2[0][0].split(",")[1] +",s0)." ;
		String teleportalPosition = "teleportal("+arr2[0][1].split(",")[0] + ","+ arr2[0][1].split(",")[1]+").";
		grid[Integer.parseInt(arr2[0][0].split(",")[0])][Integer.parseInt(arr2[0][0].split(",")[1])] = "Agent";
		grid[Integer.parseInt(arr2[0][1].split(",")[0])][Integer.parseInt(arr2[0][1].split(",")[1])] = "Teleportal";

		//Rows and columns count of the grid to parse to the .pl file
		String rowPL = "row("+m+ ").";
		String columnPL = "column("+n+").";
		
		//Number of rocks, pressure pads and obstacles in the grid
		obstaclesNumber = random.nextInt(((m*n)-4)+1) /2; //decrease number of obstacles
		rocksNumber = random.nextInt(m)+1;
		while(rocksNumber> ((m*n)-obstaclesNumber)/2)
			rocksNumber = random.nextInt(m)+2;
		if(obstaclesNumber >=3)
			obstaclesNumber = obstaclesNumber -2;
		if(rocksNumber >=3){
			rocksNumber = rocksNumber-2;
		}

		//Strings for the .pl file
		int counter = 0;
		String obstaclePositions = "";
		String rockPositions = "";
		String pressurePadPositions = "";

		//generating obstacles
		for(int i =0; i<obstaclesNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && (counter<obstaclesNumber)){
						grid[x][y] = "Obstacle";

						obstaclePositions = obstaclePositions +"obstacle(" + Integer.toString(x) + "," + Integer.toString(y) + ")." +"\r\n";
						counter ++;
					}
				}
			}

		}
	////generating rocks
		counter = 0;
		for(int i =0; i<rocksNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && grid[x][y] !="Obstacle" && (counter<rocksNumber)){
						grid[x][y] = "Rock";
						rockPositions = rockPositions +"rock(" + Integer.toString(x) + "," + Integer.toString(y) + ",s0)." +"\r\n";
						counter ++;
					}
				}
			}

		}
	////generating pressure pads
		counter = 0 ;
		for(int i =0; i<rocksNumber ; i++){
			for(int x = 0; x<arr2.length; x++){
				for(int y =0 ; y<arr2[i].length; y ++){
					if(grid[x][y] !="Agent" && grid[x][y] !="Teleportal" && grid[x][y] !="Obstacle" && grid[x][y] !="Rock" && (counter<rocksNumber)){
						grid[x][y] = "Pressure pad";
						pressurePadPositions = pressurePadPositions +"pressure_pad(" + Integer.toString(x) + "," + Integer.toString(y) + ")." +"\r\n";
						counter ++;
					}
				}
			}

		}
		
		//Writing the .pl file
		Charset utf8 = StandardCharsets.UTF_8;
		List<String> lines = Arrays.asList("1st line", "2nd line");

		try {
			String fileContent =  agentPosition + "\r\n" + teleportalPosition + "\r\n" + obstaclePositions + "\r\n" + rockPositions + "\r\n" + pressurePadPositions +"\r\n" + rowPL + "\r\n" + columnPL ;
		    Files.write(Paths.get("KB.pl"), fileContent.getBytes());
		} catch (IOException e) {
		    e.printStackTrace();
		}

		//printing the grid
		for(int i =0;i<m; i++){
			ArrayList<String> row = new ArrayList<String>();
			for (int j=0;j<n;j++){
				row.add(grid[i][j]);
			}
			System.out.println(row);
		}
		 
		 	

		return grid;
		
		
	}
	
	public static String[][] shuffle(String[][] a) {
		Random random = new Random();

		for (int i = a.length - 1; i > 0; i--) {
			for (int j = a[i].length - 1; j > 0; j--) {
				int m = random.nextInt(i + 1);
				int n = random.nextInt(j + 1);

				String temp = a[i][j];
				a[i][j] = a[m][n];
				a[m][n] = temp;
			}
		}
		return a;
	}
	
	/**
	 * Main testing and printing is here!
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		System.out.println("				Welcome to Safa and Narihan's implementation of HelpR2D2       ");
		System.out.print( "\n\n\n" );
		
		genGrid();

	}

	
}