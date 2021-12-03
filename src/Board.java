import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Inversions;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    // private int hamming;
    // private int manhattan;

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }

        // hamming = hamming();
        // manhattan = manhattan();
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
        int index = 1;
        int outOfPlace = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i == N - 1 && j == N - 1)) {
                    if (tileAt(i, j) != 0) {
                        outOfPlace++;
                    }
                } else {
                    if (tileAt(i, j) != index && (i != N - 1 && j != N - 1)) {
                        outOfPlace++;
                    }
                }
                index++;
            }

        }

        return outOfPlace;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        int sum = 0;
        int index = -1;
        int x = 0;
        int y = 0;
        int netX = 0;
        int netY = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != 0) {
                    index = tileAt(i, j) - 1;
                    y = ((int) (index / N));
                    x = (index - (y * N));

                    netX = Math.abs(j - x);
                    netY = Math.abs(i - y);

                    sum += (netX + netY);
                }
            }
        }

        return sum;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        int index = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != index
                        && (i != N - 1 && j != N - 1)) {
                    return false;
                }
                index++;
            }
        }

        return true;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        if (size() % 2 == 0) {
            return (inversions() + (int) (blankPos() / N)) % 2 == 1;
        } else {
            return inversions() % 2 == 0;
        }
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tileAt(i, j) != that.tileAt(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<>();
        int[][] cloneBoard = cloneTiles();
        int blank = blankPos();
        int y = ((int) (blank / N));
        int x = (blank - (y * N));

        if (y <= N - 1 && y > 0) {
            swapPos(cloneBoard, x, x, y, y - 1);

            Board swapped = new Board(cloneBoard);
            boards.enqueue(swapped);

            swapPos(cloneBoard, x, x, y, y - 1);
        }
        if (x >= 0 && x < N - 1) {
            swapPos(cloneBoard, x, x + 1, y, y);

            Board swapped = new Board(cloneBoard);
            boards.enqueue(swapped);

            swapPos(cloneBoard, x, x + 1, y, y);
        }
        if (y >= 0 && y < N - 1) {
            swapPos(cloneBoard, x, x, y, y + 1);

            Board swapped = new Board(cloneBoard);
            boards.enqueue(swapped);

            swapPos(cloneBoard, x, x, y, y + 1);
        }
        if (x <= N - 1 && x > 0) {
            swapPos(cloneBoard, x, x - 1, y, y);

            Board swapped = new Board(cloneBoard);
            boards.enqueue(swapped);

            swapPos(cloneBoard, x, x - 1, y, y);
        }

        return boards;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that swaps the position of two tiles.
    private void swapPos(int[][] board, int x1, int x2, int y1, int y2) {
        int swap = board[y1][x1];
        board[y1][x1] = board[y2][x2];
        board[y2][x2] = swap;

    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() {
        int addressOfZero = -1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.tileAt(i, j) == 0) {
                    addressOfZero = j + (i * N);
                }
            }
        }

        return addressOfZero;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int[] order = new int[(N * N) - 1];
        int index = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != 0) {
                    order[index] = tileAt(i, j);
                    index++;
                }
            }
        }

        return (int) Inversions.count(order);

    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() {
        int[][] cloneBoard = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cloneBoard[i][j] = tiles[i][j];
            }
        }

        return cloneBoard;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
