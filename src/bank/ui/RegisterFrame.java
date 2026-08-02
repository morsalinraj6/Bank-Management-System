package bank.ui;

import bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("SecureBank - Register");
        setSize(460, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(380, 620));

        JLabel title = new JLabel("Create your account");
        title.setFont(UITheme.FONT_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField nameField = labeledField(card, "Full Name");
        JTextField emailField = labeledField(card, "Email");
        JTextField userField = labeledField(card, "Username");
        JPasswordField passField = labeledPasswordField(card, "Password");

        card.add(sectionLabel("Account Type"));
        JComboBox<String> accountType = new JComboBox<>(new String[]{"Savings", "Current"});
        accountType.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountType.setMaximumSize(new Dimension(400, 36));
        card.add(accountType);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        card.add(sectionLabel("Security Question (password recovery)"));
        JComboBox<String> question = new JComboBox<>(new String[]{
                "What was your childhood nickname?",
                "What is your favorite teacher's name?",
                "What city were you born in?",
                "What was the name of your first school?"
        });
        question.setAlignmentX(Component.LEFT_ALIGNMENT);
        question.setMaximumSize(new Dimension(400, 36));
        card.add(question);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        JTextField answerField = labeledField(card, "Your Answer");

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setFont(UITheme.FONT_LABEL);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton registerBtn = UITheme.primaryButton("Create Account");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(400, 45));
        card.add(registerBtn);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton backBtn = UITheme.flatButton("Already have an account? Sign in");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(backBtn);

        registerBtn.addActionListener(e -> {
            try {
                BankService.register(
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        userField.getText().trim(),
                        new String(passField.getPassword()),
                        (String) accountType.getSelectedItem(),
                        (String) question.getSelectedItem(),
                        answerField.getText().trim()
                );
                JOptionPane.showMessageDialog(this, "Account created! Please sign in.");
                new LoginFrame().setVisible(true);
                dispose();
            } catch (IllegalArgumentException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_LABEL);
        l.setForeground(UITheme.TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField labeledField(JPanel card, String labelText) {
        card.add(sectionLabel(labelText));
        JTextField field = UITheme.textField();
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(400, 36));
        card.add(field);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        return field;
    }

    private JPasswordField labeledPasswordField(JPanel card, String labelText) {
        card.add(sectionLabel(labelText));
        JPasswordField field = UITheme.passwordField();
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(400, 36));
        card.add(field);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        return field;
    }
}