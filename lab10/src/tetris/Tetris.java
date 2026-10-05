package tetris;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

/**
 * Provides the logic for Tetris.
 *
 * @author Erik Nelson, Omar Yu, Noah Adhikari, Jasmine Lin
 */
public class Tetris {

    private static int WIDTH = 10;
    private static int HEIGHT = 20;
    private static int GAME_HEIGHT = 25;

    private TETile[][] board;
    private Movement movement;
    private boolean isGameOver;
    private Tetromino currentTetromino;
    private int score;

    private boolean isGameOver() {
        return isGameOver;
    }

    private void renderBoard() {
        ter.drawTiles(board);
        renderScore();
        StdDraw.show();

        if (auxFilled) {
            auxToBoard();
        } else {
            fillBoard(Tileset.NOTHING);
        }
    }

    private void spawnPiece() {
        if (board[4][19] != Tileset.NOTHING) {
            isGameOver = true;
        }
        currentTetromino = Tetromino.values()[bagRandom.getValue()];
        currentTetromino.reset();
    }

    private void updateBoard() {
        Tetromino t = currentTetromino;
        if (actionDeltaTime() > 1000) {
            movement.dropDown();
            resetActionTimer();
            return;
        }

        // 使用骨架提供的 tryMove(dx, dy) 方法
        while (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'a') {
                movement.tryMove(-1, 0);
            } else if (key == 'd') {
                movement.tryMove(1, 0);
            } else if (key == 's') {
                movement.dropDown();
            } else if (key == 'q') {
                movement.rotateLeft();
            } else if (key == 'w') {
                movement.rotateRight();
            }
        }
    }

    private void incrementScore(int linesCleared) {
        if (linesCleared == 1) {
            score += 100;
        } else if (linesCleared == 2) {
            score += 300;
        } else if (linesCleared == 3) {
            score += 500;
        } else if (linesCleared >= 4) {
            score += 800;
        }
    }

    public void clearLines(TETile[][] tiles) {
        int linesCleared = 0;

        for (int y = 0; y < GAME_HEIGHT; y++) {
            boolean isFull = true;
            for (int x = 0; x < WIDTH; x++) {
                if (tiles[x][y] == Tileset.NOTHING) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                linesCleared++;
                for (int i = y; i < GAME_HEIGHT - 1; i++) {
                    for (int x = 0; x < WIDTH; x++) {
                        tiles[x][i] = tiles[x][i + 1];
                    }
                }
                for (int x = 0; x < WIDTH; x++) {
                    tiles[x][GAME_HEIGHT - 1] = Tileset.NOTHING;
                }
                y--;
            }
        }

        if (linesCleared > 0) {
            incrementScore(linesCleared);
        }

        fillAux();
    }

    public void runGame() {
        resetActionTimer();

        while (!isGameOver()) {
            if (currentTetromino == null) {
                spawnPiece();
                if (isGameOver()) break;
            }

            updateBoard();
            renderBoard();

            // 检查上一个动作是否导致方块落地被固定（被置为 null）
            if (currentTetromino == null) {
                clearLines(auxiliary);
            }
        }

        System.exit(0);
    }

    private void renderScore() {
        StdDraw.setPenColor(255, 255, 255);
        StdDraw.text(7, 19, "Score: " + score);
    }

    public static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : (new Random()).nextLong();
        Tetris tetris = new Tetris(seed);
        tetris.runGame();
    }

    // ========================================================================
    // Everything below here you don't need to touch.
    // ========================================================================

    private final TERenderer ter = new TERenderer();
    private Random random;
    private BagRandomizer bagRandom;
    private long prevActionTimestamp;
    private long prevFrameTimestamp;
    private TETile[][] auxiliary;
    private boolean auxFilled;

    public Tetris() {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(new Random().nextLong());
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    public Tetris(long seed) {
        board = new TETile[WIDTH][GAME_HEIGHT];
        auxiliary = new TETile[WIDTH][GAME_HEIGHT];
        random = new Random(seed);
        bagRandom = new BagRandomizer(random, Tetromino.values().length);
        auxFilled = false;
        movement = new Movement(WIDTH, GAME_HEIGHT, this);

        ter.initialize(WIDTH, HEIGHT);
        fillBoard(Tileset.NOTHING);
        fillAux();
    }

    public TETile[][] getBoard() { return board; }
    public int getScore() { return score; }
    public TETile[][] getAuxiliary() { return auxiliary; }
    public Tetromino getCurrentTetromino() { return currentTetromino; }
    public void setCurrentTetromino() { currentTetromino = null; }
    public void setAuxTrue() { auxFilled = true; }

    private void fillBoard(TETile tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = tile;
            }
        }
    }

    private static void copyArray(TETile[][] src, TETile[][] dest) {
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
        }
    }

    public void fillAux() { copyArray(board, auxiliary); }
    private void auxToBoard() { copyArray(auxiliary, board); }
    private long actionDeltaTime() { return System.currentTimeMillis() - prevActionTimestamp; }
    private void resetActionTimer() { prevActionTimestamp = System.currentTimeMillis(); }
}