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

import java.util.List;

public class EmployeePayrollDatabaseTest {

    EmployeePayrollService employeePayrollService = null;

    @Before
    public void setUp() throws Exception {
        employeePayrollService = new EmployeePayrollService();
    }


    /**
     * Test Case 1 : To check the count in database is matching in java program or not
     */
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * Test Case 2 : To test whether the salary is updated in the database and is synced with the DB
     *                - Read the values from the database
     *                - Update the salary in the database
     *                - Test whether the database is correctly synced or not
     */

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Terissa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa");
        Assert.assertTrue(result);
    }
}
