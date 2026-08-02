package bank.ui;

import bank.entity.Customer;
import bank.entity.Transaction;
import bank.service.BankService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private final Customer customer;
    private JLabel balanceLabel;
    private DefaultTableModel tableModel;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;

        setTitle("SecureBank - Dashboard");
        setSize(760, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel welcome = new JLabel("Hi, " + customer.getName());
        welcome.setFont(UITheme.FONT_TITLE);
        welcome.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(UITheme.PRIMARY_DARK);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        header.add(welcome, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(20, 20));
        body.setBackground(UITheme.BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        body.add(buildAccountCard(), BorderLayout.NORTH);
        body.add(buildHistoryCard(), BorderLayout.CENTER);
        return body;
    }

    private JPanel buildAccountCard() {
        JPanel card = UITheme.card();
        card.setLayout(new BorderLayout(20, 0));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UITheme.CARD_BG);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel accLabel = new JLabel("Account: " + customer.getAccountId());
        accLabel.setFont(UITheme.FONT_SUBTITLE);
        accLabel.setForeground(UITheme.MUTED);

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        balanceLabel.setForeground(UITheme.TEXT);

        infoPanel.add(accLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        infoPanel.add(balanceLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UITheme.CARD_BG);

        JButton depositBtn = UITheme.primaryButton("Deposit");
        JButton withdrawBtn = UITheme.primaryButton("Withdraw");
        JButton transferBtn = UITheme.primaryButton("Transfer");
        JButton profileBtn = UITheme.flatButton("Edit Profile");

        depositBtn.addActionListener(e -> promptAmount("Deposit", amt -> {
            BankService.deposit(customer, amt);
            refreshData();
        }));

        withdrawBtn.addActionListener(e -> promptAmount("Withdraw", amt -> {
            BankService.withdraw(customer, amt);
            refreshData();
        }));

        transferBtn.addActionListener(e -> {
            new TransferDialog(this, customer).setVisible(true);
            refreshData();
        });

        profileBtn.addActionListener(e -> new EditProfileDialog(this, customer).setVisible(true));

        buttonPanel.add(profileBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transferBtn);

        card.add(infoPanel, BorderLayout.WEST);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    private JPanel buildHistoryCard() {
        JPanel card = UITheme.card();
        card.setLayout(new BorderLayout(0, 10));

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tableModel = new DefaultTableModel(new Object[]{"Date", "Type", "Amount", "Balance After"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(UITheme.FONT_FIELD);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        JScrollPane scrollPane = new JScrollPane(table);

        card.add(title, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private void refreshData() {
        balanceLabel.setText(String.format("Tk %,.2f", customer.getBalance()));
        tableModel.setRowCount(0);
        List<Transaction> txns = BankService.customerTransactions(customer.getCustomerId());
        for (Transaction t : txns) {
            tableModel.addRow(new Object[]{
                    t.getDateTime().toString().replace("T", " "),
                    t.getType(),
                    String.format("%,.2f", t.getAmount()),
                    String.format("%,.2f", t.getBalanceAfter())
            });
        }
    }

    private void promptAmount(String action, java.util.function.DoubleConsumer onConfirm) {
        String input = JOptionPane.showInputDialog(this, action + " amount:");
        if (input == null || input.isBlank()) return;
        try {
            double amt = Double.parseDouble(input.trim());
            onConfirm.accept(amt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}