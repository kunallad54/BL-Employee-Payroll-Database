package com.krunal.model;

import java.time.LocalDate;

public class EmployeePayrollData {
    public int employee_id;
    public String name;
    public double net_pay;
    public LocalDate start;

    public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
        this.employee_id = id;
        this.name = name;
        this.net_pay = salary;
        this.start = start;
    }
}
