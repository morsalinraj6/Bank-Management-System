package bank.ui;

import bank.service.BankService;

import javax.swing.*;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private String currentUsername;

    public ForgotPasswordFrame() {
        setTitle("SecureBank - Reset Password");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG);
        setLayout(new GridBagLayout());

        container.add(buildStep1(), "step1");
        container.add(new JPanel(), "step2");
        add(container);
    }

    private JPanel buildStep1() {
        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(340, 260));

        JLabel title = new JLabel("Forgot Password");
        title.setFont(UITheme.FONT_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Enter your username to continue");
        sub.setFont(UITheme.FONT_SUBTITLE);
        sub.setForeground(UITheme.MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField userField = UITheme.textField();
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        userField.setMaximumSize(new Dimension(300, 36));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton nextBtn = UITheme.primaryButton("Continue");
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBtn.setMaximumSize(new Dimension(300, 42));

        nextBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String question = BankService.getSecurityQuestion(username);
            if (question == null) {
                errorLabel.setText("No account found with that username");
                return;
            }
            currentUsername = username;
            container.remove(1);
            container.add(buildStep2(question), "step2");
            cardLayout.show(container, "step2");
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(sub);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nextBtn);

        return card;
    }

    private JPanel buildStep2(String question) {
        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(340, 340));

        JLabel title = new JLabel("Answer Security Question");
        title.setFont(UITheme.FONT_TITLE.deriveFont(20f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel qLabel = new JLabel("<html>" + question + "</html>");
        qLabel.setFont(UITheme.FONT_LABEL);
        qLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField answerField = UITheme.textField();
        answerField.setAlignmentX(Component.LEFT_ALIGNMENT);
        answerField.setMaximumSize(new Dimension(300, 36));

        JLabel newPassLabel = new JLabel("New Password");
        newPassLabel.setFont(UITheme.FONT_LABEL);
        newPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField newPassField = UITheme.passwordField();
        newPassField.setAlignmentX(Component.LEFT_ALIGNMENT);
        newPassField.setMaximumSize(new Dimension(300, 36));

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton resetBtn = UITheme.primaryButton("Reset Password");
        resetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetBtn.setMaximumSize(new Dimension(300, 42));

        resetBtn.addActionListener(e -> {
            try {
                boolean ok = BankService.resetPassword(currentUsername, answerField.getText(),
                        new String(newPassField.getPassword()));
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Password reset successfully. Please sign in.");
                    dispose();
                } else {
                    errorLabel.setText("Incorrect answer");
                }
            } catch (IllegalArgumentException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(qLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(answerField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(newPassLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(newPassField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(resetBtn);

        return card;
    }
}