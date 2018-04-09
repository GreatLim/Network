package player;
import list.*;

/**
 *  Chip Class records Chips' information
 **/


public class Chip {

    public int color; // the value of color are stored in Color Class.
    public int x;
    public int y;
   

    
    public boolean equals(Object c) {

        return ((Chip)c).color == color && ((Chip)c).x == x && ((Chip)c).y == y;

    }

    public Chip(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
    }


    /**
     *  find the chips (of the same color) that form connections with a chip
     *  @param b is an Game board
     *  @return DList that stores chips that connect with a "this" chip.
     **/
    public DList findPair(Board b) {
        DList result = new DList();
        if(color == Color.SPACE) {
            return null;
        } else {
            DList l1 = findHorizonPair(b);
            DList l2 = findVerticalPair(b);
            DList l3 = findDiagonal1Pair(b);
            DList l4 = findDiagonal2Pair(b);
            result = l1.connect(l2).connect(l3).connect(l4);
        }
        return result;
    }


    public DList findHorizonPair(Board b) {
        DList result = new DList();
        for(int i = x + 1; i < 8; i++) {
            int temp = b.grid[i][y].color;
            if(temp == color) {
                result.insertBack(new Chip(i, y , temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }

        for(int i = x - 1; i >= 0; i--) {
            int temp = b.grid[i][y].color;
            if(temp == color) {
                result.insertBack(new Chip(i, y , temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }
        return result;
    }

    public DList findVerticalPair(Board b) {
        DList result = new DList();
        for(int j = y + 1; j < 8; j++) {
            int temp = b.grid[x][j].color;
            if(temp == color) {
                result.insertBack(new Chip(x, j , temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }

        for(int j = y - 1; j >= 0; j--) {
            int temp = b.grid[x][j].color;
            if(temp == color) {
                result.insertBack(new Chip(x, j , temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }
        return result;
    }

    public DList findDiagonal1Pair(Board b) {
        DList result = new DList();
        for(int i = x + 1, j = y + 1; i < 8 && j < 8; i++, j++) {
            int temp = b.grid[i][j].color;
            if(temp == color) {
                result.insertBack(new Chip(i, j, temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }

        for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
            int temp = b.grid[i][j].color;
            if(temp == color) {
                result.insertBack(new Chip(i, j, temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }
        return result;
    }

    public DList findDiagonal2Pair(Board b) {
        DList result = new DList();
        for(int i = x + 1, j = y - 1; i < 8 && j >= 0; i++, j--) {
            int temp = b.grid[i][j].color;
            if(temp == color) {
                result.insertBack(new Chip(i, j, temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }

        for(int i = x - 1, j = y + 1; i >= 0 && j < 8; i--, j++) {
            int temp = b.grid[i][j].color;
            if(temp == color) {
                result.insertBack(new Chip(i, j, temp));
                break;
            } else if(temp == Color.SPACE) {
                continue;
            } else {
                break;
            }
        }
        return result;
    }


    
    protected boolean isVisited(boolean[][] key)
    {
    	 	return key[x][y];    	 	
    }
    protected void marker(boolean[][] key)
    {
    		key[x][y] = true;
    		
    }
    protected void unmarker(boolean[][] key)
    {
    		key[x][y] = false;
    }
    public String toString(){
        String s;
        s = "( " + x + " , " + y + " , " + Color.toString(color) +")";
        return s;
    }

    
   
    
    
    public static void main(String[] args) {
        Board b = new Board();
        b.grid[4][4].color = Color.WHITE;
        b.grid[5][5].color = Color.BLACK;
        b.grid[4][2].color = Color.WHITE;
        b.grid[4][6].color = Color.WHITE;
        b.grid[2][2].color = Color.WHITE;
        b.grid[6][6].color = Color.WHITE;
        b.grid[6][2].color = Color.WHITE;
        b.grid[6][4].color = Color.WHITE;
        b.grid[2][4].color = Color.WHITE;
        b.grid[2][6].color = Color.WHITE;
        DList l = b.grid[2][6].findPair(b);
        b.printBoard();
        System.out.println("\nPairs of " + b.grid[2][6] +" is " + l);
    }

}
