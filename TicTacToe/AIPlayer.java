package TicTacToe;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer {
    private final Seed aiSeed;
    private final Seed opponentSeed;

    public AIPlayer(Seed aiSeed) {
        this.aiSeed = aiSeed;
        this.opponentSeed = (aiSeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public int[] move(Board board) {
        int[] result = minimax(board, aiSeed);
        return new int[]{result[1], result[2]};
    }

    private int[] minimax(Board board, Seed player) {
        List<int[]> nextMoves = generateMoves(board);
        int bestScore = (player == aiSeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || board.hasWon(aiSeed) || board.hasWon(opponentSeed)) {
            bestScore = score(board);
            return new int[]{bestScore, bestRow, bestCol};
        }

        for (int[] move : nextMoves) {
            board.cells[move[0]][move[1]].content = player;
            if (player == aiSeed) {
                currentScore = minimax(board, opponentSeed)[0];
                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestRow = move[0];
                    bestCol = move[1];
                }
            } else {
                currentScore = minimax(board, aiSeed)[0];
                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestRow = move[0];
                    bestCol = move[1];
                }
            }
            board.cells[move[0]][move[1]].content = Seed.NO_SEED;
        }
        return new int[]{bestScore, bestRow, bestCol};
    }


    private List<int[]> generateMoves(Board board) {
        List<int[]> nextMoves = new ArrayList<>();
        if (board.hasWon(aiSeed) || board.hasWon(opponentSeed)) {
            return nextMoves;
        }
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }
        return nextMoves;
    }

    private int score(Board board) {
        if (board.hasWon(aiSeed)) {
            return 1;
        } else if (board.hasWon(opponentSeed)) {
            return -1;
        } else {
            return 0;
        }
    }
}