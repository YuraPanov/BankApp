package com.example.bankapp.model;

import java.sql.Date;

public class Loan {
    private int loanId;
    private int clientId;
    private String currencyCode;
    private double principalAmount;
    private Date dueDate;
    private Date loanDate;
    private float interestRate;
    private double amountDue;
    private int penaltyAmount;
    private Date actualReturnDate;
    private double monthlyPayment;
//    private int loanStatus;

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

    public float getInterestRate() { return interestRate; }
    public void setInterestRate(float interestRate) { this.interestRate = interestRate; }

    public Double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public Date getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(Date actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public int getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(int penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public Double getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

//    public int getLoanStatus() { return loanStatus; }
//    public void setLoanStatus(int loanStatus) { this.loanStatus = loanStatus; }

}