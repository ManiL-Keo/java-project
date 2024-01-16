import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import java.io.FileWriter;


import com.opencsv.exceptions.CsvValidationException;
import com.toedter.calendar.JDateChooser;
class Hotel {
    private String name;
    private String address;
    private String contactDetails;
    private ArrayList<Room> rooms;


    public Hotel(String name, String address, String contactDetails) {
        this.name = name;
        this.address = address;
        this.contactDetails = contactDetails;
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    // Checks the availability of a room for a given date range
    public boolean checkAvailability(int roomNumber, Date startDate, Date endDate) {
        // Implementation to check room availability for the given date range
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            return room.isAvailable(startDate, endDate);
        }
        return false;
    }

    // Books a room for a customer for a specified date range
    // Modify the bookRoom method to accept multiple room numbers
    public void bookRoom(String roomNumbers, Customer customer, Date startDate, Date endDate) {
        // Split the input string into individual room numbers
        String[] roomNumberStrings = roomNumbers.split(",");

        for (String roomNumberString : roomNumberStrings) {
            try {
                int roomNumber = Integer.parseInt(roomNumberString.trim());

                Room room = findRoomByNumber(roomNumber);
                if (room != null && room.isAvailable(startDate, endDate)) {
                    room.bookRoom(customer, startDate, endDate);
                    System.out.println("Room " + roomNumber + " booked successfully!");
                } else {
                    System.out.println("Room " + roomNumber + " not available for the specified date range.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid room number: " + roomNumberString.trim());
            }
        }
    }


    // Checks out a customer from a room
    // Modify the checkOut method to accept multiple room numbers
    public void checkOut(String roomNumbers) {
        // Split the input string into individual room numbers
        String[] roomNumberStrings = roomNumbers.split(",");

        for (String roomNumberString : roomNumberStrings) {
            try {
                int roomNumber = Integer.parseInt(roomNumberString.trim());

                Room room = findRoomByNumber(roomNumber);
                if (room != null && room.isOccupied()) {
                    room.checkOut();
                    System.out.println("Room " + roomNumber + " checked out successfully!");
                } else {
                    System.out.println("Room " + roomNumber + " is not occupied or does not exist.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid room number: " + roomNumberString.trim());
            }
        }
    }


    // Finds a room by its room number
    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // Gets a list of all rooms
    public ArrayList<Room> getRooms() {
        return rooms;
    }
}

class Room {
    private int roomNumber;
    private String type;
    private double price;
    private boolean availability;
    private Customer customer;


    // Constructor initializes a room with a room number, type, and price
    public Room(int roomNumber, String type, double price) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.availability = true;
        this.customer = null;
    }

    // Gets the room number
    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isAvailable(Date startDate, Date endDate) {
        if (availability) {
            // Check if the room is available for the given date range
            if (customer == null) {
                // If there is no customer, the room is available
                return true;
            } else {
                // If there is a customer, check if the date range overlaps with the existing booking
                boolean isBeforeExistingBooking = endDate.before(customer.getCheckInDate()) || endDate.equals(customer.getCheckInDate());
                boolean isAfterExistingBooking = startDate.after(customer.getCheckOutDate()) || startDate.equals(customer.getCheckOutDate());

                return isBeforeExistingBooking || isAfterExistingBooking;
            }
        }
        return false;
    }

    public void bookRoom(Customer customer, Date startDate, Date endDate) {
        if (isAvailable(startDate, endDate)) {
            // Set the customer and update availability
            this.customer = customer;
            this.availability = false;

            // Update Customer check-in and check-out dates
            customer.checkIn(this.roomNumber, startDate);
            customer.checkOut(endDate);


            System.out.println("Room booked successfully!");
        } else {
            System.out.println("Room not available for the specified date range.");
        }
    }

    public void checkOut() {
        if (isOccupied()) {
            // Perform any necessary actions related to checking out a customer
            this.customer = null;
            this.availability = true;

            System.out.println("Room checked out successfully!");
        } else {
            System.out.println("Room is not occupied.");
        }
    }

    public boolean isOccupied() {

        return customer != null;
    }
    public Customer getCustomer() {

        return customer;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }


    public boolean setAvailability(boolean availability) {
        return availability;
    }
}


class Customer {
    private String name;
    private String contactDetails;
    private Date checkInDate;
    private Date checkOutDate;
    private int roomNumber;

    public Customer(String name, String contactDetails) {
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public void checkIn(int roomNumber, Date checkInDate) {
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
    }

    public void checkOut(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }
}
class IDandPasswords {

    HashMap<String, String> logininfo = new HashMap<String, String>();

    IDandPasswords() {

        logininfo.put("Admin", "123");
    }

    public HashMap getLoginInfo() {
        return logininfo;
    }
}


class LoginPage implements ActionListener {
    JFrame frame = new JFrame();
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Reset");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("userID:");
    JLabel userPasswordLabel = new JLabel("password:");
    JLabel messageLabel = new JLabel();
    JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    HashMap<String, String> logininfo = new HashMap<String, String>();

    LoginPage(HashMap<String, String> loginInfoOriginal) {

        logininfo = loginInfoOriginal;

        userIDLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);

        messageLabel.setBounds(125, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 25));

        userIDField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);

        showPasswordCheckBox.setBounds(125, 180, 150, 25);
        showPasswordCheckBox.addActionListener(this);
        showPasswordCheckBox.setFocusable(false);

        loginButton.setBounds(125, 210, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        resetButton.setBounds(225, 210, 100, 25);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(showPasswordCheckBox);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
        }

        if (e.getSource() == loginButton) {
            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            if (logininfo.containsKey(userID)) {
                if (logininfo.get(userID).equals(password)) {
                    messageLabel.setForeground(Color.green);
                    messageLabel.setText("");
                    frame.dispose();
                    HotelManagementGUI hotelManagementGUI = new HotelManagementGUI(userID);
                } else {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Wrong password");
                }
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Username not found");
            }
        }
        if (e.getSource() == showPasswordCheckBox) {
            if (showPasswordCheckBox.isSelected()) {
                userPasswordField.setEchoChar((char) 0); // Show password
            } else {
                userPasswordField.setEchoChar('*'); // Hide password
            }
        }
    }
}
class HotelManagementGUI {
    private Hotel hotel;
    private JPanel inputPanel;
    private String userID;

    public HotelManagementGUI(String userID) {
        this.hotel = hotel;
        this.userID = userID;
        this.hotel = new Hotel("My Hotel", "123 Main St", "123-456-7890");
        createAndShowGUI();
    }

    private void createAndShowGUI() {

        JFrame frame = new JFrame("Hotel Management System");

        JLabel text = new JLabel("Welcome To Booking System");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1000, 10));

        JButton addRoomButton = new JButton("Add Room");
        JButton removeRoomButton = new JButton("Remove Room");
        JButton checkAvailabilityButton = new JButton("Check Availability");
        JButton bookRoomButton = new JButton("Book Room");
        JButton checkOutButton = new JButton("Check Out");
        JButton viewAllRoomsButton = new JButton("View All Rooms");
        JButton viewBookingButton = new JButton("View Booking Room");

        addRoomButton.setFocusable(false);
        removeRoomButton.setFocusable(false);
        checkOutButton.setFocusable(false);
        bookRoomButton.setFocusable(false);
        checkOutButton.setFocusable(false);
        checkAvailabilityButton.setFocusable(false);
        viewBookingButton.setFocusable(false);
        viewAllRoomsButton.setFocusable(false);

        Dimension buttonSize = new Dimension(150, 30);
        addRoomButton.setPreferredSize(buttonSize);
        removeRoomButton.setPreferredSize(buttonSize);
        checkAvailabilityButton.setPreferredSize(buttonSize);
        bookRoomButton.setPreferredSize(buttonSize);
        checkOutButton.setPreferredSize(buttonSize);
        viewAllRoomsButton.setPreferredSize(buttonSize);

        inputPanel = new JPanel(new GridLayout(0, 2));

        // View All Rooms Button
        viewAllRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve all rooms from the hotel
                ArrayList<Room> allRooms = hotel.getRooms();

                if (!allRooms.isEmpty()) {
                    // Prepare data for displaying in a table
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String[][] data = new String[allRooms.size()][4];
                    int i = 0;

                    // Populate the data array with room details
                    for (Room room : allRooms) {
                        data[i][0] = String.valueOf(room.getRoomNumber());
                        data[i][1] = room.getType();
                        data[i][2] = String.valueOf(room.getPrice());
                        data[i][3] = room.isAvailable(new Date(), new Date()) ? "Available" : "Booked";

                        i++;
                    }

                    String[] columnNames = {"Room Number", "Type", "Price", "Availability"};

                    // Create a table and display it in a JOptionPane
                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JOptionPane.showMessageDialog(null, scrollPane, "All Rooms Information", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No rooms available.");
                }
            }
        });
        // Add Room Button
        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for adding a room
                JTextField roomNumberField = new JTextField();
                JComboBox<String> roomTypeComboBox = new JComboBox<>(new String[]{"Normal", "VIP", "VVIP"});
                JTextField roomPriceField = new JTextField();

                // Add an ActionListener to the roomTypeComboBox to update the room price dynamically
                roomTypeComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedRoomType = (String) roomTypeComboBox.getSelectedItem();
                        double price;

                        // Set the price based on the selected room type
                        switch (selectedRoomType) {
                            case "Normal":
                                price = 80.0;
                                break;
                            case "VIP":
                                price = 110.0;
                                break;
                            case "VVIP":
                                price = 150.0;
                                break;
                            default:
                                price = 0.0; // Default value or handle error as needed
                        }

                        roomPriceField.setText(String.valueOf(price));
                    }
                });

                JPanel inputPanel = new JPanel(new GridLayout(0, 2));
                inputPanel.add(new JLabel("Room Number:"));
                inputPanel.add(roomNumberField);
                inputPanel.add(new JLabel("Room Type:"));
                inputPanel.add(roomTypeComboBox);
                inputPanel.add(new JLabel("Room Price:"));
                inputPanel.add(roomPriceField);

                // Show input dialog and handle user input
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse input and create a new room
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        String type = (String) roomTypeComboBox.getSelectedItem();
                        double price = Double.parseDouble(roomPriceField.getText());

                        Room newRoom = new Room(roomNumber, type, price);
                        hotel.addRoom(newRoom);
                        JOptionPane.showMessageDialog(null, "Room added successfully!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            }
        });


        // Remove Room Button
        removeRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for removing a room
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Room Number to remove:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);

                // Show input dialog and handle user input
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse input, find and remove the specified room
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        Room roomToRemove = hotel.findRoomByNumber(roomNumber);

                        if (roomToRemove != null) {
                            hotel.removeRoom(roomToRemove);
                            JOptionPane.showMessageDialog(null, "Room removed successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Room not found!");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numbers.");
                    }
                }
            }
        });

        // Check Availability Button
        checkAvailabilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for checking room availability
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Number:"));
                JTextField roomNumberField = new JTextField();
                inputPanel.add(roomNumberField);

                // Add JDateChooser for Start Date
                inputPanel.add(new JLabel("Enter Start Date:"));
                JDateChooser startDateChooser = new JDateChooser();
                inputPanel.add(startDateChooser);

                // Add JDateChooser for End Date
                inputPanel.add(new JLabel("Enter End Date:"));
                JDateChooser endDateChooser = new JDateChooser();
                inputPanel.add(endDateChooser);

                // Show input dialog and handle user input
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse input, check room availability, and show the result
                        int roomNumber = Integer.parseInt(roomNumberField.getText());
                        Date startDate = startDateChooser.getDate();
                        Date endDate = endDateChooser.getDate();

                        if (startDate != null && endDate != null) {
                            boolean isAvailable = hotel.checkAvailability(roomNumber, startDate, endDate);

                            if (isAvailable) {
                                JOptionPane.showMessageDialog(null, "Room is available for the given date range.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Room is not available for the given date range.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select valid start and end dates.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input or date format!");
                    }
                }
            }
        });



        // Book Room Button
        // Modify the bookRoomButton ActionListener
        bookRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for booking rooms
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Numbers (comma-separated):"));
                JTextField roomNumbersField = new JTextField();
                inputPanel.add(roomNumbersField);
                inputPanel.add(new JLabel("Enter Customer Name:"));
                JTextField customerNameField = new JTextField();
                inputPanel.add(customerNameField);
                inputPanel.add(new JLabel("Enter Customer Contact Details:"));
                JTextField contactDetailsField = new JTextField();
                inputPanel.add(contactDetailsField);

                // Add JDateChooser for check-in date
                inputPanel.add(new JLabel("Enter Check-in Date:"));
                JDateChooser checkInDateChooser = new JDateChooser();
                inputPanel.add(checkInDateChooser);

                // Add JDateChooser for check-out date
                inputPanel.add(new JLabel("Enter Check-out Date:"));
                JDateChooser checkOutDateChooser = new JDateChooser();
                inputPanel.add(checkOutDateChooser);

                // Show input dialog and handle user input
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse input and book the rooms for the specified date range
                        String roomNumbers = roomNumbersField.getText();
                        String customerName = customerNameField.getText();
                        String contactDetails = contactDetailsField.getText();
                        Customer customer = new Customer(customerName, contactDetails);

                        // Retrieve selected dates from JDateChooser
                        Date startDate = checkInDateChooser.getDate();
                        Date endDate = checkOutDateChooser.getDate();

                        hotel.bookRoom(roomNumbers, customer, startDate, endDate);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input or date format!");
                    }
                }
            }
        });



        // Check Out Button
        // Modify the checkOutButton ActionListener
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display input fields for checking out rooms
                inputPanel.removeAll();
                inputPanel.add(new JLabel("Enter Room Numbers to check out (comma-separated):"));
                JTextField roomNumbersField = new JTextField();
                inputPanel.add(roomNumbersField);

                // Show input dialog and handle user input
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Enter Room Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // Parse input and check out the specified rooms
                        String roomNumbers = roomNumbersField.getText();
                        hotel.checkOut(roomNumbers);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input or room number format!");
                    }
                }
            }
        });

        // View Booking Button
        viewBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Improved logic for viewing all booked rooms information
                ArrayList<Room> bookedRooms = new ArrayList<>();

                for (Room room : hotel.getRooms()) {
                    if (room.isOccupied() && room.getCustomer() != null) {
                        bookedRooms.add(room);
                    }
                }

                if (!bookedRooms.isEmpty()) {
                    // Prepare data for displaying booked room information
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String[][] data = new String[bookedRooms.size()][6];
                    int i = 0;

                    // Populate the data array with booked room details
                    for (Room room : bookedRooms) {
                        Customer customer = room.getCustomer();

                        data[i][0] = String.valueOf(room.getRoomNumber());
                        data[i][1] = String.valueOf(!room.isOccupied());
                        data[i][2] = customer.getCheckInDate() != null ? dateFormat.format(customer.getCheckInDate()) : "Not available";
                        data[i][3] = customer.getCheckOutDate() != null ? dateFormat.format(customer.getCheckOutDate()) : "Not available";
                        data[i][4] = customer.getName();
                        data[i][5] = customer.getContactDetails();

                        i++;
                    }

                    String[] columnNames = {"Room Number", "Availability", "Check-in Date", "Check-out Date", "Customer Name", "Contact Details"};

                    // Create a table and display it in a JOptionPane
                    JTable table = new JTable(data, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JOptionPane.showMessageDialog(null, scrollPane, "Booked Rooms Information", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No rooms are currently booked.");
                }
            }
        });



        panel.add(text);
        panel.add(viewAllRoomsButton);
        panel.add(viewBookingButton);
        panel.add(addRoomButton);
        panel.add(removeRoomButton);
        panel.add(checkAvailabilityButton);
        panel.add(bookRoomButton);
        panel.add(checkOutButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.add(panel);
        frame.setVisible(true);


    }



}

public class GUI {
    public static void main(String[] args) {
        Hotel hotel = new Hotel("My Hotel", "123 Main St", "123-456-7890");
        IDandPasswords idandPasswords = new IDandPasswords();



        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());
        });
    }
}
