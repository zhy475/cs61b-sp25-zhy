package Lab9;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.Random;

public class Task3 {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithTrees(world);

        Random rand = new Random(2873123);
        int squareCount = 0; // 记录方块数量

        boolean gameRunning = true;
        while (gameRunning) {
            // 渲染当前世界
            ter.renderFrame(world);

            // 任务3B：在左上角打印方块数量
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(1, HEIGHT - 1, "Number of squares: " + squareCount);

            // 检查键盘输入
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n') {
                    // 按 n 添加方块
                    addRandomSquare(world, rand);
                    squareCount++;
                } else if (key == 'q') {
                    // 按 q 退出程序
                    gameRunning = false;
                }
            }

            // 短暂暂停以避免闪烁和CPU占用过高
            StdDraw.pause(20);
        }

        System.exit(0);
    }

    private static void fillWithTrees(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.TREE;
            }
        }
    }

    private static void drawSquare(TETile[][] world, int startX, int startY, int size, TETile tile) {
        for (int x = startX; x < startX + size; x++) {
            for (int y = startY; y < startY + size; y++) {
                if (x >= 0 && x < world.length && y >= 0 && y < world[0].length) {
                    world[x][y] = tile;
                }
            }
        }
    }

    private static void addRandomSquare(TETile[][] world, Random rand) {
        int size = RandomUtils.uniform(rand, 3, 8);
        int startX = RandomUtils.uniform(rand, 0, world.length - size + 1);
        int startY = RandomUtils.uniform(rand, 0, world[0].length - size + 1);

        int type = RandomUtils.uniform(rand, 0, 3);
        TETile tile;
        switch (type) {
            case 0: tile = Tileset.FLOWER; break;
            case 1: tile = Tileset.WALL; break;
            case 2: default: tile = Tileset.WATER; break;
        }

        drawSquare(world, startX, startY, size, tile);
    }
}