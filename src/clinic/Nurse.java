package clinic;

public class Nurse extends StaffMember {
    private String shift;
    private boolean certified;

    public Nurse(String name, int salary, String hireDate, String shift, boolean certified) {
        super(name, "nurse", salary, hireDate);
        this.shift = shift;
        this.certified = certified;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Shift: " + shift);
        System.out.println("Certified: " + (certified ? "Yes" : "No"));
    }

    public void assistDoctor() {
        System.out.println(name + " is assisting the doctor.");
    }
}