import javax.swing.*;
import java.util.ArrayList;
public class TransactionHistory {
    private ArrayList<Integer> transactions;
    private JTable historyTable;
    private JLabel totalMoneyDisplay;
    private JLabel averageMoneyDisplay;

    public TransactionHistory(JTable historyTable, JLabel totalMoneyDisplay, JLabel averageMoneyDisplay) {
        this.transactions = new ArrayList<>();
        this.historyTable = historyTable;
        this.totalMoneyDisplay = totalMoneyDisplay;
        this.averageMoneyDisplay = averageMoneyDisplay;
    }

    public void addTransaction(int amount) {
        transactions.add(amount);
        updateTransactionHistory();
    }
    private void updateTransactionHistory() {
        String[] columnNames = {"Transaction", "Profit"};
        String[][] data = new String[transactions.size()][2];
        for (int i = 0; i < transactions.size(); i++) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = String.valueOf(transactions.get(i));
        }
        historyTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        int totalMoney = transactions.stream().mapToInt(Integer::intValue).sum();
        double averageMoney = transactions.isEmpty() ? 0 : (double) totalMoney / transactions.size();
        totalMoneyDisplay.setText("Total profit: " + totalMoney);
        averageMoneyDisplay.setText("Average profit: " + String.format("%.1f", averageMoney));
    }
}
