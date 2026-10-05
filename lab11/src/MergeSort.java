public class MergeSort {

    public static int[] sort(int[] arr) {
        // 基本情况：数组长度为0或1时已经有序
        if (arr.length <= 1) {
            return arr;
        }

        // 1. 将数组分成两半
        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        // 2. 递归排序两半
        left = sort(left);
        right = sort(right);

        // 3. 合并已排序的两半
        return merge(left, right);
    }

    private static int[] merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        // 比较两个数组的元素，按顺序放入 c
        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) {
                c[k++] = a[i++];
            } else {
                c[k++] = b[j++];
            }
        }

        // 将剩余的元素复制到 c
        while (i < a.length) {
            c[k++] = a[i++];
        }
        while (j < b.length) {
            c[k++] = b[j++];
        }

        return c;
    }
}