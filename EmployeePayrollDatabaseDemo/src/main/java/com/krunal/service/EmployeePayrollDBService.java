/**************************************************************************************
 *
 * Purpose : To perform all JDBC Operations and connect database with java program
 *
 * @author Krunal Lad
 * @Since 19-07-2021
 *
 **************************************************************************************/

package com.krunal.service;

import com.krunal.exception.EmployeePayrollException;
import com.krunal.model.EmployeePayrollData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    private EmployeePayrollDBService() {
    }

    /**
     * Purpose : For creating a singleton object
     *
     * @return
     */

    public static EmployeePayrollDBService getInstance() {
        if ( employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    /**
     * Purpose : Read the employee payroll data from the database
     *
     * @return : employeePayrollList
     */

    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee_payroll";
        return getEmployeePayrollDataUsingDB(sql);
    }

    /**
     * Purpose : Update the salary in the DB using Statement Interface
     *
     * @param name
     * @param salary
     * @return
     */

    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    /**
     * Purpose : Update the salary in the DB using PreparedStatement Interface
     *
     * @param name
     * @param salary
     * @return
     */

    public int updateEmployeeDataPreparedStatement(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name,salary);
    }

    /**
     * Purpose : Create connection with the database
     *
     * @return
     * @throws SQLException
     */

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_services";
        String userName = "root";
        String password = "Kklad@2506";
        Connection connection;
        System.out.println("Connecting to database: "+jdbcURL);
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful! " +connection);
        return connection;
    }

    /**
     * Purpose : Update the salary in the DB using Statement Interface
     *
     * @param name
     * @param salary
     * @throws EmployeePayrollException
     * @return
     */

    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String sql = String.format("UPDATE employee_payroll SET salary = %.2f WHERE name = '%s';", salary, name);
        try (Connection connection = this.getConnection()){
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingStatement() for detailed information!");
        }
    }

    /**
     * Purpose : Update the salary in the DB using PreparedStatement Interface
     *
     * @param name
     * @param salary
     * @throws EmployeePayrollException
     * @return
     */

    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws EmployeePayrollException {
        String sql = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
        try (Connection connection = this.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, salary);
            statement.setString(2, name);

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the updateEmployeeDataUsingPreparedStatement() for detailed information!");
        }
    }

    /**
     * Purpose : Get the list of EmployeePayrollData using the assigned name
     *           setString() is used to set the assigned name value in the sql query
     *           Return all the attribute values listed for a particular name
     *
     * @param name
     * @throws EmployeePayrollException
     * @return
     */

    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null)
            this.preparedStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(name) for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * Purpose : Assign the value of the attributes in a list and return it
     *
     * @param resultSet
     * @throws EmployeePayrollException
     * @return
     */

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollData(resultSet) for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * Purpose : To get the details of a particular employee from the DB using PreparedStatement Interface
     *
     * @throws EmployeePayrollException
     */

    private void preparedStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll WHERE name = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the preparedStatementForEmployeeData() for detailed information!");
        }
    }

    /**
     * Purpose : Create connection to execute query and read the value from the database
     *           Assign the value in a list variable
     *
     * @param sql
     * @throws EmployeePayrollException
     * @return
     */

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Please check the getEmployeePayrollDataUsingDB() for detailed information!");
        }
        return employeePayrollList;
    }

    /**
     * Purpose : Read the data for a certain date range from the database
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws EmployeePayrollException
     */

    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return getEmployeePayrollDataUsingDB(sql);
    }
}
