package player;

/**
 *  Color class defines different values of colors.
 **/

public class Color {

    public final static int BLACK = 0;
    public final static int WHITE = 1;
    public final static int SPACE = 2;

    public static String toString(int color) {
        if (color == Color.SPACE) {
            return "    ";
        } else if (color == Color.WHITE) {
            return " WW ";
        } else {
            return " BB ";
        }
    }
}
