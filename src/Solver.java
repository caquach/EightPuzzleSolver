import edu.princeton.cs.algs4.*;

import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    private LinkedStack<Board> solution;
    private int moves;

    // Helper search node class.
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previous;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }

    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("Board does not exist.");
        }
        if (!initial.isSolvable()) {
            throw new IllegalArgumentException("Board is not solvable.");
        }
        solution = new LinkedStack<>();
        MinPQ<SearchNode> pq = new MinPQ<>(new ManhattanOrder());
        SearchNode node = new SearchNode(initial, 0, null);
        pq.insert(node);
        moves = 0;
        while (!pq.isEmpty()) {
            node = pq.delMin();
            if (node.board.manhattan() == 0) {
                moves = node.moves;
                solution.push(node.board);
                SearchNode cur = node.previous;
                while (cur != null) {
                    solution.push(cur.board);
                    cur = cur.previous;
                }
                break;
            } else {
                for (Board neighbor : node.board.neighbors()) {
                    if (node.previous == null || !node.previous.board.equals(neighbor)) {
                        SearchNode n = new SearchNode(neighbor, node.moves + 1, node);
                        pq.insert(n);
                    }
                }
            }

        }
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        Queue<Board> boards = new Queue<>();
        solution.pop(); //gets rid of initial board.
        while (!solution.isEmpty()) {
            boards.enqueue(solution.pop());
        }
        return boards;
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int aNode = a.board.hamming() + a.moves;
            int bNode = b.board.hamming() + b.moves;

            if (aNode > bNode) {
                return 1;
            }
            if (aNode < bNode) {
                return -1;
            }
            return 0;
        }
    }

    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int aNode = a.board.manhattan() + a.moves;
            int bNode = b.board.manhattan() + b.moves;

            if (aNode > bNode) {
                return 1;
            }
            if (aNode < bNode) {
                return -1;
            }
            return 0;
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
