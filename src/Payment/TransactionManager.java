package Payment;

import java.util.ArrayList;
import java.util.List;


public class TransactionManager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactionsByUser(String email) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getMemberEmail().equals(email)) {
                result.add(t);
            }
        }
        return result;
    }

    public void printAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("目前沒有交易紀錄。");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }


}
