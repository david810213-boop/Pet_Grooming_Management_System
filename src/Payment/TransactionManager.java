package Payment;

import java.util.ArrayList;
import java.util.List;


public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    // 根據使用者 email 查詢交易紀錄
    public List<Transaction> getTransactionsByUser(String email) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getMemberEmail().equals(email)) {
                result.add(t);
            }
        }
        return result;
    }
    // 列印所有交易紀錄
    public void printAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("目前沒有交易紀錄。");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }

    // 取得所有交易紀錄
    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    // 計算總營收
    public double calculateTotalRevenue() {
        double total = 0;
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
        return total;
    } 

}
