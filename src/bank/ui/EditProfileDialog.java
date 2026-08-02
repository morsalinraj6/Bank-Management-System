package bank.ui;

import bank.entity.Customer;
import bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class EditProfileDialog extends JDialog {

    public EditProfileDialog(Frame owner, Customer customer) {
        super(owner, "Edit Profile", true);
        setSize(360, 400);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 340));

        JTextField nameField = UITheme.textField();
        nameField.setText(customer.getName());
        JTextField emailField = UITheme.textField();
        emailField.setText(customer.getEmail());
        JPasswordField passField = UITheme.passwordField();

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);

        JButton saveBtn = UITheme.primaryButton("Save Changes");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.setMaximumSize(new Dimension(260, 40));

        saveBtn.addActionListener(e -> {
            try {
                String newPass = new String(passField.getPassword());
                BankService.updateCustomerProfile(customer, nameField.getText().trim(),
                        emailField.getText().trim(), newPass.isBlank() ? null : newPass);
                JOptionPane.showMessageDialog(this, "Profile updated");
                dispose();
            } catch (IllegalArgumentException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        addField(card, "Full Name", nameField);
        addField(card, "Email", emailField);
        addField(card, "New Password (blank = keep current)", passField);
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(saveBtn);

        add(card);
    }

    private void addField(JPanel card, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UITheme.FONT_LABEL);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(260, 36));
        card.add(label);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(field);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
    }
}