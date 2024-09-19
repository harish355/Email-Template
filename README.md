import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Model classes with @JsonIgnoreProperties to ignore unknown fields
public class JFrogAuditParser {

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AuditReport {
        public String projectName;
        public String version;
        public List<Component> components;

        // Getters and Setters
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Component {
        public String name;
        public String version;
        public List<Vulnerability> vulnerabilities;

        // Getters and Setters
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Vulnerability {
        public String id;
        public String severity;
        public String description;

        // Getters and Setters
    }

    public static void main(String[] args) {
        // File path to the JFrog audit report (JSON)
        String jsonFilePath = "jfrog_audit_report.json";

        // CSV file path to which data will be appended
        String csvFilePath = "audit_report.csv";

        // Create an ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Parse the JSON file into the AuditReport object
            AuditReport report = mapper.readValue(new File(jsonFilePath), AuditReport.class);

            // Write or append parsed data to a CSV file
            appendToCsv(report, csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to append parsed data to a CSV file
    private static void appendToCsv(AuditReport report, String csvFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {

            // Write CSV header if the file is empty or new
            File file = new File(csvFilePath);
            if (file.length() == 0) {
                writer.write("Project Name, Project Version, Component Name, Component Version, Vulnerability ID, Severity, Description");
                writer.newLine();
            }

            // Append data from the parsed report
            for (Component component : report.components) {
                if (component.vulnerabilities != null && !component.vulnerabilities.isEmpty()) {
                    for (Vulnerability vulnerability : component.vulnerabilities) {
                        writer.write(String.format("%s, %s, %s, %s, %s, %s, %s",
                                report.projectName,
                                report.version,
                                component.name,
                                component.version,
                                vulnerability.id,
                                vulnerability.severity,
                                vulnerability.description));
                        writer.newLine();
                    }
                } else {
                    // If no vulnerabilities, still log the component
                    writer.write(String.format("%s, %s, %s, %s, N/A, N/A, N/A",
                            report.projectName,
                            report.version,
                            component.name,
                            component.version));
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
