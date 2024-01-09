package Hospital_manag;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.tools.Diagnostic;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;

import java.util.ArrayList;

public class HospitalManag<Diagnosis>{
	private ArrayList<Diagnosis> diagnosesList = new ArrayList<>();
	private Component patientFrame;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_manag";
    private static final String USER = "root";
    private static final String PASSWORD = "";
	protected static final Window mainMenuFrame = null;
    

    private Connection connectToDatabase() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalManag().createLoginFrame());
        
    }

    private void createLoginFrame() {
        JFrame loginFrame = new JFrame("Hospital Management System - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateLogin(usernameField.getText(), new String(passwordField.getPassword()))) {
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                    openMainMenu();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password");
                }
            }
        });

        loginPanel.add(createHeaderLabel1("Welcome to Hospital Management System"));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(createSpace(20));
        loginPanel.add(loginButton);

        // The next line is causing the issue
        loginFrame.getContentPane().add(loginPanel);
        loginFrame.setSize(500, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
    
    private Component createSpace(int i) {
        return new JLabel(" ");  // or return new JPanel() if you want an empty space
    }

	private boolean validateLogin(String username, String password) {
        // Replace this with actual validation logic
        return "admin".equals(username) && "admin".equals(password);
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Hospital Management System - Main Menu");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton doctorButton = createStyledButton1("Doctor");
        JButton patientButton = createStyledButton1("Patient");
        JButton diagnosisButton = createStyledButton1("Diagnosis");
        JButton logoutButton = createStyledButton1("Logout");

        doctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                openDoctorForm();
            }
        });

        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                openPatientForm();
            }
        });
        diagnosisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                openDiagnosisForm();
            }
        });
        // Add action listeners for other buttons...


        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                createLoginFrame();
            }
        });

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainMenuPanel.add(doctorButton);
        mainMenuPanel.add(patientButton);
        mainMenuPanel.add(diagnosisButton);
        mainMenuPanel.add(logoutButton);

        mainMenuFrame.getContentPane().add(mainMenuPanel);
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    protected void openPatientForm1() {
		// TODO Auto-generated method stub
		
	}

	private void openDoctorForm() {
        JFrame doctorFrame = new JFrame("Doctor Information");
        doctorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel doctorPanel = new JPanel(new BorderLayout());

        // Doctor table and form UI code
        DefaultTableModel doctorTableModel = new DefaultTableModel();
        
        doctorTableModel.addColumn("Name");
        doctorTableModel.addColumn("Password");
        doctorTableModel.addColumn("Experience");

        JTable doctorTable = new JTable(doctorTableModel);
        JScrollPane doctorScrollPane = new JScrollPane(doctorTable);

        JPanel doctorFormPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField doctorIdField = createStyledTextField1();
        JLabel doctorNameLabel = createStyledLabel1("Name:");
        JTextField doctorNameField = createStyledTextField1();
        JLabel doctorPasswordLabel = createStyledLabel1("Password:");
        JPasswordField doctorPasswordField = new JPasswordField();
        JLabel doctorExperienceLabel = createStyledLabel1("Experience:");
        JTextField doctorExperienceField = createStyledTextField1();

        JButton addDoctorButton = createStyledButton1("Add");
        JButton updateDoctorButton = createStyledButton1("Update");
        JButton deleteDoctorButton = createStyledButton1("Delete");
        JButton clearDoctorButton = createStyledButton1("Clear");
        JButton homeDoctorButton = createStyledButton1("Home");
        JButton showDoctorButton = createStyledButton1("Show");
        JButton diagnosisButton = createStyledButton1("Diagnosis");

        diagnosisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                openDoctorForm();
            }
        });
        
        // Action listeners for Doctor buttons
        addDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDoctorToDatabase(doctorNameField.getText(), new String(doctorPasswordField.getPassword()), doctorExperienceField.getText());
                updateDoctorTable(doctorTableModel);
                clearForm(doctorIdField, doctorNameField, doctorPasswordField, doctorExperienceField);
            }
        });

        updateDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assuming you have a selected row in the table, get the ID from the selected row
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow != -1) {
                    int doctorId = (int) doctorTableModel.getValueAt(selectedRow, 0);
                    updateDoctorInDatabase(doctorId, doctorNameField.getText(), new String(doctorPasswordField.getPassword()), doctorExperienceField.getText());
                    updateDoctorTable(doctorTableModel);
                    clearForm(doctorIdField, doctorNameField, doctorPasswordField, doctorExperienceField);
                } else {
                    JOptionPane.showMessageDialog(doctorFrame, "Select a doctor from the table to update.");
                }
            }
        });

        deleteDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow != -1) {
                    int doctorId = (int) doctorTableModel.getValueAt(selectedRow, 0);
                    deleteDoctorFromDatabase(doctorId);
                    updateDoctorTable(doctorTableModel);
                    clearForm(doctorIdField, doctorNameField, doctorPasswordField, doctorExperienceField);
                } else {
                    JOptionPane.showMessageDialog(doctorFrame, "Select a doctor from the table to delete.");
                }
            }
        });
        showDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call a method to fetch and display data from the database
                showDoctorsFromDatabase(doctorTableModel);
                
            }
        });
        clearDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm(doctorIdField, doctorNameField, doctorPasswordField, doctorExperienceField);
            }
        });

        homeDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doctorFrame.dispose();
                openMainMenu();
            }
        });

        showDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDoctorTable(doctorTableModel);
            }
        });

        // Similar action listeners for other Doctor buttons...

        doctorFormPanel.add(doctorNameLabel);
        doctorFormPanel.add(doctorNameField);
        doctorFormPanel.add(doctorPasswordLabel);
        doctorFormPanel.add(doctorPasswordField);
        doctorFormPanel.add(doctorExperienceLabel);
        doctorFormPanel.add(doctorExperienceField);
        doctorFormPanel.add(addDoctorButton);
        doctorFormPanel.add(updateDoctorButton);
        doctorFormPanel.add(deleteDoctorButton);
        doctorFormPanel.add(clearDoctorButton);
        doctorFormPanel.add(showDoctorButton);
        doctorFormPanel.add(homeDoctorButton);

        doctorPanel.add(createHeaderLabel1("Doctor Information"));
        doctorPanel.add(doctorScrollPane, BorderLayout.CENTER);
        doctorPanel.add(doctorFormPanel, BorderLayout.SOUTH);

        doctorFrame.getContentPane().add(doctorPanel);
        doctorFrame.setSize(800, 600);
        doctorFrame.setLocationRelativeTo(null);
        doctorFrame.setVisible(true);
    }

	private void updateDoctorInDatabase(int doctorId, String name, String password, String experience) {
	    // Implement code to update a doctor in the database based on the provided ID
	}
	private void deleteDoctorFromDatabase(int doctorId) {
	    try (Connection connection = connectToDatabase();
	         PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM doctor WHERE id = ?")) {
	        preparedStatement.setInt(1, doctorId);
	        preparedStatement.executeUpdate();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	private void showDoctorsFromDatabase(DefaultTableModel doctorTableModel) {
	    // Implement code to fetch data from the 'doctor' table and update the table model
	    try {
	        Connection connection = connectToDatabase();
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM doctor");

	        // Clear existing rows from the table
	        while (doctorTableModel.getRowCount() > 0) {
	            doctorTableModel.removeRow(0);
	        }

	        // Add rows from the result set to the table model
	        while (resultSet.next()) {
	            Object[] row = {
	                /*resultSet.getInt("id"),*/
	                resultSet.getString("name"),
	                resultSet.getString("password"),
	                resultSet.getString("experience")
	            };
	            doctorTableModel.addRow(row);
	        }

	        resultSet.close();
	        statement.close();
	        connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	private void addDoctorToDatabase(String name, String password, String experience) {
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO doctor (name, password, experience) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, experience);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();  // Handle the exception appropriately
        }
    }

    private void updateDoctorTable(DefaultTableModel doctorTableModel) {
        // Implement code to update the doctor table from the database
        // Fetch data from the database and update the table model
    }


    private void clearForm(JTextField idField, JTextField nameField, JTextField doctorIdField, JTextField experienceField) {
        idField.setText("");
        nameField.setText("");
        doctorIdField.setText("");
        experienceField.setText("");
    }

    // ... (Other methods)

    private JTextField createStyledTextField1() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JLabel createStyledLabel1(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private JButton createStyledButton1(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    private JLabel createHeaderLabel1(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }
    private void openPatientForm() {
        JFrame patientFrame = new JFrame("Patient Information");
        patientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel patientPanel = new JPanel(new BorderLayout());

        // Patient table and form UI code
        DefaultTableModel patientTableModel = new DefaultTableModel();
        patientTableModel.addColumn("Name");
        patientTableModel.addColumn("Address");
        patientTableModel.addColumn("Phone");
        patientTableModel.addColumn("Age");
        patientTableModel.addColumn("Gender");
        patientTableModel.addColumn("Blood Group");
        patientTableModel.addColumn("Pathologies");

        JTable patientTable = new JTable(patientTableModel);
        JScrollPane patientScrollPane = new JScrollPane(patientTable);

        JPanel patientFormPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        JTextField patientIdField = createStyledTextField1();
        JLabel patientNameLabel = createStyledLabel1("Name:");
        JTextField patientAgeField = createStyledTextField1();
        JLabel patientAddressLabel = createStyledLabel1("Address:");
        JTextField patientAddressField = createStyledTextField1();
        JLabel patientPhoneLabel = createStyledLabel1("Phone:");
        JTextField patientNameField = createStyledTextField1();
        JLabel patientAgeLabel = createStyledLabel1("Age:");
        JTextField patientPhoneField = createStyledTextField1();
        JLabel patientGenderLabel = createStyledLabel1("Gender:");
        JComboBox<String> patientGenderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        JLabel patientBloodGroupLabel = createStyledLabel1("Blood Group:");
        JComboBox<String> patientBloodGroupComboBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JLabel patientPathologiesLabel = createStyledLabel1("Pathologies:");
        JTextField patientPathologiesField = createStyledTextField1();

        JButton addPatientButton = createStyledButton1("Add");
        JButton updatePatientButton = createStyledButton1("Update");
        JButton deletePatientButton = createStyledButton1("Delete");
        JButton clearPatientButton = createStyledButton1("Clear");
        JButton homePatientButton = createStyledButton1("Home");
        JButton showPatientButton = createStyledButton1("Show");

        // Action listeners for Patient buttons
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatientToDatabase(patientNameField.getText(), patientAddressField.getText(),
                		patientPhoneField.getText(),patientAgeField.getText(),
                        (String) patientGenderComboBox.getSelectedItem(),
                        (String) patientBloodGroupComboBox.getSelectedItem(),
                        patientPathologiesField.getText());
                updatePatientTable(patientTableModel);
                clearForm(patientIdField, patientNameField, patientAddressField, patientPhoneField, 
                		patientAgeField,patientGenderComboBox,
                        patientBloodGroupComboBox, patientPathologiesField);
            }
        });

        updatePatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    int patientId = (int) patientTableModel.getValueAt(selectedRow, 0);
                    updatePatientInDatabase(patientId, patientNameField.getText(), patientAgeField.getText(),
                            patientAddressField.getText(), patientPhoneField.getText(),
                            (String) patientGenderComboBox.getSelectedItem(),
                            (String) patientBloodGroupComboBox.getSelectedItem(),
                            patientPathologiesField.getText());
                    updatePatientTable(patientTableModel);
                    clearForm(patientIdField, patientNameField, patientAgeField,
                            patientAddressField, patientPhoneField, patientGenderComboBox,
                            patientBloodGroupComboBox, patientPathologiesField);
                } else {
                    JOptionPane.showMessageDialog(patientFrame, "Please select a patient to update.");
                }
            }
        });
        

        deletePatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement code for deleting patient from the database
                // You may need to fetch the selected patient's ID from the table and perform the delete
                      int selectedRow = patientTable.getSelectedRow();
                      if (selectedRow != -1) {
                              int patientId = (int) patientTableModel.getValueAt(selectedRow, 0);
                              deletePatientFromDatabase(patientId);
                              updatePatientTable(patientTableModel);
                              clearForm(patientIdField, patientNameField, patientAgeField,
                                      patientAddressField, patientPhoneField, patientGenderComboBox,
                                      patientBloodGroupComboBox, patientPathologiesField); } 
                      else {
                JOptionPane.showMessageDialog(patientFrame, "Delete functionality not implemented.");
            }
           }            
        });
          
        showPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call a method to fetch and display data from the database
                showPatientsFromDatabase(patientTableModel);
                
            }
        });

        clearPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm(patientIdField, patientNameField, patientAgeField,
                        patientAddressField, patientPhoneField, patientGenderComboBox,
                        patientBloodGroupComboBox, patientPathologiesField);
            }
        });

        homePatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientFrame.dispose();
                openMainMenu();
            }
        });

        showPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatientTable(patientTableModel);           
                }
        });

        patientFormPanel.add(patientNameLabel);
        patientFormPanel.add(patientNameField);
        patientFormPanel.add(patientAddressLabel);
        patientFormPanel.add(patientAddressField);
        patientFormPanel.add(patientPhoneLabel);
        patientFormPanel.add(patientPhoneField);
        patientFormPanel.add(patientAgeLabel);
        patientFormPanel.add(patientAgeField);
        patientFormPanel.add(patientGenderLabel);
        patientFormPanel.add(patientGenderComboBox);
        patientFormPanel.add(patientBloodGroupLabel);
        patientFormPanel.add(patientBloodGroupComboBox);
        patientFormPanel.add(patientPathologiesLabel);
        patientFormPanel.add(patientPathologiesField);
        patientFormPanel.add(addPatientButton);
        patientFormPanel.add(updatePatientButton);
        patientFormPanel.add(deletePatientButton);
        patientFormPanel.add(clearPatientButton);
        patientFormPanel.add(showPatientButton);
        patientFormPanel.add(homePatientButton);

        patientPanel.add(createHeaderLabel1("Patient Information"));
        patientPanel.add(patientScrollPane, BorderLayout.CENTER);
        patientPanel.add(patientFormPanel, BorderLayout.SOUTH);

        patientFrame.getContentPane().add(patientPanel);
        patientFrame.setSize(800, 600);
        patientFrame.setLocationRelativeTo(null);
        patientFrame.setVisible(true);
    }

    private void addPatientToDatabase(String name,  String address, String phone,String age,
                                      String gender, String bloodGroup, String pathologies) {
          try (Connection connection = connectToDatabase();
        		  PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO patient (name, address, phone , age, gender, blood_group, pathologies) VALUES (?, ?, ?, ?, ? , ?, ?)") ){
        	  preparedStatement.setString(1, name);
              preparedStatement.setString(2, address);
              preparedStatement.setString(3, phone);
              preparedStatement.setString(4, age);
              preparedStatement.setString(5, gender);
              preparedStatement.setString(6, bloodGroup);
              preparedStatement.setString(7, pathologies);
              preparedStatement.executeUpdate();
          }catch(SQLException ex) {
        	  ex.printStackTrace();
        	  }
          }
    private void updatePatientTable(DefaultTableModel patientTableModel) {
        try {
            Connection connection = connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patient");

            // Clear existing rows from the table
            while (patientTableModel.getRowCount() > 0) {
                patientTableModel.removeRow(0);
            }

            // Add rows from the result set to the table model
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("phone"),
                    resultSet.getString("age"),
                    resultSet.getString("gender"),
                    resultSet.getString("blood_group"),
                    resultSet.getString("pathologies")
                };
                patientTableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void updatePatientInDatabase(int patientId, String name, String age, String address, String phone,
            String gender, String bloodGroup, String pathologies) {
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE patient SET name = ?, age = ?, address = ?, phone = ?, gender = ?, blood_group = ?, pathologies = ? WHERE id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, age);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, bloodGroup);
            preparedStatement.setString(7, pathologies);
            preparedStatement.setInt(8, patientId);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient with ID " + patientId + " updated in the database.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deletePatientFromDatabase(int patientId) {
        try (Connection connection = connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM patient WHERE id = ?")) {
            preparedStatement.setInt(1, patientId);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient with ID " + patientId + " deleted from the database.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void showPatientsFromDatabase(DefaultTableModel patientTableModel) {
	    // Implement code to fetch data from the 'doctor' table and update the table model
	    try {
	        Connection connection = connectToDatabase();
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM patient");

	        // Clear existing rows from the table
	        while (patientTableModel.getRowCount() > 0) {
	            patientTableModel.removeRow(0);
	        }

	        // Add rows from the result set to the table model
	        while (resultSet.next()) {
	            Object[] row = {
	                resultSet.getString("name"),
	                resultSet.getString("address"),
	                resultSet.getString("phone"),
	                resultSet.getString("age"),
	                resultSet.getString("gender"),
	                resultSet.getString("blood_group"),
	                resultSet.getString("pathologies")
	            };
	            patientTableModel.addRow(row);
	        }

	        resultSet.close();
	        statement.close();
	        connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
       

    
    private void clearForm(JTextField patientIdField, JTextField patientNameField, JTextField patientAgeField,
            JTextField patientAddressField, JTextField patientPhoneField, JComboBox<String> patientGenderComboBox,
            JComboBox<String> patientBloodGroupComboBox, JTextField patientPathologiesField) {
        // Clear all text fields and reset combo boxes
        patientIdField.setText("");
        patientNameField.setText("");
        patientAgeField.setText("");
        patientAddressField.setText("");
        patientPhoneField.setText("");
        patientGenderComboBox.setSelectedIndex(0); // Assuming Male is the default
        patientBloodGroupComboBox.setSelectedIndex(0); // Assuming A+ is the default
        patientPathologiesField.setText("");
}

    private void openDiagnosisForm() {
    	 JFrame diagnosisFrame = new JFrame("Diagnosis Information");
         diagnosisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         JPanel diagnosisPanel = new JPanel(new BorderLayout());
         
         DefaultTableModel diagnosisTableModel = new DefaultTableModel();
         
         diagnosisTableModel.addColumn("Name");
         diagnosisTableModel.addColumn("Symptoms");
         diagnosisTableModel.addColumn("Diagnosis");
         diagnosisTableModel.addColumn("Medicines");

         // Table pour afficher les informations de diagnostic
         JTable diagnosisTable = new JTable(diagnosisTableModel);
         JScrollPane diagnosisScrollPane = new JScrollPane(diagnosisTable);

         // Formulaire pour entrer de nouvelles informations de diagnostic
         JPanel formPanel = new JPanel(new GridLayout(6, 6, 10, 10));

         
         JLabel nameLabel = createStyledLabel1("Name:");
         JTextField nameField = createStyledTextField1();
         JLabel symptomsLabel = createStyledLabel1("Symptoms:");
         JTextField symptomsField = createStyledTextField1();
         JLabel diagnosisDetailsLabel = createStyledLabel1("Diagnosis:");
         JTextField diagnosisDetailsField = createStyledTextField1();
         JLabel medicinesLabel = createStyledLabel1("Medicines:");
         JTextField medicinesField = createStyledTextField1();

         JButton addButton = createStyledButton1("Add");
         JButton clearButton = createStyledButton1("Clear");
         JButton homeButton = createStyledButton1("Home");
         JButton showButton = createStyledButton1("Show");
         JButton printButton = createStyledButton1("Print Prescription");

         // Action listeners pour les boutons
         addButton.addActionListener(new ActionListener() {
        	 @Override
        	 public void actionPerformed(ActionEvent e) {
             addDiagnosisToDatabase( nameField.getText(), symptomsField.getText(), diagnosisDetailsField.getText(), medicinesField.getText());
             updateDoctorTable(diagnosisTableModel);
             clearForm(nameField, symptomsField, diagnosisDetailsField, medicinesField);	 
        	 }
         });
         
         showButton.addActionListener(new ActionListener() {
        	 @Override
        	 public void actionPerformed(ActionEvent e) {
        		 showFromDatabase(diagnosisTableModel);
        	 }
         });


         homeButton.addActionListener(new ActionListener() {
        	 @Override
        	 public void actionPerformed(ActionEvent e) {
             diagnosisFrame.dispose();
             openMainMenu();
        	 }
         });
         
         

         printButton.addActionListener(e -> {
             int selectedRow = diagnosisTable.getSelectedRow();
             if (selectedRow != -1) {
                 generatePrescriptionPDF(diagnosesList.get(selectedRow));
             } else {
                 JOptionPane.showMessageDialog(diagnosisFrame, "Select a diagnosis to print prescription.");
             }
         });
         
         
         diagnosisFrame.getContentPane().add(diagnosisPanel);
         diagnosisFrame.setSize(800, 600);
         diagnosisFrame.setLocationRelativeTo(null);
         diagnosisFrame.setVisible(true);

        
         formPanel.add(nameLabel);
         formPanel.add(nameField);
         formPanel.add(symptomsLabel);
         formPanel.add(symptomsField);
         formPanel.add(diagnosisDetailsLabel);
         formPanel.add(diagnosisDetailsField);
         formPanel.add(medicinesLabel);
         formPanel.add(medicinesField);
         formPanel.add(addButton);
         formPanel.add(clearButton);
         formPanel.add(showButton);
         formPanel.add(printButton);
         formPanel.add(homeButton);

         diagnosisPanel.add(createHeaderLabel1("Diagnosis Information"));
         diagnosisPanel.add(diagnosisScrollPane, BorderLayout.CENTER);
         diagnosisPanel.add(formPanel, BorderLayout.SOUTH);

         diagnosisFrame.getContentPane().add(diagnosisPanel);
         diagnosisFrame.setSize(800, 600);
         diagnosisFrame.setLocationRelativeTo(null);
         diagnosisFrame.setVisible(true);
     }

     private void generatePrescriptionPDF(Diagnosis diagnosis) {
		// TODO Auto-generated method stub
    	
		
	}

	// Méthode pour stocker les informations de diagnostic dans la base de données
     private void addDiagnosisToDatabase(  String name, String symptoms, String diagnosis, String medicines) {
         try (Connection connection = connectToDatabase();
              PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO diagnosis ( name, symptoms, diagnosis, medicines) VALUES (?, ?, ?, ?)")) {
             
             preparedStatement.setString(1, name);
             preparedStatement.setString(2, symptoms);
             preparedStatement.setString(3, diagnosis);
             preparedStatement.setString(4, medicines);
             preparedStatement.executeUpdate();
         } catch (SQLException ex) {
             ex.printStackTrace();  // Gérer l'exception de manière appropriée
         }
     }
     private void showFromDatabase(DefaultTableModel diagnosisTableModel) {
 	    // Implement code to fetch data from the 'doctor' table and update the table model
    	 diagnosisTable.getSelectionModel().addListSelectionListener(e -> {
             int selectedRow = diagnosisTable.getSelectedRow();
             if (selectedRow != -1) {
                 Diagnosis selectedDiagnosis = new Diagnosis(
                         (String) diagnosisTableModel.getValueAt(selectedRow, 1),
                         (String) diagnosisTableModel.getValueAt(selectedRow, 2),
                         (String) diagnosisTableModel.getValueAt(selectedRow, 3),
                         (String) diagnosisTableModel.getValueAt(selectedRow, 4)
                 );
                 generatePrescriptionPDF(selectedDiagnosis);
             }
         });
    	 
    	 
    	 
    	 try {
 	        Connection connection = connectToDatabase();
 	        Statement statement = connection.createStatement();
 	        ResultSet resultSet = statement.executeQuery("SELECT * FROM diagnosis");

 	        // Clear existing rows from the table
 	        while (diagnosisTableModel.getRowCount() > 0) {
 	            diagnosisTableModel.removeRow(0);
 	        }

 	        // Add rows from the result set to the table model
 	        while (resultSet.next()) {
 	            Object[] row = {
 	                resultSet.getString("name"),
 	                resultSet.getString("symptoms"),
 	                resultSet.getString("diagnosis"),
 	                resultSet.getString("medicines")
 	                
 	            };
 	            diagnosisTableModel.addRow(row);
 	        }

 	        resultSet.close();
 	        statement.close();
 	        connection.close();
 	    } catch (SQLException ex) {
 	        ex.printStackTrace();
 	    }
 	}
    	
    }
