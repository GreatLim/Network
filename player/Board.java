/* Board.java*/

package player;

import list.*;

import java.io.*;

/**
 * Board class implements an 8 * 8 game board with Chip for each cell
 **/

public class Board {

    public final static int DIMENSION = 8;

    public Chip[][] grid = new Chip[DIMENSION][DIMENSION];
    public static int bChipCount = 0;//count the number of black chips used.
    public static int wChipCount = 0;//count the number of white chips used.
    

    public Board() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Chip(i, j, Color.SPACE);
            }
        }
    }

    /**
     * set a chip on the game board
     *
     * @param c a chip that will be set
     **/
    //discuss about validity of Chip 
    public void setChip(Chip c) {
        grid[c.x][c.y] = c;
    }

    /**
     * delete a chip on the game board
     *
     * @param c a chip that will be deleted
     **/

    public void delChip(Chip c) {
        grid[c.x][c.y].color = Color.SPACE;
    }




    /**
     * set the Board for
     *
     * @param m     Move
     * @param color
     */

    //suppose Move= Add or Step only, not quit
    protected Board setBoard(Move m, int color) {
        Chip c = new Chip(m.x1, m.y1, color);//the amount of chip is wrong ????
        //if chip c is not used, we should change it to "new Chip(m.x1, m.y1, color);"
        if (m.moveKind == Move.STEP) {
            grid[m.x2][m.y2].color = Color.SPACE;
            Chip c2 = new Chip(m.x2, m.y2, color);//why?
        }
        grid[m.x1][m.y1].color = color;
        return this;
    }

    
    protected void restoreBoard(Move m, int color) {
        // m1 is a move that reverses m
        if (m.moveKind == Move.STEP) {
            Move m1 = new Move(m.x2, m.y2, m.x1, m.y1);
            setBoard(m1, color);
        } else {
            grid[m.x1][m.y1].color = Color.SPACE;
        }
    }


    /**
     * find the best move
     *
     * @param side        is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
     * @param alpha       is the score that MachinePlayer.COMPUTER knows it can achieve(it should be initialized with -46 )
     * @param beta        is the score that MachinePlayer.OPPONENT knows it can achieve(it should be initialized with 46 )
     * @param searchDepth is depth that this recursion can achieve
     * @param mark        is used to record searchDepth (it should be initialized with 0)
     * @return Best objection that stores best move
     */



    private DList generateChipList(int color) {
        DList ChipList = new DList();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (grid[i][j].color == color) {
                    ChipList.insertBack(grid[i][j]);
                }
            }
        }
        return ChipList;


    }

    /**
     * Be used in isValidMove() Test if a player have more than two chips in a connected group, whether
     * connected orthogonally or diagonally.
     *
     * @param side
     * @param m
     * @return true if move "m" of player "side" makes three chips in a connected group in "this" GameBoard; otherwise,
     * false
     */


    private boolean isConnected(int color, Move m) {
        //moveKind is supposed to be STEP or ADD
        if (m.moveKind == Move.STEP) {
            grid[m.x2][m.y2].color = Color.SPACE;
        }
        grid[m.x1][m.y1].color = color;
        ListNode n = generateChipList(color).front();
        try {
            while (n.isValidNode()) {
                if (isConnected(color, (Chip) n.item())) {
                    if (m.moveKind == Move.STEP) {
                        grid[m.x2][m.y2].color = color;
                    }
                    grid[m.x1][m.y1].color = Color.SPACE;
                    return true;
                }
                n = n.next();
            }
            if (m.moveKind == Move.STEP) {
                grid[m.x2][m.y2].color = color;
            }
            grid[m.x1][m.y1].color = Color.SPACE;
            return false;
        } catch (InvalidNodeException e) {
            return false;
        }
    }

    private boolean isConnected(int color, Chip c) {
        if (color == Color.SPACE) {
            return false;
        }
        int flag = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (c.x + i < 8 && c.x + i >= 0 && c.y + j < 8 && c.y + j >= 0) {
                    if (grid[c.x + i][c.y + j].color == color) {
                        flag++;
                    }
                }
            }
        }
        if (flag >= 3) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * isValidMove() determines whether move "m" of player "side" is a valid move on "this" Game
     * Board
     * <p>
     * Unusual conditions:
     * If side is neither MachinePlayer.COMPUTER nor MachinePlayer.OPPONENT, returns false.
     * If GameBoard squares contain illegal values, the behavior of this, method is undefined
     *
     * @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
     * @return true if move "m" of player "side" is a valid move in "this" GameBoard; otherwise,
     * false
     **/

    protected boolean isValidMove(int color, Move m) {
        // is the moveKind is not Valid the method is undefined
        //Quit is always valid;
        if (m.moveKind == Move.QUIT) {
            return true;
        }
        
        //step condition
         if(color == Color.BLACK && bChipCount <10 && m.moveKind == Move.STEP){
        		return false;
        	}
         if(color == Color.WHITE && wChipCount <10 && m.moveKind == Move.STEP){
     		return false;
     	}
        
        //rule3
        if ((m.x1 == 0 && m.y1 == 0) || (m.x1 == 0 && m.y1 == 7) || (m.x1 == 7 && m.y1 == 0) || (m.x1 == 7 && m.y1 == 7)) {
            return false;
        }

        //rule2
        if ((color == Color.WHITE && (m.y1 == 0 || m.y1 == 7)) || (color == Color.BLACK && (m.x1 == 0 || m.x1 == 7))) {
            return false;
        }

        //rule1
        if (grid[m.x1][m.y1].color != Color.SPACE) {
            return false;
        }
        
        
        //rule4
        if (isConnected(color, m)) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * generateValidMove() generates a list of all valid moves of player "side" on "this" Game Board
     * <p>
     * Unusual conditions:
     * If side is neither MachinePlayer.COMPUTER nor MachinePlayer.OPPONENT, returns false.
     * If GameBoard squares contain illegal values, the behavior of this, method is undefined
     *
     * @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
     * @return valid moves are returned in the "item" of the return DList
     **/


    public DList generateValidMove(int color) {

        DList l = new DList();
        //add if haveLeftChip()=true
        //System.out.print(haveLeftChip(color));
        if (haveLeftChip(color)) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Move m = new Move(x, y);
                    if (isValidMove(color, m)) {
                        l.insertFront(m);
                    }
                }
            }
        }
        //step
        //if chip.color==color, x=chip.x, y..;

        else {
        		try {
                ListNode n = generateChipList(color).front();
                while (n.isValidNode()) {
                    int x = ((Chip) n.item()).x,
                            y = ((Chip) n.item()).y;
                    grid[x][y].color = Color.SPACE;
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                        		Move m = new Move(i,j,x,y);
                        		 if (isValidMove(color, m)) {
                                     l.insertFront(m);
                                 }
                        }
                     }

                    n = n.next();
                }

            } catch (InvalidNodeException e) {
                System.out.println(e);
            }
        }
        
        if(l == null)
    			System.out.println("error!");
        return l;
    }

    private boolean haveLeftChip(int color) {
     	bChipCount = 0;
     	wChipCount = 0;
    		for(int i=0;i<8;i++)
    		{
    			for(int j=0;j<8;j++) {
    				if (grid[i][j].color == Color.BLACK) {
    		            bChipCount++;
    		        } 
    				if (grid[i][j].color == Color.WHITE) {
    		            wChipCount++;
    		        } 
    			}
    		}
        if (color == Color.BLACK) {
            return bChipCount < 10;
        } else {
            return wChipCount < 10;
        }
    }

    DList start(int color) {
        if (color == Color.SPACE) {
            return null;
        }

        DList l = new DList();
        if (color == Color.BLACK) {
            for (int i = 0; i < 8; i++) {
                if (grid[i][0].color == color) {
                    l.insertBack(grid[i][0]);
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (grid[0][i].color == color) {
                    l.insertBack(grid[0][i]);
                }
            }
        }
        return l;
    }

    private boolean isEndpoint(Chip c, int color) {
        if (color == Color.SPACE) {
            return false;
        }

        if (color == Color.BLACK) {
            if (c.y == 7) {
                return true;
            }
        } else {
            if (c.x == 7) {
                return true;
            }
        }
        return false;
    }

    private boolean isStartpoint(Chip c, int color) {
        if (color == Color.SPACE) {
            return false;
        }

        if (color == Color.BLACK) {
            if (c.y == 0) {
                return true;
            }
        } else {
            if (c.x == 0) {
                return true;
            }
        }
        return false;
    }

    //c1->c2->c3
    private boolean isTurning(Chip c1, Chip c2, Chip c3) {
        if (direction(c1, c2) == direction(c2, c3)) {
            return false;
        } else {
            return true;
        }

    }

    //c1,c2 is a pair;
    //c1->c2
    //return -1 for vertical; 1 for horizontal; 
    // 2 for diagonal left_down && right_upper; 
    //-2 for diagonal left_upper && right_down; 
    // 0 for not connect
    private int direction(Chip c1, Chip c2) {
        if (c1.x == c2.x) {
            return -1;
        } else if (c1.y == c2.y) {
            return 1;
        } else if ((c1.x - c1.y) == (c2.x - c2.y)) {
            return -2;
        } else if ((c1.x + c1.y) == (c2.x + c2.y)) {
            return 2;
        } else {
            return 0;
        }

    }

      
    boolean findPath(ListNode u, ListNode v, int color, boolean[][] key, int step) {
        try {
            ListNode w = ((Chip) v.item()).findPair(this).front();
            while (w.isValidNode()) {
                if (isTurning((Chip) u.item(), (Chip) v.item(), (Chip) w.item())) {
                    //w is not visited
                    if (!((Chip) w.item()).isVisited(key)) {
                        ((Chip) w.item()).marker(key);
                        //w is not an end point
                        if (!isEndpoint((Chip) w.item(), color)) {
                            if (findPath(v, w, color, key, step + 1)) {
                                return true;
                            }else {
                            		((Chip) w.item()).unmarker(key);
                            }
                        }
                        //w is an end point
                        else {
                            if (step >= 5) {
                                return true;
                            }else {
                            		((Chip) w.item()).unmarker(key);
                            }
                        }
                    } 
                }                
                w = w.next();
            }
        } catch (InvalidNodeException e) {
            return false;
        }
        return false;
    }

    public void printBoard() {
        int count = 0;
        System.out.println("-----------------------------------------");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print("|" + Color.toString(grid[j][i].color));
                count++;
                if (count % 8 == 0) {
                    System.out.println("| _" + i);
                    System.out.println("-----------------------------------------");
                }
            }
        }
        System.out.println("  0_   1_   2_   3_   4_   5_   6_   7_\n");
        System.out.println("bChipCount: "+bChipCount);
        System.out.println("wChipCount: "+wChipCount);
    }

    public String toString() {
        String s = "[ ";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(grid[i][j].color == Color.WHITE || grid[i][j].color == Color.BLACK) {
                    s = s + grid[i][j] + " ";
                }
            }
        }
        s += "]";
        return s;
    }

    public void testIsValidMove() {
        BufferedReader keyBoard =
                new BufferedReader(new InputStreamReader(System.in));


        //set a Move        
        int color = Color.WHITE;
        System.out.println("the color of the new movement is: " + Color.toString(color));
        System.out.println("make a new movement");
        System.out.println("Valid commands are: " +
                "add, step, quit");
        System.out.print("-->");
        try {
            String moveKind = keyBoard.readLine();
            if (moveKind.equals("quit")) {
                Move m = new Move();
                System.out.println("the movement is valid: " + isValidMove(color, m));
            }
            while (!moveKind.equals("quit")) {
                if (moveKind.equals("add")) {
                    System.out.println("input position in which a chip is being added");
                    System.out.print("input x-coordinates index-->");
                    String x1 = keyBoard.readLine();
                    System.out.print("input y-coordinates index-->");
                    String y1 = keyBoard.readLine();
                    Move m = new Move(Integer.valueOf(x1).intValue(), Integer.valueOf(y1).intValue());
                    System.out.println(m.toString());
                    //The move is or not valid
                    System.out.println("the movement is valid: " + isValidMove(color, m));
                } else if (moveKind.equals("step")) {
                    System.out.println("input position in which a chip is being stepped");
                    System.out.print("input x-coordinates index of new position-->");
                    String x1 = keyBoard.readLine();
                    System.out.print("input y-coordinates index of new position-->");
                    String y1 = keyBoard.readLine();
                    System.out.print("input x-coordinates index of old position-->");
                    String x2 = keyBoard.readLine();
                    System.out.print("input y-coordinates index of old position-->");
                    String y2 = keyBoard.readLine();
                    Move m = new Move(Integer.valueOf(x1).intValue(), Integer.valueOf(y1).intValue(),
                            Integer.valueOf(x2).intValue(), Integer.valueOf(y2).intValue());
                    System.out.println(m.toString());
                    //The move is or not valid
                    System.out.println("the movement is valid: " + isValidMove(color, m));
                } else {
                    System.err.println("Invalid move: " + moveKind);
                }
                System.out.println("-->");
                moveKind = keyBoard.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void testGenerateValidMove() {
        int color = Color.WHITE;
        System.out.println("the color of the new movement is: " + Color.toString(color));
        System.out.println(generateValidMove(color).toString());
    }

   


    public static void main(String[] args) {
        //set a Board
        System.out.println("start to set a Board");

        Board b = new Board();

        b.grid[4][4].color = Color.WHITE;
        b.grid[1][1].color = Color.BLACK;
        b.grid[4][2].color = Color.WHITE;
        b.grid[4][6].color = Color.BLACK;
        b.grid[6][6].color = Color.WHITE;
        b.grid[2][3].color = Color.BLACK;
        
        //System.out.println("\n------ test generateValidMove() ------");
        //b.testGenerateValidMove();

        b.grid[6][4].color = Color.WHITE;
        b.grid[4][7].color = Color.BLACK;
        
        //System.out.println("\n------ test generateValidMove() ------");
        //b.testGenerateValidMove();

        b.grid[6][2].color = Color.WHITE;

        b.grid[2][4].color = Color.BLACK;
        b.grid[7][2].color = Color.WHITE;
        b.grid[2][0].color = Color.BLACK;

        b.printBoard();
        //System.out.println("------ test isValidMove() ------");
        //b.testIsValidMove();
        //System.out.println("\n------ test generateValidMove() ------");
        //b.testGenerateValidMove();
        
        System.out.println("\n------ test hasValidNetwork() ------");
//        MachinePlayer p = new MachinePlayer(Color.WHITE);
//        p.board.grid[1][1].color = Color.WHITE;
//        p.board.grid[2][1].color = Color.WHITE;
//        p.board.grid[4][1].color = Color.WHITE;
//        p.board.grid[5][1].color = Color.WHITE;
//        p.board.grid[7][1].color = Color.WHITE;
//        p.board.grid[0][2].color = Color.WHITE;
//        System.out.println(p.hasValidNetwork(MachinePlayer.COMPUTER));

    }
}
