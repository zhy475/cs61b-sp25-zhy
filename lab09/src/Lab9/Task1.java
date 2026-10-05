package Lab9;

import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

public class Task1 {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 20;

    public static void main(String[] args) {
        // 初始化渲染器，指定宽度和高度
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // 创建一个宽30，高15的二维瓦片数组
        TETile[][] world = new TETile[WIDTH][15];

        // 填充世界
        fillWithTrees(world);

        // 渲染世界
        ter.renderFrame(world);
    }

    private static void fillWithTrees(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                world[x][y] = Tileset.TREE;
            }
        }
    }
}