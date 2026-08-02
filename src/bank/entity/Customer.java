package bank.entity;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 2L;

    private String customerId;
    private String name;
    private String email;
    private String username;
    private String passwordHash;
    private String passwordSalt;
    private String securityQuestion;
    private String securityAnswerHash;
    private String securityAnswerSalt;
    private String accountId;
    private double balance;

    public Customer() {}

    public Customer(String customerId, String name, String email, String username,
                     String passwordHash, String passwordSalt,
                     String securityQuestion, String securityAnswerHash, String securityAnswerSalt,
                     String accountId, double balance) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.securityQuestion = securityQuestion;
        this.securityAnswerHash = securityAnswerHash;
        this.securityAnswerSalt = securityAnswerSalt;
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswerHash() { return securityAnswerHash; }
    public void setSecurityAnswerHash(String securityAnswerHash) { this.securityAnswerHash = securityAnswerHash; }

    public String getSecurityAnswerSalt() { return securityAnswerSalt; }
    public void setSecurityAnswerSalt(String securityAnswerSalt) { this.securityAnswerSalt = securityAnswerSalt; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return customerId + " - " + name + " (" + username + ")";
    }
}