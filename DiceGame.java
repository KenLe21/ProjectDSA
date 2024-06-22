import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Random;

public class DiceGame {
    private final JFrame frame;
    private final JLabel moneyLabel;
    private final JLabel diceLabel;
    private final JLabel numberLabel;
    private final JLabel resultLabel;
    private final JTextField moneyInputField;
    private final JRadioButton taiRadio;
    private final JRadioButton xiuRadio;
    private final JButton playButton;
    private final JButton napButton;
    private final JButton allInButton;
    private final JButton[] betButtons;
    private final JTable historyTable;
    private JLabel totalMoneyDisplay;
    private JLabel averageMoneyDisplay;
    private int prices = 0;
    private int money = 0;
    private final int[] diceValues = new int[3];
    private final TransactionHistory transactionHistory;
    private static PlayerRanking playerRanking;
    private static User user;
    private static final String RANKING_FILE = "player_ranking.csv";

    public DiceGame(User loggedInUser) {
        user = loggedInUser;
        frame = new JFrame("Dice Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Load background image
        JLabel backgroundLabel = new JLabel(new ImageIcon("E:\\lab scale\\ProjectDSA\\src\\img-1.jpg"));
        frame.setContentPane(backgroundLabel);
        frame.setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        moneyLabel = new JLabel("Money: 0");
        moneyLabel.setForeground(Color.WHITE);
        moneyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(moneyLabel);

        JButton rankingButton = new JButton("Show Ranking");
        styleButton(rankingButton, new Color(255, 193, 7), Color.WHITE);
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRanking();
            }
        });
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(rankingButton);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        moneyInputField = new JTextField(10);
        moneyInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, moneyInputField.getPreferredSize().height));
        napButton = new JButton("Add Money");
        styleButton(napButton, new Color(0, 123, 255), Color.WHITE);

        controlPanel.add(moneyInputField);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(napButton);
        controlPanel.add(Box.createVerticalGlue());

        frame.add(controlPanel, BorderLayout.WEST);

        // Game Panel
        JPanel gamePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        gamePanel.setOpaque(false);
        gamePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Bet Panel
        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        betPanel.setOpaque(false);
        taiRadio = new JRadioButton("Big");
        taiRadio.setForeground(Color.BLACK);
        xiuRadio = new JRadioButton("Small");
        xiuRadio.setForeground(Color.BLACK);
        ButtonGroup group = new ButtonGroup();
        group.add(taiRadio);
        group.add(xiuRadio);
        betPanel.add(taiRadio);
        betPanel.add(xiuRadio);

        allInButton = new JButton("All In");
        styleButton(allInButton, new Color(220, 53, 69), Color.WHITE);
        betPanel.add(allInButton);

        betButtons = new JButton[5];
        for (int i = 0; i < betButtons.length; i++) {
            betButtons[i] = new JButton("" + (i + 1) * 1000000);
            styleButton(betButtons[i], new Color(40, 167, 69), Color.WHITE);
            betPanel.add(betButtons[i]);
        }

        gamePanel.add(betPanel);

        // Play Panel
        JPanel playPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        playPanel.setOpaque(false);
        playButton = new JButton("Play");
        styleButton(playButton, new Color(23, 162, 184), Color.WHITE);
        diceLabel = new JLabel("Dice: ");
        diceLabel.setForeground(Color.WHITE);
        numberLabel = new JLabel("Numbers: ");
        numberLabel.setForeground(Color.WHITE);
        resultLabel = new JLabel("Result: ");
        resultLabel.setForeground(Color.WHITE);
        playPanel.add(playButton);
        playPanel.add(diceLabel);
        playPanel.add(numberLabel);
        playPanel.add(resultLabel);

        gamePanel.add(playPanel);

        // History Panel
        JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.setOpaque(false);
        historyPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        historyTable = new JTable();
        historyTable.setFillsViewportHeight(false);
        totalMoneyDisplay = new JLabel("Total Money: 0");
        totalMoneyDisplay.setForeground(Color.WHITE);
        totalMoneyDisplay.setFont(new Font("Arial", Font.BOLD, 16));
        averageMoneyDisplay = new JLabel("Average Money: 0.0");
        averageMoneyDisplay.setForeground(Color.WHITE);
        averageMoneyDisplay.setFont(new Font("Arial", Font.BOLD, 16));
        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        historyPanel.add(totalMoneyDisplay, BorderLayout.NORTH);
        historyPanel.add(averageMoneyDisplay, BorderLayout.SOUTH);

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(historyPanel, BorderLayout.EAST);

        transactionHistory = new TransactionHistory(historyTable, totalMoneyDisplay, averageMoneyDisplay);
        playerRanking = new PlayerRanking();

        // Load player rankings
        playerRanking.loadRankingFromFile(RANKING_FILE);
        addListeners();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                playerRanking.saveRankingToFile(RANKING_FILE);
            }
        });

        frame.setVisible(true);
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private void addListeners() {
        playButton.setVisible(false);
        for (JButton betButton : betButtons) {
            betButton.setVisible(false);
        }
        allInButton.setVisible(false);
        taiRadio.setVisible(false);
        xiuRadio.setVisible(false);
        resultLabel.setVisible(false);
        diceLabel.setVisible(false);
        numberLabel.setVisible(false);

        napButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int addedMoney = Integer.parseInt(moneyInputField.getText());
                    if (addedMoney < 20000) {
                        JOptionPane.showMessageDialog(frame, "The cost must be more than or equal to 20000.");
                    } else {
                        money += addedMoney;
                        moneyLabel.setText("Money: " + money);
                        playButton.setVisible(true);
                        for (JButton betButton : betButtons) {
                            betButton.setVisible(true);
                        }
                        allInButton.setVisible(true);
                        taiRadio.setVisible(true);
                        xiuRadio.setVisible(true);
                        resultLabel.setVisible(true);
                        diceLabel.setVisible(true);
                        numberLabel.setVisible(true);
                        historyTable.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid amount of money.");
                }
            }
        });

        for (JButton betButton : betButtons) {
            betButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        prices = Integer.parseInt(betButton.getText().replace(",", ""));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid bet amount.");
                    }
                }
            });
        }

        allInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prices = money;
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });
    }

    private void playGame() {

        if (money < prices) {
            JOptionPane.showMessageDialog(frame, "You don't have enough money.");
            return;
        }

        diceLabel.setText("Dice: Rolling...");
        numberLabel.setText("Numbers: ");
        resultLabel.setText("Result: ");

        new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollDice();
                int total = calculateTotal();
                displayDiceValues();

                String result;
                if (taiRadio.isSelected()) {
                    if (total >= 11) {
                        result = "Tai win";
                        money += prices;
                        transactionHistory.addTransaction(prices);
                        playerRanking.addPlayer(user.getUsername(), money);
                    } else {
                        result = "Tai lose";
                        money -= prices;
                        transactionHistory.addTransaction(-prices);
                        playerRanking.addPlayer(user.getUsername(), money);
                    }
                } else if (xiuRadio.isSelected()) {
                    if (total < 11) {
                        result = "Xiu win";
                        money += prices;
                        transactionHistory.addTransaction(prices);
                        playerRanking.addPlayer(user.getUsername(), money);
                    } else {
                        result = "Xiu lose";
                        money -= prices;
                        transactionHistory.addTransaction(-prices);
                        playerRanking.addPlayer(user.getUsername(), money);
                    }
                } else {
                    result = "No bet selected";
                }

                resultLabel.setText("Result: " + result);
                moneyLabel.setText("Money: " + money);
                diceLabel.setText("Dice: ");

                ((Timer) e.getSource()).stop();
            }
        }).start();
    }

    private void rollDice() {
        Random rand = new Random();
        for (int i = 0; i < diceValues.length; i++) {
            diceValues[i] = rand.nextInt(6) + 1;
        }
    }

    private int calculateTotal() {
        int total = 0;
        for (int value : diceValues) {
            total += value;
        }
        return total;
    }

    private void displayDiceValues() {
        StringBuilder numbers = new StringBuilder();
        for (int value : diceValues) {
            numbers.append(value).append(" ");
        }
        numberLabel.setText("Numbers: " + numbers);
    }

    private void showRanking() {
        JFrame rankingFrame = new JFrame("Player Ranking");
        rankingFrame.setSize(300, 400);
        rankingFrame.setLayout(new BorderLayout());

        JTextArea rankingTextArea = new JTextArea();
        rankingTextArea.setEditable(false);

        StringBuilder rankingData = new StringBuilder();
        int rank = 1;

        for (Map.Entry<String, Integer> entry : playerRanking.getRanking().entrySet()) {
            rankingData.append(rank).append(". ").append(entry.getKey())
                    .append(" - Score: ").append(entry.getValue()).append("\n");
            rank++;
        }

        rankingTextArea.setText(rankingData.toString());
        rankingFrame.add(new JScrollPane(rankingTextArea), BorderLayout.CENTER);

        rankingFrame.setLocationRelativeTo(null);
        rankingFrame.setVisible(true);
    }

    public static void main(String[] args) {
        playerRanking = new PlayerRanking();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Option();
            }
        });
    }
}



