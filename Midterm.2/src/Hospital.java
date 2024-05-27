import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private List<Patient> patients;

    public Hospital() {
        patients = new ArrayList<>();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(String patientId) {
        patients.removeIf(patient -> patient.getId().equals(patientId));
    }

    public void saveState() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("state.csv"))) {
            for (Patient patient : patients) {
                writer.write(patient.toString());
                writer.newLine();
            }
            System.out.println("State saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void restoreState() {
        try (BufferedReader reader = new BufferedReader(new FileReader("state.csv"))) {
            String line;
            patients.clear();
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading line: " + line);
                String[] data = line.split(",");
                if (data.length == 3) {
                    try {
                        String id = data[0];
                        String name = data[1];
                        int age = Integer.parseInt(data[2]);
                        patients.add(new Patient(name, age, id));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line (invalid age): " + line);
                    }
                } else {
                    System.err.println("Skipping malformed line (incorrect format): " + line);
                }
            }
            System.out.println("State restored successfully.");
        } catch (FileNotFoundException e) {
            System.err.println("state.csv not found. No state to restore.");
        } catch (IOException e) {
            System.err.println("Error restoring state: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<Patient> getPatients() {
        return patients;
    }

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Hospital hospital = new Hospital();

        // Clear any existing patients before restoring (just for safety)
        hospital.getPatients().clear();

        // Restore the state from the file
        hospital.restoreState();

        // Display the restored state
        System.out.println("Restored patients:");
        for (Patient patient : hospital.getPatients()) {
            System.out.println(patient.getName() + " - " + patient.getAge() + " - " + patient.getId());
        }
    }}


