import java.util.InputMismatchException;
import java.util.Scanner;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
// CEO username: CodeNinja password: Hack3r
// employee username: WimpyKid09 password: room
public class EmployeeData {
    // instance variables
    Connection connection; 
    Statement statement;
    ResultSet resultSet;
    String username;
    String password;
    Scanner userInput = new Scanner(System.in);

    // constructor
    public EmployeeData(Connection connection, Statement statement, ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
        this.username = "";
        this.password = "";
    }

    // constructor
    public EmployeeData() {
        this.connection = null;
        this.statement = null;
        this.resultSet = null;
        this.username = "";
        this.password = "";
    }
    public static void main(String[] args) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/employees";
        String dUsername = "noelberaki";
        String dPassword = "Suns";

        EmployeeData companyX = new EmployeeData();
        companyX.establishConnection(jdbcURL, dUsername, dPassword);
        companyX.intro();
        companyX.employeeCheck();
        companyX.menu();
    
        System.out.println("\n\n");
    }

    // Establish connection to database
    public void establishConnection(String jdbcURL, String databaseUsername, String databasePassword) {
        try {
            this.connection = DriverManager.getConnection(jdbcURL, databaseUsername, databasePassword);
            this.statement = connection.createStatement();
        }
        catch (SQLException e) {
            System.out.println("Error: Couldn't establish connection to database\n\n");
        }
    }

    // Introduction
    public void intro() {
        System.out.println("\n\n                    CompanyX                      ");
        System.out.println("------------------------------------------------------");
        System.out.println("Sign in to see employee data!");
    }

    // Menu 
    public void menu() {
        System.out.println("\n\n                    Menu                 ");
        System.out.println("------------------------------------------------------");
        System.out.println("Press 1 to view entire list of employees");
        System.out.println("Press 2 to view a specific employee");
        System.out.println("Press 3 to view employees in a specific profession");
        System.out.println("Press 4 to update employee list (CEO + CTO only)");
        System.out.println("Press 0 to exit");

        int choice = 0;
        int tries = 0;
        boolean attempts = true;
        try {
            do {
                System.out.print("Choice: ");
                choice = this.userInput.nextInt();

                tries++;
                if (tries >= 3) {
                    System.out.println("Too many failed attempts. Program will exit now.");
                    System.out.println("Goodbye\n\n");
                    System.exit(0);
                }
                else if (choice < 0 || choice > 4) {
                    System.out.println("Invalid selection");
                    System.out.println("Please try again");
                }
                else {
                    attempts = false;
                    break;
                }
            } while (attempts);
        }
        catch (InputMismatchException e) {
            System.out.println("Error: Not an option");
            System.out.println("System exit now");
            System.exit(0);
        }       

        switch (choice) {
            case 1: viewEntireList(); break;
            case 2: viewEmployee(); break;
            case 3: profession(); break;
            case 4: updateList(); break;
            case 0: System.out.println("Goodbye\n\n"); System.exit(0); break;
            default: System.out.println("Goodbye\n\n"); System.exit(0);
        }
    }

    // valied employee check
    public void employeeCheck() {
        System.out.print("Username: ");
        String username = this.userInput.next();
        System.out.print("Password: ");
        String password = this.userInput.next();

        try {
            String query = String.format("SELECT * FROM employees WHERE username='%s' AND password='%s';", username, password);
            this.resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                System.out.println("\nSuccesful Login");
                this.username = username;
                this.password = password;
            }
            else {
                System.out.println("\nError: Username or Password isn't valid.");
                System.out.println("Username entered: " + this.username);
                System.out.println("Password entered: " + this.password);
                System.out.println("Goodbye\n\n");
                System.exit(0);
            }
        }
        catch (SQLException e) {
            System.out.println("Error: Failure to login");
            System.out.println("Goodbye\n\n");
            System.exit(0);
        }
    }

    // view entire table of employees
    public void viewEntireList() {
        try {
            String query = "SELECT * FROM employees;";
            this.resultSet = statement.executeQuery(query);

            System.out.println("\n\n                  Employees at CompanyX                                   ");
            System.out.println("-------------------------------------------------------------------------------|");

            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                String jobTitle = resultSet.getString("job_title");
                String name = resultSet.getString("fName") + " " + resultSet.getString("lName");
                String username = resultSet.getString("username");

                System.out.printf("ID: %s, Job Title: %s, Name: %s, Username: %s\n", ID, jobTitle, name, username);
                System.out.println("-------------------------------------------------------------------------------|");
            }
        }
        catch (SQLException e) {
            System.out.println("Error: Can't view list");
        }
        finally {
            menuOrExit();
        }
    }

    // view specific employee
    public void viewEmployee() {
        System.out.println("\n\nHow do you want to find this employee?");
        System.out.println("Enter 1 find employee by ID");
        System.out.println("Enter 2 to find employee by username");
        System.out.print("\nOption: ");
        int option = this.userInput.nextInt();

        String mode;

        switch(option) {
            case 1: mode = "id"; break;
            case 2: mode = "username"; break;
            default: 
                mode = "Invalid option. System exitting now";
                System.out.println(mode);
                System.exit(0);
            break;
        }

        System.out.println("What employee do you want to look at?");
        String employee = this.userInput.next();

        try {
            String query = "SELECT * FROM employees WHERE ";
            if (mode.equals("id")) {
                query += "id='" + Integer.parseInt(employee) + "';";
            }
            else {
                query += "username='" + employee + "';";
            }

            this.resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("fName") + " " + resultSet.getString("lName");
                String jobTitle = resultSet.getString("job_title");
                String username = resultSet.getString("username");
                System.out.println("-------------------------------------------------------------------------------|");
                System.out.printf("ID: %s, Job Title: %s, Name: %s, Username: %s|\n", id, jobTitle, name, username);
                System.out.println("-------------------------------------------------------------------------------|");
            }
            else {
                System.out.println("\nCouldn't fetch employee. Employee may not be logged");
            }
        }
        catch (SQLException e) {
            System.out.println("\nError: Couldn't fetch employee");
        }
        finally {
            menuOrExit();
        }
    }

    // Specific profession in company
    public void profession() {
        System.out.println("\n\nWhat profession would you like to look at");
        System.out.println("Press 1 for CEO");
        System.out.println("Press 2 for CTO");
        System.out.println("Press 3 for Product Manager");
        System.out.println("Press 4 for Software Engineer");

        System.out.println("\nChoice: ");
        try {
            int choice = this.userInput.nextInt();
            String query = "SELECT * FROM employees WHERE job_title='";

            if (choice == 1) {
                query += "CEO';";
            }
            else if (choice == 2) {
                query += "CTO';";
            }
            else if (choice == 3) {
                query += "Product Manager';";
            }
            else if (choice == 4) {
                query += "Software Engineer';";
            }
            else {
                System.out.println("Not a valid choice.");
                System.out.println("System exiting now. Goodbye");
                System.exit(0);
            }

            resultSet = statement.executeQuery(query);
            System.out.println("-------------------------------------------------------------------------------|");
            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                String jobTitle = resultSet.getString("job_title");
                String name = resultSet.getString("fName") + " " + resultSet.getString("lName");
                String username = resultSet.getString("username");

                System.out.printf("ID: %s, Job Title: %s, Name: %s, Username: %s\n", ID, jobTitle, name, username);
                System.out.println("-------------------------------------------------------------------------------|");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Not a valid choice.");
            System.out.println("System exiting now. Goodbye\n\n");
            System.exit(0);
        }
        catch (SQLException e) {
            System.out.println("Error: Couldn't fufill request");
            System.exit(0);
        }
        finally {
            menuOrExit();
        }
    }

    // update list
    public void updateList() {
        try {
            String query = "SELECT * FROM employees WHERE username='" + this.username + "';";
            this.resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String job = resultSet.getString("job_title");
                if (!job.equals("CEO") && !job.equals("CTO")) {
                    System.out.println("\nSorry, but you are not authorized to make updates to the employee database.");
                    System.out.println("Only the CEO and CTO of CompanyX have access to make such request");
                }
                else {
                    String name = resultSet.getString("fName") + " " + resultSet.getString("lName");
                    System.out.println("\nWelcome " + name);
                    System.out.println("Would you like to hire an employee or fire one?");
                    System.out.println("Enter 1 to hire or 2 to fire");

                    try {
                        int choice = userInput.nextInt();
                        hireOrFire(choice);
                    }
                    catch(InputMismatchException e) {
                        System.out.println("Invalid option. Goodbye");
                        System.exit(0);
                    }
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error: Sorry, but request isn't able to be fufilled");
            System.exit(0);
        }
        finally {
            menuOrExit();
        }
    }

    // hire or fire employee. Inserts into database or drops a row from the database
    private void hireOrFire(int choice) {
        try {
            String query = "";
            if (choice == 1) {
                System.out.println("Who would you like to hire?");
            }
            else if (choice == 2) {
                System.out.println("Who would you like to fire?");
            }

            System.out.print("ID: ");
            int id = userInput.nextInt();

            if (choice == 2) {
                query = "DELETE FROM employees WHERE id='" + id + "';";
                statement.executeUpdate(query);
                System.out.println("Hard to see a valuable assest go, but it is what it is");
                return;
            }
            userInput.nextLine();

            System.out.print("Job Title: ");
            String jobTitle = userInput.nextLine();

            System.out.print("First Name: ");
            String fName = userInput.nextLine();

            System.out.print("Last Name: ");
            String lName = userInput.nextLine();

            System.out.print("Username: ");
            String username = userInput.next();

            System.out.print("Password: ");
            String password = userInput.next();

            if (choice == 1) {
                query = String.format("INSERT INTO employees VALUES(%d, '%s', '%s', '%s', '%s', '%s');", id, jobTitle, fName, lName, username, password);
                System.out.println("Congrats on the new addition to the company");
            }
            else {
                System.out.println("Not a valid choice.");
                System.out.println("System exiting now. Goodbye");
                System.exit(0);
            }
            statement.executeUpdate(query);
        }
        catch (InputMismatchException e) {
            System.out.println("Error while filling out information");
            System.out.println("System exiting");
            System.exit(0);
        }
        catch (SQLException e) {
            System.out.println("Error: Couldn't log new hire");
            System.out.println("If hiring, be aware of a duplicate ID or username. No employee can have the same ID or username");        
            System.exit(0);
        }
    }

    // Back to menu or exit
    public void menuOrExit() {
        System.out.println("\nPress 1 to go back to the menu or 0 to exit program");
        System.out.print("Choice: ");
        int choice = userInput.nextInt();
        if (choice == 1) { menu(); }
        else {System.out.println("Goodbye\n\n"); System.exit(0);}
    }
}