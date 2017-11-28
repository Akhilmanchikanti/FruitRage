import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FruitRage
{
	public static class BoardCluster
	{
		int count;
		int row;
		int col;
		
		public BoardCluster(int fruitCount, int i, int j)
		{
			count = fruitCount;
			row = i;
			col = j;
		}
	}
	
	static int boardSize ;
	static int fruitTypes;
	static float timeForGame;
	static int visited[][];
	static int myCount = 0;
	public static void gravity(char board[][])
	{
		for (int row = 0; row < boardSize; row++)
		{
			int count = 0, k = boardSize - 1, col;
			for (col = boardSize - 1; col >= 0; col--)
			{
				if (board[col][row] == '*')
				{
					count++;
				}
				else
				{
					board[k][row] = board[col][row];
					k--;
				}
			}
			
			count--;
			while (count >= 0)
			{
				board[count][row] = '*';
				count--;
			}
		}
	}
	
	
	public static int dfs_counts(char boardT[][], int row, int col, int count, char val)
	{
		if ( (col + 1 < boardSize) && (visited[row][col + 1] == 0) && (boardT[row][col + 1] == val))
		{
			visited[row][col + 1] = 1;
			count++;
			boardT[row][col+1] = '-';
			count = dfs_counts(boardT, row, col+1, count, val);
		}
		
		if ( (row + 1 < boardSize) && (visited[row+1][col] == 0) && (boardT[row + 1][col] == val))
		{
			visited[row + 1][col] = 1;
			count++;
			boardT[row+1][col] = '-';
			count= dfs_counts(boardT, row+1, col, count, val);
		}
		
		if ( (col - 1 >= 0) && (visited[row][col - 1] == 0)  && (boardT[row][col - 1] == val))
		{
			visited[row][col - 1] = 1;
			count++;
			boardT[row][col-1] = '-';
			count= dfs_counts(boardT, row, col-1, count, val);
		}
		
		if ( (row - 1 >= 0) && (visited[row - 1][col] == 0)  && (boardT[row - 1][col] == val))
		{
			visited[row - 1][col] = 1;
			count++;
			boardT[row-1][col] = '-';
			count= dfs_counts(boardT, row-1, col, count, val);
		}
		return count;
	}
	
	public static int dfs_count (char boardT[][], int row, int col, int count, char val)
	{
		if ( (col + 1 < boardSize) && (visited[row][col + 1] == 0) && (boardT[row][col + 1] == val))
		{
			visited[row][col + 1] = 1;
			count++;
			boardT[row][col+1] = '*';
			count = dfs_count(boardT, row, col+1, count, val);
		}
		
		if ( (row + 1 < boardSize) && (visited[row+1][col] == 0) && (boardT[row + 1][col] == val))
		{
			visited[row + 1][col] = 1;
			count++;
			boardT[row+1][col] = '*';
			count= dfs_count(boardT, row+1, col, count, val);
		}
		
		if ( (col - 1 >= 0) && (visited[row][col - 1] == 0)  && (boardT[row][col - 1] == val))
		{
			visited[row][col - 1] = 1;
			count++;
			boardT[row][col-1] = '*';
			count= dfs_count(boardT, row, col-1, count, val);
		}
		
		if ( (row - 1 >= 0) && (visited[row - 1][col] == 0)  && (boardT[row - 1][col] == val))
		{
			visited[row - 1][col] = 1;
			count++;
			boardT[row-1][col] = '*';
			count= dfs_count(boardT, row-1, col, count, val);
		}
		return count;
	}
	
	public static char[][] deepClone()
	{
		char[][] temp = new char[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++)
		{
		     temp[i] = Arrays.copyOf(board[i], boardSize);
		}
		
		return temp;
	}
	
	public static char[][] deepClone(char t[][])
	{
		char[][] temp = new char[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++)
		{
		     temp[i] = Arrays.copyOf(t[i], boardSize);
		}
		
		return temp;
	}
	
	static int miniCount = 0;
	public static ArrayList<BoardCluster> generate_children(char x[][])
	{
		visited = new int[boardSize][boardSize];
		ArrayList<BoardCluster> clusters = new ArrayList<BoardCluster>();
		for (int row = 0; row < boardSize; row++)
		{
			for (int col = boardSize - 1; col >= 0; col--)
			{
				int count = 0;
				if (x[col][row] == '*')
				{
					break;
				}
				if (visited[col][row] == 0)
				{
					count++;
					visited[col][row] = 1;
					int g = dfs_counts(x, col, row, count, x[col][row]);
					x[col][row] = '-';
					clusters.add(new BoardCluster(g, col, row));			
				}
			}
		}
		return clusters;
	}
	public static HashMap<String, Integer> miniMax(
			char x[][], 
			BoardCluster bcs, 
			int playerCount, 
			boolean plT, 
			int alpha, 
			int beta,
			int maxScore,
			int minScore)
	{
		miniCount++;
		
		if (playerCount == 0 || isBoardDone(x))
		{
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			hm.put("Score", maxScore-minScore);
			hm.put("Row", bcs.row);
			hm.put("Col", bcs.col);
			
			return hm;
		}
		
		char tempB[][] = deepClone(x);
		visited = new int[boardSize][boardSize];
		ArrayList<BoardCluster> childrens = generate_children(tempB);
		Collections.sort(childrens, new Comparator<BoardCluster>() {
			@Override
			public int compare (BoardCluster b, BoardCluster c)
			{
				return Double.compare(c.count, b.count);
			}
		});
		if (plT)
		{
			int v = Integer.MIN_VALUE;
			HashMap<String, Integer> hmm = new HashMap<String, Integer>();
			hmm.put("Score", v);
			for (BoardCluster cluster: childrens)
			{
				visited = new int[boardSize][boardSize];
				char tempBB[][] = deepClone(x);
				dfs_count(tempBB, cluster.row, cluster.col, 0, tempBB[cluster.row][cluster.col]);
				tempBB[cluster.row][cluster.col] = '*';
				maxScore = maxScore + (cluster.count * cluster.count);
				gravity(tempBB);
				HashMap<String, Integer> temp = miniMax(tempBB, cluster, playerCount-1,false, alpha, beta, maxScore, minScore);
				maxScore = maxScore - (cluster.count*cluster.count);
				if (temp.get("Score") > hmm.get("Score"))
				{
					hmm.put("Score", temp.get("Score"));
					hmm.put("Row", cluster.row);
					hmm.put("Col", cluster.col);
				}
				
				alpha = Math.max(alpha, hmm.get("Score"));
				
				if (beta <= alpha)
				{
					break;
				}
			}
			
			return hmm;
		}
		else
		{
			int v = Integer.MAX_VALUE;
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			hm.put("Score", v);
			for (BoardCluster cluster: childrens)
			{
				visited = new int[boardSize][boardSize];
				char tempBB[][] = deepClone(x);
				dfs_count(tempBB, cluster.row, cluster.col, 0, tempBB[cluster.row][cluster.col]);
				tempBB[cluster.row][cluster.col] = '*';
				minScore = minScore+ (cluster.count * cluster.count);
				gravity(tempBB);
				HashMap<String, Integer> temp = miniMax(tempBB, cluster, playerCount-1,true, alpha, beta, maxScore, minScore);
				minScore = minScore - (cluster.count*cluster.count);
				if (temp.get("Score") < hm.get("Score"))
				{
					hm.put("Score", temp.get("Score"));
					hm.put("Row", cluster.row);
					hm.put("Col", cluster.col);
				}
				
				beta = Math.min(beta, hm.get("Score"));
				
				if (beta <= alpha)
				{
					break;
				}
			}
			
			return hm;
		}
	}
	
	public static boolean isBoardDone(char b[][])
	{
		for (int row = boardSize - 1; row >= 0; row--)
		{
			for (int col = boardSize - 1; col >= 0;)
			{
				if (b[col][row] != '*')
				{
					return false;
				}
				else
				{
					break;
				}
			}
		}
		
		return true;
	}
	public static char board[][];
	public static void main(String[] args) throws IOException
	{
		Long startTime = System.currentTimeMillis();
		
		FileReader fr = new FileReader("input.txt");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> lines= new ArrayList<String>();
		String line ;
		while((line = br.readLine())!=null)
		{
			lines.add(line);
		}
		br.close();
		
		boardSize = Integer.parseInt(lines.get(0));
		fruitTypes = Integer.parseInt(lines.get(1));
		timeForGame = Float.parseFloat(lines.get(2));
		
		board = new char[boardSize][boardSize];
		for (int i=0; i<boardSize; i++)
		{
			String row= lines.get(i+3); 
			for (int j=0; j<boardSize; j++)
			{
				board[i][j] = row.charAt(j);
			}
		}
		
		char t[][]= deepClone();
		HashMap<String, Integer>h = miniMax(t, new BoardCluster(0, 0, 0),1,true, Integer.MIN_VALUE , Integer.MAX_VALUE,0, 0);
		int depth = 5;
		if (timeForGame <= 5)
		{
			depth = 1;
		}
		else
		if (timeForGame <= 10)
		{
			depth = 2;
		}
		else
		if (miniCount == (boardSize*boardSize + 1))
		{
			depth = 2;
		}
		else
		if (timeForGame > 30 && (miniCount >= (boardSize*boardSize - 100) && miniCount <= (boardSize*boardSize+1)) && (boardSize >= 9 && boardSize<= 18))
		{
			depth = 4;
		}
		else
		if (timeForGame > 30 && boardSize >= 19 && boardSize<= 26)
		{
			depth = 4;
		}
		else
		if (timeForGame <= 30)
		{
			depth = 3;
		}
		miniCount = 0;
		if (depth != 1)
		{
			h = miniMax(t, new BoardCluster(0, 0, 0),depth,true, Integer.MIN_VALUE , Integer.MAX_VALUE,0, 0);
		}
		char c = (char) (h.get("Col") + 65);
		int l = h.get("Row")+1;
		visited = new int [boardSize][boardSize];
		char ch = board[h.get("Row")][h.get("Col")];
		board[h.get("Row")][h.get("Col")] = '*';
		visited[h.get("Row")][h.get("Col")] = 1;
		dfs_count(board, h.get("Row"), h.get("Col"), 1, ch);
		gravity(board);
		writeToFile(""+c+l, board);
	}
	
	public static void writeToFile(String move, char board[][])
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
			writer.write(move);
			writer.newLine();
			for (int i=0; i<boardSize; i++)
			{
				StringBuilder sb = new StringBuilder();
				for (int j=0;j<boardSize; j++)
				{
					sb.append(board[i][j]+"");
				}
				writer.append(sb.toString());
				writer.newLine();
			}
			
			writer.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

}
