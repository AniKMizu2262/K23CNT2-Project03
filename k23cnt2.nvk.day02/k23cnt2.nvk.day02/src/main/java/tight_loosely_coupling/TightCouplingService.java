package tight_loosely_coupling;

import java.util.Arrays;

public class TightCouplingService {

    // 1. Khai báo thuộc tính
    private BubbleSortAlgorithm bubbleSortAlgorithm;

    // 2. Hàm khởi tạo (Constructor) - Nằm trực tiếp trong class
    // Đây là "Tight Coupling"
    public TightCouplingService() {
        this.bubbleSortAlgorithm = new BubbleSortAlgorithm();
    }

    // Constructor thứ 2 (dùng cho Loose Coupling)
    public TightCouplingService(BubbleSortAlgorithm bubbleSortAlgorithm) {
        this.bubbleSortAlgorithm = bubbleSortAlgorithm;
    }

    // 3. Phương thức - Nằm trực tiếp trong class
    public void complexBusinessSort(int[] arr) {
        bubbleSortAlgorithm.sort(arr); // Gọi thuật toán
        Arrays.stream(arr).forEach(System.out::println); // In kết quả
    }

    // 4. Hàm main - Nằm trực tiếp trong class
    public static void main(String[] args) {
        // Sử dụng constructor mặc định (Tight Coupling)
        TightCouplingService service = new TightCouplingService();

        int[] data = {11, 12, 13, 15, 23};

        
        service.complexBusinessSort(data);
    }
}