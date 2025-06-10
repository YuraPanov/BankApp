package com.example.bankapp.model;

import java.sql.Date;

public class Loan {
    private int loanId;
    private int clientId;
    private String currencyCode;
    private double principalAmount;
    private Date loanDate;
    private Date dueDate;
    private double interestRate;
    private double amountDue;
    private Date actualReturnDate;
    private double monthlyPayment;
    private double penaltyAmount;
    private String loanStatus;

    // getters and setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public double getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(double principalAmount) { this.principalAmount = principalAmount; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getLoanDate() { return loanDate; }
    public void setLoanDate(Date loanDate) { this.loanDate = loanDate; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public Double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public Date getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(Date actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(double penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public Double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public String getLoanStatus() { return loanStatus; }
    public void setLoanStatus(String loanStatus) { this.loanStatus = loanStatus; }


    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", clientId=" + clientId +
                ", currencyCode='" + currencyCode + '\'' +
                ", principalAmount=" + principalAmount +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", interestRate=" + interestRate +
                ", amountDue=" + amountDue +
                ", penaltyAmount=" + penaltyAmount +
                ", actualReturnDate=" + actualReturnDate +
                ", loanStatus='" + loanStatus + '\'' +
                ", monthlyPayment=" + monthlyPayment +
                '}';
    }
}
