public class HeapSort {

    public static void sort(int[] arr) {
        int n = arr.length;

        // 1. 构建最大堆（从最后一个非叶子节点开始向前调整）
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 2. 一个个将堆顶元素（最大值）放到数组末尾
        for (int i = n - 1; i > 0; i--) {
            // 将当前堆顶（最大值）移到末尾
            swap(arr, 0, i);
            // 重新调整剩余的堆
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}