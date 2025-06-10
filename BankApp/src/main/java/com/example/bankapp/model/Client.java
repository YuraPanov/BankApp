package com.example.bankapp.model;
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String passportSeries;
    private String passportNumber;
    private String passportIssuedDate;
    private String passportIssuedBy;
    private String telephoneNumber;

    // Getters and setters
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPassportSeries() { return passportSeries; }
    public void setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getPassportIssuedDate() { return passportIssuedDate; }
    public void setPassportIssuedDate(String passportIssuedDate) { this.passportIssuedDate = passportIssuedDate; }

    public String getPassportIssuedBy() { return passportIssuedBy; }
    public void setPassportIssuedBy(String passportIssuedBy) { this.passportIssuedBy = passportIssuedBy; }

//    public String getTelephoneNumber() { return telephoneNumber; }
//    public void setTelephoneNumber(String telephoneNumber) { this.telephoneNumber = telephoneNumber; }

    @Override
    public String toString() { return firstName + " " + lastName; }
}