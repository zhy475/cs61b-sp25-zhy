public class QuickSort {

    public static int[] sort(int[] arr) {
        quickSort(arr, 0, arr.length);
        return arr;
    }

    private static void quickSort(int[] arr, int start, int end) {
        // 基本情况：子数组为空或只有一个元素
        if (end - start <= 1) {
            return;
        }

        // 获取切分后的边界 [lt_end, gt_start]
        int[] bounds = partition(arr, start, end);

        // 递归排序小于区和大于区（等于区已经在正确位置了）
        quickSort(arr, start, bounds[0]);
        quickSort(arr, bounds[1], end);
    }

    private static int[] partition(int[] arr, int start, int end) {
        int pivot = arr[start];
        int lt = start;       // arr[start..lt-1] < pivot
        int gt = end;         // arr[gt..end-1] > pivot
        int i = start + 1;    // arr[lt..i-1] == pivot

        while (i < gt) {
            if (arr[i] < pivot) {
                swap(arr, i, lt);
                i++;
                lt++;
            } else if (arr[i] > pivot) {
                gt--;
                swap(arr, i, gt);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}