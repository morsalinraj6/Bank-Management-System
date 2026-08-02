package bank.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 2L;

    private String txnId;
    private String customerId;
    private String accountId;
    private String relatedAccountId;
    private LocalDateTime dateTime;
    private String type;
    private double amount;
    private double balanceAfter;

    public Transaction() {}

    public Transaction(String txnId, String customerId, String accountId, String relatedAccountId,
                        LocalDateTime dateTime, String type, double amount, double balanceAfter) {
        this.txnId = txnId;
        this.customerId = customerId;
        this.accountId = accountId;
        this.relatedAccountId = relatedAccountId;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getTxnId() { return txnId; }
    public void setTxnId(String txnId) { this.txnId = txnId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getRelatedAccountId() { return relatedAccountId; }
    public void setRelatedAccountId(String relatedAccountId) { this.relatedAccountId = relatedAccountId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }

    @Override
    public String toString() {
        return txnId + " " + type + " " + amount + " => " + balanceAfter + " @ " + dateTime;
    }
}