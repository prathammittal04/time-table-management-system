import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyTimetableproject{
    static Connection conn;

    public static void main(String[] args) {
        connectToDatabase();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Timetable Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout());

            JPanel titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("Welcome to the Timetable Management System!");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titlePanel.add(titleLabel);

            JPanel buttonPanel = createButtonPanel(frame);

            mainPanel.add(titlePanel, BorderLayout.NORTH);
            mainPanel.add(buttonPanel, BorderLayout.CENTER);

            frame.add(mainPanel, BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JPanel createButtonPanel(JFrame frame) {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton displayButton = new JButton("Display Timetable");
        JButton addButton = new JButton("Add Subject");
        JButton removeButton = new JButton("Remove Subject");
        JButton modifyButton = new JButton("Modify Subject");

        displayButton.addActionListener(e -> displayTimetable(frame));
        addButton.addActionListener(e -> addSubject(frame));
        removeButton.addActionListener(e -> removeSubject(frame));
        modifyButton.addActionListener(e -> modifySubject(frame));

        buttonPanel.add(displayButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(modifyButton);

        return buttonPanel;
    }


    private static void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/timetable_db", "root", "Seema_0306");
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println("exception ref: " + e);
        }
    }

    private static void displayTimetable(JFrame frame) {
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM timetable");

            int maxPeriod = 0;
            int maxDay = 0;
            while (rs.next()) {
                int period = rs.getInt("period");
                int day = rs.getInt("day");
                if (period > maxPeriod) {
                    maxPeriod = period;
                }
                if (day > maxDay) {
                    maxDay = day;
                }
            }

            rs.beforeFirst();

            Object[][] data = new Object[maxPeriod][maxDay + 1];

            while (rs.next()) {
                int period = rs.getInt("period");
                int day = rs.getInt("day");
                String subject = rs.getString("subject");
                data[period - 1][0] = "Period " + period;
                data[period - 1][day] = subject;
            }

            String[] columnNames = new String[maxDay + 1];
            columnNames[0] = "Period";
            for (int i = 1; i <= maxDay; i++) {
                columnNames[i] = "Day " + i;
            }

            JTable timetableTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(timetableTable);

            timetableTable.setFillsViewportHeight(true);
            timetableTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            timetableTable.getTableHeader().setReorderingAllowed(false);

            JOptionPane.showMessageDialog(frame, scrollPane, "Timetable", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            System.out.println("exception ref: " + e);
        }
    }

    private static void addSubject(JFrame frame) {
        JTextField dayField = new JTextField(5);
        JTextField periodField = new JTextField(5);
        JTextField subjectField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Day:"));
        panel.add(dayField);
        panel.add(new JLabel("Period:"));
        panel.add(periodField);
        panel.add(new JLabel("Subject:"));
        panel.add(subjectField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Subject", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int day = Integer.parseInt(dayField.getText());
            int period = Integer.parseInt(periodField.getText());
            String subject = subjectField.getText();

            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO timetable (day, period, subject) VALUES (?, ?, ?)");
                stmt.setInt(1, day);
                stmt.setInt(2, period);
                stmt.setString(3, subject);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Subject added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                System.out.println("exception ref: " + e);
            }
        }
    }
    private static void removeSubject(JFrame frame) {
        JTextField dayField = new JTextField(5);
        JTextField periodField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.add(new JLabel("Day:"));
        panel.add(dayField);
        panel.add(new JLabel("Period:"));
        panel.add(periodField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Remove Subject", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int day = Integer.parseInt(dayField.getText());
            int period = Integer.parseInt(periodField.getText());

            try {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM timetable WHERE day = ? AND period = ?");
                stmt.setInt(1, day);
                stmt.setInt(2, period);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Subject removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No subject found at the specified day and period.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                System.out.println("exception ref: " + e);
            }
        }
    }

    private static void modifySubject(JFrame frame) {
        JTextField dayField = new JTextField(5);
        JTextField periodField = new JTextField(5);
        JTextField newSubjectField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Day:"));
        panel.add(dayField);
        panel.add(new JLabel("Period:"));
        panel.add(periodField);
        panel.add(new JLabel("New Subject:"));
        panel.add(newSubjectField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Modify Subject", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int day = Integer.parseInt(dayField.getText());
            int period = Integer.parseInt(periodField.getText());
            String newSubject = newSubjectField.getText();

            try {
                PreparedStatement stmt = conn.prepareStatement("UPDATE timetable SET subject = ? WHERE day = ? AND period = ?");
                stmt.setString(1, newSubject);
                stmt.setInt(2, day);
                stmt.setInt(3, period);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Subject modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No subject found at the specified day and period.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                System.out.println("exception ref: " + e);
            }
        }
    }
}