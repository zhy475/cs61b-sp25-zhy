package Lab9;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.Random;

public class Task2 {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithTrees(world);

        Random rand = new Random(2873123); // 使用固定种子以便重现结果

        // 画出5个随机方块
        for (int i = 0; i < 5; i++) {
            addRandomSquare(world, rand);
        }

        ter.renderFrame(world);
    }

    private static void fillWithTrees(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.TREE;
            }
        }
    }

    // 任务2A：画正方形，处理越界问题
    private static void drawSquare(TETile[][] world, int startX, int startY, int size, TETile tile) {
        for (int x = startX; x < startX + size; x++) {
            for (int y = startY; y < startY + size; y++) {
                // 检查是否越界
                if (x >= 0 && x < world.length && y >= 0 && y < world[0].length) {
                    world[x][y] = tile;
                }
            }
        }
    }

    // 任务2B、2C：随机位置、随机大小、随机类型的方块
    private static void addRandomSquare(TETile[][] world, Random rand) {
        // 随机大小 3 到 7 (不包含8)
        int size = RandomUtils.uniform(rand, 3, 8);

        // 随机X和Y坐标 (确保左上角在边界内)
        int startX = RandomUtils.uniform(rand, 0, world.length - size + 1);
        int startY = RandomUtils.uniform(rand, 0, world[0].length - size + 1);

        // 随机方块类型 (使用 switch 语句)
        int type = RandomUtils.uniform(rand, 0, 3);
        TETile tile;
        switch (type) {
            case 0:
                tile = Tileset.FLOWER;
                break;
            case 1:
                tile = Tileset.WALL;
                break;
            case 2:
            default:
                tile = Tileset.WATER;
                break;
        }

        drawSquare(world, startX, startY, size, tile);
    }
}