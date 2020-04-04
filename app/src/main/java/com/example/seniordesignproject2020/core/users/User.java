package com.example.seniordesignproject2020.core.users;

import com.example.seniordesignproject2020.core.scan_types.ScanType;

public abstract class User {

    private long id;

    private String firstName;
    private String lastName;
    private String eMail;
    private ScanType[] scanTypes;
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public ScanType[] getScanTypes() {
        return scanTypes;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setScanTypes(ScanType[] scanTypes) {
        this.scanTypes = scanTypes;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
