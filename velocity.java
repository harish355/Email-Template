import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class HtmlTemplateProcessor {
    public static void main(String[] args) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        // Prepare data for the table
        List<List<String>> tableData = new ArrayList<>();
        List<String> row1 = List.of("1", "John Doe", "30");
        List<String> row2 = List.of("2", "Jane Smith", "25");
        tableData.add(row1);
        tableData.add(row2);

        // Populate the Velocity context with data
        VelocityContext context = new VelocityContext();
        context.put("data", tableData);

        // Merge data with the HTML template
        Template template = velocityEngine.getTemplate("template.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        // Write the merged content to a new HTML file
        try (FileWriter fileWriter = new FileWriter("output.html")) {
            fileWriter.write(writer.toString());
            System.out.println("HTML file generated successfully.");
        } catch (Exception e) {
            System.err.println("Error writing HTML file: " + e.getMessage());
        }
    }
}
