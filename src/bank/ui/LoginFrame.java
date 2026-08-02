package bank.ui;

import bank.entity.Customer;
import bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("SecureBank - Login");
        setSize(420, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(340, 400));

        JLabel title = new JLabel("Welcome back");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to your SecureBank account");
        subtitle.setFont(UITheme.FONT_SUBTITLE);
        subtitle.setForeground(UITheme.MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = label("Username or Email");
        JTextField userField = UITheme.textField();
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField.setMaximumSize(new Dimension(400, 40));

        JLabel passLabel = label("Password");
        JPasswordField passField = UITheme.passwordField();
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(400, 40));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setFont(UITheme.FONT_LABEL);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn = UITheme.primaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(400, 45));

        JButton forgotBtn = UITheme.flatButton("Forgot password?");
        forgotBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton registerBtn = UITheme.flatButton("Create a new account");
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please fill in all fields");
                return;
            }
            Object result = BankService.login(user, pass);
            if ("ADMIN".equals(result)) {
                new AdminDashboard().setVisible(true);
                dispose();
            } else if (result instanceof Customer) {
                new CustomerDashboard((Customer) result).setVisible(true);
                dispose();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        });

        forgotBtn.addActionListener(e -> new ForgotPasswordFrame().setVisible(true));

        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(userLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(passField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(loginBtn);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(forgotBtn);
        card.add(registerBtn);

        add(card);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.FONT_LABEL);
        l.setForeground(UITheme.TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}