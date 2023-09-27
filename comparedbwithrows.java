
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DatabaseComparison {
    public static void main(String[] args) {
        String jdbcUrl1 = "jdbc:your_sybase_db_url";
        String jdbcUrl2 = "jdbc:your_db2_db_url";
        String username = "your_db_username";
        String password = "your_db_password";

        try (Connection connection1 = DriverManager.getConnection(jdbcUrl1, username, password);
             Connection connection2 = DriverManager.getConnection(jdbcUrl2, username, password);
             FileInputStream excelFile = new FileInputStream("input.xlsx");
             Workbook workbook = new XSSFWorkbook(excelFile)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Skip the header row
                    continue;
                }

                String accountID = row.getCell(0).getStringCellValue();
                int roleCode;
                Cell roleCodeCell = row.getCell(1);

                // Check cell type and extract the role code accordingly
                if (roleCodeCell.getCellType() == CellType.STRING) {
                    // Role code is stored as a string
                    roleCode = Integer.parseInt(roleCodeCell.getStringCellValue().trim());
                } else if (roleCodeCell.getCellType() == CellType.NUMERIC) {
                    // Role code is stored as a numeric value
                    roleCode = (int) roleCodeCell.getNumericCellValue();
                } else {
                    // Handle other cell types or errors as needed
                    System.err.println("Unsupported cell type for role code.");
                    continue;
                }

                // Construct SQL query
                String query = "SELECT * FROM table_amenities WHERE accountid = ? AND rolecode = ?";

                // Execute queries on the first database (Sybase)
                try (PreparedStatement statement1 = connection1.prepareStatement(query)) {
                    statement1.setString(1, accountID);
                    statement1.setInt(2, roleCode);

                    ResultSet resultSet1 = statement1.executeQuery();
                    // Process and compare results as needed
                }

                // Execute queries on the second database (DB2)
                try (PreparedStatement statement2 = connection2.prepareStatement(query)) {
                    statement2.setString(1, accountID);
                    statement2.setInt(2, roleCode);

                    ResultSet resultSet2 = statement2.executeQuery();
                    // Process and compare results as needed
                }
            }

            System.out.println("Comparison completed successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
