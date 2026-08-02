package bank.ui;

import bank.entity.Customer;
import bank.entity.Transaction;
import bank.service.BankService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private DefaultTableModel customerModel;
    private DefaultTableModel txnModel;
    private JLabel statsLabel;

    public AdminDashboard() {
        setTitle("SecureBank - Admin Panel");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_LABEL);
        tabs.addTab("Customers", buildCustomerTab());
        tabs.addTab("All Transactions", buildTransactionTab());
        add(tabs, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Admin Panel");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(Color.WHITE);

        statsLabel = new JLabel();
        statsLabel.setFont(UITheme.FONT_SUBTITLE);
        statsLabel.setForeground(new Color(0xE5E7EB));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(statsLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(UITheme.PRIMARY);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        header.add(left, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel buildCustomerTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JTextField searchField = UITheme.textField();
        searchField.setMaximumSize(new Dimension(300, 36));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(UITheme.BG);
        searchPanel.add(new JLabel("Search by name/username/email: "));
        searchPanel.add(searchField);

        customerModel = new DefaultTableModel(new Object[]{"Customer ID", "Name", "Username", "Email", "Account ID", "Balance"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(customerModel);
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);

        searchField.addCaretListener(e -> filterCustomers(searchField.getText()));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTransactionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        txnModel = new DefaultTableModel(new Object[]{"Txn ID", "Customer ID", "Account ID", "Type", "Amount", "Balance After", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(txnModel);
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void refreshData() {
        statsLabel.setText(String.format("%d customers | Tk %,.2f total balance",
                BankService.customers().size(), BankService.totalBankBalance()));
        filterCustomers("");

        txnModel.setRowCount(0);
        List<Transaction> txns = BankService.allTransactionsSorted();
        for (Transaction t : txns) {
            txnModel.addRow(new Object[]{
                    t.getTxnId(), t.getCustomerId(), t.getAccountId(), t.getType(),
                    String.format("%,.2f", t.getAmount()),
                    String.format("%,.2f", t.getBalanceAfter()),
                    t.getDateTime().toString().replace("T", " ")
            });
        }
    }

    private void filterCustomers(String query) {
        customerModel.setRowCount(0);
        String q = query.trim().toLowerCase();
        for (Customer c : BankService.customers()) {
            boolean match = q.isEmpty()
                    || c.getName().toLowerCase().contains(q)
                    || c.getUsername().toLowerCase().contains(q)
                    || c.getEmail().toLowerCase().contains(q);
            if (match) {
                customerModel.addRow(new Object[]{
                        c.getCustomerId(), c.getName(), c.getUsername(), c.getEmail(),
                        c.getAccountId(), String.format("%,.2f", c.getBalance())
                });
            }
        }
    }
}