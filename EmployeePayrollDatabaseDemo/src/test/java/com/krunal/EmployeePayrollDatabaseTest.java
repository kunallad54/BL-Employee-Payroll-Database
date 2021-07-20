/**
 * Purpose : To perform JUnit Testing
 */
package com.krunal;

import com.krunal.exception.EmployeePayrollException;
import com.krunal.model.EmployeePayrollData;
import com.krunal.service.EmployeePayrollService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollDatabaseTest {

    EmployeePayrollService employeePayrollService = null;

    @Before
    public void setUp() throws Exception {
        employeePayrollService = new EmployeePayrollService();
    }

    /**
     * Purpose : To test whether the number of records present in the database matches with the expected value
     *
     * @throws EmployeePayrollException
     */

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * Purpose : To test whether the salary is updated in the database and is synced with the DB
     *           - Read the values from the database
     *           - Update the salary in the database
     *           - Test whether the database is correctly synced or not
     *
     * @throws EmployeePayrollException
     */

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terissa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa");
        Assert.assertTrue(result);
    }

    /**
     * Purpose : To test whether the salary is updated in the database and is synced with the DB using JDBC PreparedStatement
     *           - Read the values from the database
     *           - Update the salary in the database
     *           - Test whether the database is correctly synced or not
     *
     * @throws EmployeePayrollException
     */

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDBUsingPreparedStatement() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalaryUsingPreparedStatement("Terissa", 5000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa");
        Assert.assertTrue(result);
    }

    /**
     * Purpose : To test whether the count of the retrieved data who have joined in a particular data range matches with the expected value
     *
     * @throws EmployeePayrollException
     */

    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchTheEmployeeCount() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(EmployeePayrollService.IOService.DB_IO, startDate, endDate);
        Assert.assertEquals(3, employeePayrollData.size());
    }
}
