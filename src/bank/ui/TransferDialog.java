package bank.ui;

import bank.entity.Customer;
import bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class TransferDialog extends JDialog {

    public TransferDialog(Frame owner, Customer from) {
        super(owner, "Transfer Money", true);
        setSize(360, 320);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 260));

        JLabel title = new JLabel("Transfer Money");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accLabel = new JLabel("Destination Account ID");
        accLabel.setFont(UITheme.FONT_LABEL);
        accLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField accField = UITheme.textField();
        accField.setAlignmentX(Component.LEFT_ALIGNMENT);
        accField.setMaximumSize(new Dimension(260, 36));

        JLabel amtLabel = new JLabel("Amount");
        amtLabel.setFont(UITheme.FONT_LABEL);
        amtLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField amtField = UITheme.textField();
        amtField.setAlignmentX(Component.LEFT_ALIGNMENT);
        amtField.setMaximumSize(new Dimension(260, 36));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton sendBtn = UITheme.primaryButton("Send Transfer");
        sendBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendBtn.setMaximumSize(new Dimension(260, 42));

        sendBtn.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(amtField.getText().trim());
                BankService.transfer(from, accField.getText().trim(), amt);
                JOptionPane.showMessageDialog(this, "Transfer successful");
                dispose();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Enter a valid amount");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(accLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(accField);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(amtLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(amtField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(sendBtn);

        add(card);
    }
}