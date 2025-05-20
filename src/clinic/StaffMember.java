package clinic;

public class StaffMember {
    protected String name;
    protected String role;
    protected int salary;
    protected String hireDate;

    public StaffMember(String name, String role, int salary, String hireDate) {
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Salary: " + salary);
        System.out.println("Hired: " + hireDate);
    }
}