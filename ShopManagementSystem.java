
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;

public class ShopManagementSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}

class User {

    private String username;
    private String password;
    private String role;
    private String name;

    public User(String username, String password, String role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}

class LoginFrame extends JFrame {

    private static Map<String, User> users = new HashMap<>();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public LoginFrame() {
        setTitle("Shop Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Shop Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);

        roleCombo = new JComboBox<>(new String[]{"Seller", "Customer"});
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> showRegisterDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleCombo.getSelectedItem().toString().toLowerCase();

        User user = users.get(username);

        if (user != null && user.getPassword().equals(password) && user.getRole().equals(role)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            this.dispose();

            if (role.equals("seller")) {
                new SellerDashboard(user).setVisible(true);
            } else {
                new CustomerDashboard(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"seller", "customer"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String role = roleCombo.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!");
                return;
            }

            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(dialog, "Username already exists!");
                return;
            }

            users.put(username, new User(username, password, role, name));
            JOptionPane.showMessageDialog(dialog, "Registration successful!");
            dialog.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}

class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    private int productId;
    private String productName;
    private Date manufacturingDate;
    private Date expiryDate;
    private float price;
    private int quantity;

    public Product(int productId, String productName, Date manufacturingDate, Date expiryDate, float price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.manufacturingDate = manufacturingDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Date getManufacturingDate() {
        return manufacturingDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }
}

class ProductManager {

    private static ProductManager instance;
    private ArrayList<Product> products;
    private static int nextId = 1;

    private ProductManager() {
        this.products = new ArrayList<>();
        loadProducts();
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void addNewProduct(Product product) {
        products.add(product);
        saveProducts();
    }

    public void deleteProduct(int productId) {
        products.removeIf(p -> p.getProductId() == productId);
        saveProducts();
    }

    public Product getProductById(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getAllProducts() {
        return products;
    }

    public static int getNextId() {
        return nextId++;
    }

    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadProducts() {
        File file = new File("products.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                products = (ArrayList<Product>) ois.readObject();
                if (!products.isEmpty()) {
                    nextId = products.stream()
                            .mapToInt(Product::getProductId)
                            .max()
                            .orElse(0) + 1;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading products: " + e.getMessage());
                products = new ArrayList<>();
            }
        }
    }
}

class Customer {

    private int customerId;
    private String customerName;
    private long phoneNumber;
    private String orderStatus;

    public Customer(int customerId, String customerName, long phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.orderStatus = "No Active Orders";
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}

class CustomerManager {

    private static CustomerManager instance;
    private ArrayList<Customer> customers;
    private static int nextId = 1;

    private CustomerManager() {
        this.customers = new ArrayList<>();
    }

    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void modifyCustomer(int customerId, String newName, long newPhone) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                customer.setCustomerName(newName);
                customer.setPhoneNumber(newPhone);
                break;
            }
        }
    }

    public void deleteCustomer(int customerId) {
        customers.removeIf(c -> c.getCustomerId() == customerId);
    }

    public void updateOrderStatus(int customerId, String status) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                customer.setOrderStatus(status);
                break;
            }
        }
    }

    public Customer getCustomerById(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }

    public static int getNextId() {
        return nextId++;
    }
}

class Seller {

    private int sellerId;
    private String sellerName;
    private int numberOfBills;
    private boolean isVerified;

    public Seller(int sellerId, String sellerName) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.numberOfBills = 0;
        this.isVerified = false;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getNumberOfBills() {
        return numberOfBills;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public boolean verifyMember(int verificationCode) {
        if (verificationCode == 1234) {
            isVerified = true;
            return true;
        }
        return false;
    }

    public void incrementBills() {
        numberOfBills++;
    }
}

class Bill implements Serializable {

    private static final long serialVersionUID = 1L;
    private int billId;
    private String customerName;
    private ArrayList<BillItem> items;
    private double totalAmount;
    private String paymentMethod;
    private Date billDate;

    public Bill(int billId, String customerName, String paymentMethod) {
        this.billId = billId;
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
        this.billDate = new Date();
    }

    public void addItem(Product product, int quantity) {
        items.add(new BillItem(product, quantity));
        totalAmount += product.getPrice() * quantity;
        product.decreaseQuantity(quantity);
    }

    public int getBillId() {
        return billId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<BillItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Date getBillDate() {
        return billDate;
    }
}

class BillItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private String productName;
    private int productId;
    private int quantity;
    private float price;
    private double subtotal;

    public BillItem(Product product, int quantity) {
        this.productName = product.getProductName();
        this.productId = product.getProductId();
        this.quantity = quantity;
        this.price = product.getPrice();
        this.subtotal = price * quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }
}

class SellerDashboard extends JFrame {

    private User currentUser;
    private ProductManager productManager;
    private CustomerManager customerManager;
    private Seller seller;
    private JTabbedPane tabbedPane;
    private static int nextBillId = 1;
    private ArrayList<Bill> bills = new ArrayList<>();

    private JComboBox<String> productCombo;
    private JTextField customerNameField;
    private JTextField quantityField;
    private JComboBox<String> paymentCombo;
    private JLabel stockLabel;
    private DefaultTableModel billHistoryModel;
    private JLabel totalSalesLabel;

    public SellerDashboard(User user) {
        this.currentUser = user;
        this.productManager = ProductManager.getInstance();
        this.customerManager = CustomerManager.getInstance();
        this.seller = new Seller(1, user.getName());

        setTitle("Shop Management System - Seller Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(52, 73, 94));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (Seller)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomePanel.add(welcomeLabel);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Products", createProductPanel());
        tabbedPane.addTab("Customers", createCustomerPanel());
        tabbedPane.addTab("Billing", createBillingPanel());
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Seller Info", createSellerPanel());

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 2) {
                    refreshProductCombo();
                }
            }
        });

        setLayout(new BorderLayout());
        add(welcomePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Name", "Mfg Date", "Exp Date", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Product");
        JButton deleteBtn = new JButton("Delete Product");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> showAddProductDialog(model));
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                productManager.deleteProduct(id);
                refreshProductTable(model);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to delete!");
            }
        });
        refreshBtn.addActionListener(e -> refreshProductTable(model));

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshProductTable(model);

        return panel;
    }

    private void showAddProductDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Add Product", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField mfgDateField = new JTextField();
        JTextField expDateField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();

        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Mfg Date (dd-MM-yyyy):"));
        panel.add(mfgDateField);
        panel.add(new JLabel("Exp Date (dd-MM-yyyy):"));
        panel.add(expDateField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Product product = new Product(
                        ProductManager.getNextId(),
                        nameField.getText(),
                        sdf.parse(mfgDateField.getText()),
                        sdf.parse(expDateField.getText()),
                        Float.parseFloat(priceField.getText()),
                        Integer.parseInt(quantityField.getText())
                );
                productManager.addNewProduct(product);
                refreshProductTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(saveBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshProductTable(DefaultTableModel model) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (Product product : productManager.getAllProducts()) {
            model.addRow(new Object[]{
                product.getProductId(),
                product.getProductName(),
                sdf.format(product.getManufacturingDate()),
                sdf.format(product.getExpiryDate()),
                product.getPrice(),
                product.getQuantity()
            });
        }
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Name", "Phone", "Order Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Customer");
        JButton modifyBtn = new JButton("Modify Customer");
        JButton deleteBtn = new JButton("Delete Customer");
        JButton updateStatusBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> showAddCustomerDialog(model));
        modifyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                showModifyCustomerDialog(model, row);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to modify!");
            }
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                customerManager.deleteCustomer(id);
                refreshCustomerTable(model);
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer to delete!");
            }
        });
        updateStatusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                String status = JOptionPane.showInputDialog(this, "Enter new order status:");
                if (status != null && !status.trim().isEmpty()) {
                    customerManager.updateOrderStatus(id, status);
                    refreshCustomerTable(model);
                    JOptionPane.showMessageDialog(this, "Status updated successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer!");
            }
        });
        refreshBtn.addActionListener(e -> refreshCustomerTable(model));

        buttonPanel.add(addBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(updateStatusBtn);
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private void showAddCustomerDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Add Customer", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                Customer customer = new Customer(
                        CustomerManager.getNextId(),
                        nameField.getText(),
                        Long.parseLong(phoneField.getText())
                );
                customerManager.addCustomer(customer);
                refreshCustomerTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Customer added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(saveBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showModifyCustomerDialog(DefaultTableModel model, int row) {
        int id = (int) model.getValueAt(row, 0);
        Customer customer = customerManager.getCustomerById(id);

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found!");
            return;
        }

        JDialog dialog = new JDialog(this, "Modify Customer", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(customer.getCustomerName());
        JTextField phoneField = new JTextField(String.valueOf(customer.getPhoneNumber()));

        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                customerManager.modifyCustomer(id, nameField.getText(), Long.parseLong(phoneField.getText()));
                refreshCustomerTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Customer modified successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(saveBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void refreshCustomerTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Customer customer : customerManager.getAllCustomers()) {
            model.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhoneNumber(),
                customer.getOrderStatus()
            });
        }
    }

    private JPanel createBillingPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);

        JPanel upperPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Billing Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        customerNameField = new JTextField(20);
        productCombo = new JComboBox<>();
        quantityField = new JTextField(10);
        paymentCombo = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "UPI"});
        stockLabel = new JLabel("Stock: ");

        refreshProductCombo();

        productCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateStockLabel();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(customerNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Product:"), gbc);
        gbc.gridx = 1;
        formPanel.add(productCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Available Stock:"), gbc);
        gbc.gridx = 1;
        formPanel.add(stockLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        formPanel.add(paymentCombo, gbc);

        JButton generateBillBtn = new JButton("Generate Bill & Invoice");
        generateBillBtn.setBackground(new Color(0, 128, 0));
        generateBillBtn.setForeground(Color.WHITE);
        generateBillBtn.setFont(new Font("Arial", Font.BOLD, 14));

        generateBillBtn.addActionListener(e -> generateBill());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(generateBillBtn, gbc);

        upperPanel.add(formPanel, BorderLayout.CENTER);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBorder(BorderFactory.createTitledBorder("Recent Bills"));

        String[] columns = {"Bill ID", "Customer", "Products", "Amount", "Payment", "Date & Time"};
        billHistoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable billTable = new JTable(billHistoryModel);
        billTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel historyButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewBillBtn = new JButton("View Selected Bill");
        JButton clearHistoryBtn = new JButton("Clear History");

        viewBillBtn.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow >= 0) {
                int billId = (int) billHistoryModel.getValueAt(selectedRow, 0);
                Bill selectedBill = bills.stream()
                        .filter(b -> b.getBillId() == billId)
                        .findFirst()
                        .orElse(null);
                if (selectedBill != null) {
                    showInvoice(selectedBill);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a bill to view!");
            }
        });

        clearHistoryBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear bill history?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bills.clear();
                billHistoryModel.setRowCount(0);
                updateTotalSales();
            }
        });

        historyButtonPanel.add(viewBillBtn);
        historyButtonPanel.add(clearHistoryBtn);

        totalSalesLabel = new JLabel("Total Sales: Rs. 0.00");
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        historyButtonPanel.add(Box.createHorizontalStrut(50));
        historyButtonPanel.add(totalSalesLabel);

        lowerPanel.add(historyButtonPanel, BorderLayout.NORTH);
        lowerPanel.add(new JScrollPane(billTable), BorderLayout.CENTER);

        splitPane.setTopComponent(upperPanel);
        splitPane.setBottomComponent(lowerPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void refreshProductCombo() {
        productCombo.removeAllItems();
        for (Product p : productManager.getAllProducts()) {
            if (p.getQuantity() > 0) {
                productCombo.addItem(p.getProductId() + " - " + p.getProductName() + " - Rs." + p.getPrice());
            }
        }

        if (productCombo.getItemCount() == 0) {
            productCombo.addItem("No products available");
        }

        updateStockLabel();
    }

    private void updateStockLabel() {
        if (productCombo.getItemCount() > 0 && !productCombo.getSelectedItem().toString().equals("No products available")) {
            try {
                String selectedItem = productCombo.getSelectedItem().toString();
                int productId = Integer.parseInt(selectedItem.split(" - ")[0]);
                Product product = productManager.getProductById(productId);
                if (product != null) {
                    stockLabel.setText(String.valueOf(product.getQuantity()));
                    if (product.getQuantity() <= 5) {
                        stockLabel.setForeground(Color.RED);
                    } else {
                        stockLabel.setForeground(Color.BLACK);
                    }
                }
            } catch (Exception e) {
                stockLabel.setText("N/A");
            }
        } else {
            stockLabel.setText("N/A");
        }
    }

    private void generateBill() {
        try {
            if (productCombo.getItemCount() == 0 || productCombo.getSelectedItem().toString().equals("No products available")) {
                JOptionPane.showMessageDialog(this, "No products available for billing!");
                return;
            }

            String customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter customer name!");
                return;
            }

            String selectedItem = productCombo.getSelectedItem().toString();
            int productId = Integer.parseInt(selectedItem.split(" - ")[0]);
            Product product = productManager.getProductById(productId);

            if (product == null) {
                JOptionPane.showMessageDialog(this, "Product not found!");
                return;
            }

            String quantityStr = quantityField.getText().trim();
            if (quantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter quantity!");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                return;
            }

            if (product.getQuantity() < quantity) {
                JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + product.getQuantity());
                return;
            }

            Bill bill = new Bill(nextBillId++, customerName, paymentCombo.getSelectedItem().toString());
            bill.addItem(product, quantity);
            bills.add(bill);
            seller.incrementBills();

            addBillToHistory(bill);

            updateTotalSales();

            showInvoice(bill);

            customerNameField.setText("");
            quantityField.setText("");
            refreshProductCombo();

            Component inventoryTab = tabbedPane.getComponentAt(3);
            if (inventoryTab instanceof JPanel) {
                tabbedPane.setSelectedIndex(3);
                tabbedPane.setSelectedIndex(2);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void addBillToHistory(Bill bill) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        StringBuilder productsStr = new StringBuilder();
        for (BillItem item : bill.getItems()) {
            if (productsStr.length() > 0) {
                productsStr.append(", ");
            }
            productsStr.append(item.getProductName())
                    .append(" (")
                    .append(item.getQuantity())
                    .append(")");
        }

        billHistoryModel.addRow(new Object[]{
            bill.getBillId(),
            bill.getCustomerName(),
            productsStr.toString(),
            String.format("Rs. %.2f", bill.getTotalAmount()),
            bill.getPaymentMethod(),
            sdf.format(bill.getBillDate())
        });
    }

    private void updateTotalSales() {
        double totalSales = bills.stream()
                .mapToDouble(Bill::getTotalAmount)
                .sum();
        totalSalesLabel.setText(String.format("Total Sales: Rs. %.2f", totalSales));
    }

    private void showInvoice(Bill bill) {
        JDialog dialog = new JDialog(this, "Invoice - Bill #" + bill.getBillId(), true);
        dialog.setSize(550, 650);
        dialog.setLocationRelativeTo(this);

        JTextArea invoiceArea = new JTextArea();
        invoiceArea.setEditable(false);
        invoiceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        StringBuilder invoice = new StringBuilder();
        invoice.append("==========================================\n");
        invoice.append("              SHOP INVOICE\n");
        invoice.append("==========================================\n\n");
        invoice.append("Shop Management System\n");
        invoice.append("Address: 123 Main Street\n");
        invoice.append("Phone: 1234567890\n");
        invoice.append("------------------------------------------\n\n");
        invoice.append("Bill No: ").append(bill.getBillId()).append("\n");
        invoice.append("Date: ").append(sdf.format(bill.getBillDate())).append("\n");
        invoice.append("Seller: ").append(seller.getSellerName()).append("\n");
        invoice.append("Customer: ").append(bill.getCustomerName()).append("\n");
        invoice.append("------------------------------------------\n");
        invoice.append("ITEMS:\n");
        invoice.append("------------------------------------------\n");
        invoice.append(String.format("%-20s %5s %8s %10s\n", "Product", "Qty", "Price", "Total"));
        invoice.append("------------------------------------------\n");

        for (BillItem item : bill.getItems()) {
            invoice.append(String.format("%-20s %5d %8.2f %10.2f\n",
                    item.getProductName().length() > 20
                    ? item.getProductName().substring(0, 17) + "..."
                    : item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal()));
        }

        invoice.append("------------------------------------------\n");
        invoice.append(String.format("%-34s %10.2f\n", "TOTAL AMOUNT:", bill.getTotalAmount()));
        invoice.append("------------------------------------------\n");
        invoice.append("Payment Method: ").append(bill.getPaymentMethod()).append("\n");
        invoice.append("Payment Status: PAID\n");
        invoice.append("==========================================\n");
        invoice.append("\n");
        invoice.append("        Thank you for shopping!\n");
        invoice.append("          Please visit again\n");
        invoice.append("\n");
        invoice.append("==========================================\n");
        invoice.append("This is a computer generated invoice\n");

        invoiceArea.setText(invoice.toString());

        JPanel buttonPanel = new JPanel();
        JButton printBtn = new JButton("Print");
        JButton saveBtn = new JButton("Save");
        JButton closeBtn = new JButton("Close");

        printBtn.addActionListener(e -> {
            try {
                invoiceArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error printing: " + ex.getMessage());
            }
        });

        saveBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("Invoice_" + bill.getBillId() + ".txt"));
            if (fileChooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                    writer.print(invoiceArea.getText());
                    JOptionPane.showMessageDialog(dialog, "Invoice saved successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error saving: " + ex.getMessage());
                }
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(printBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(invoiceArea), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Product ID", "Product Name", "Current Stock", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                String status = getModel().getValueAt(row, 3).toString();
                if (status.equals("Out of Stock")) {
                    comp.setBackground(new Color(255, 200, 200));
                } else if (status.equals("Low Stock")) {
                    comp.setBackground(new Color(255, 255, 200));
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh Inventory");
        JLabel summaryLabel = new JLabel();

        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            int totalProducts = 0;
            int outOfStock = 0;
            int lowStock = 0;

            for (Product product : productManager.getAllProducts()) {
                totalProducts++;
                String status;
                if (product.getQuantity() == 0) {
                    status = "Out of Stock";
                    outOfStock++;
                } else if (product.getQuantity() <= 5) {
                    status = "Low Stock";
                    lowStock++;
                } else {
                    status = "In Stock";
                }
                model.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getQuantity(),
                    status
                });
            }

            summaryLabel.setText(String.format("Total: %d | Out of Stock: %d | Low Stock: %d",
                    totalProducts, outOfStock, lowStock));
        });

        refreshBtn.doClick();

        buttonPanel.add(refreshBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(summaryLabel);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSellerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Seller ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(seller.getSellerId())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Seller Name:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(seller.getSellerName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Number of Bills:"), gbc);
        gbc.gridx = 1;
        JLabel billCountLabel = new JLabel(String.valueOf(seller.getNumberOfBills()));
        panel.add(billCountLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Verification Status:"), gbc);
        gbc.gridx = 1;
        JLabel verificationLabel = new JLabel(seller.isVerified() ? "Verified" : "Not Verified");
        verificationLabel.setForeground(seller.isVerified() ? new Color(0, 128, 0) : Color.RED);
        panel.add(verificationLabel, gbc);

        JButton verifyBtn = new JButton("Verify (Code: 1234)");
        verifyBtn.addActionListener(e -> {
            String code = JOptionPane.showInputDialog(this, "Enter verification code:");
            if (code != null) {
                try {
                    if (seller.verifyMember(Integer.parseInt(code))) {
                        verificationLabel.setText("Verified");
                        verificationLabel.setForeground(new Color(0, 128, 0));
                        JOptionPane.showMessageDialog(this, "Verification successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid verification code!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number!");
                }
            }
        });

        JButton refreshBtn = new JButton("Refresh Info");
        refreshBtn.addActionListener(e -> {
            billCountLabel.setText(String.valueOf(seller.getNumberOfBills()));
            verificationLabel.setText(seller.isVerified() ? "Verified" : "Not Verified");
            verificationLabel.setForeground(seller.isVerified() ? new Color(0, 128, 0) : Color.RED);
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(verifyBtn, gbc);
        gbc.gridx = 1;
        panel.add(refreshBtn, gbc);

        return panel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

class CustomerDashboard extends JFrame {

    private User currentUser;
    private ProductManager productManager;
    private DefaultTableModel productTableModel;
    private JLabel infoLabel;

    public CustomerDashboard(User user) {
        this.currentUser = user;
        this.productManager = ProductManager.getInstance();

        setTitle("Shop Management System - Customer View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(41, 128, 185));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (Customer)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel);

        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));

        String[] columns = {"Product ID", "Product Name", "Price (Rs.)", "Stock Status", "Expiry Date"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable productTable = new JTable(productTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                String status = getModel().getValueAt(row, 3).toString();
                if (status.equals("Out of Stock")) {
                    comp.setBackground(new Color(255, 200, 200));
                } else if (status.equals("Low Stock")) {
                    comp.setBackground(new Color(255, 255, 200));
                } else {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh Products");
        JButton allProductsBtn = new JButton("Show All");

        searchBtn.addActionListener(e -> searchProducts(searchField.getText()));
        refreshBtn.addActionListener(e -> refreshProductList());
        allProductsBtn.addActionListener(e -> {
            searchField.setText("");
            refreshProductList();
        });

        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(allProductsBtn);
        searchPanel.add(refreshBtn);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoLabel = new JLabel();
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        updateInfoLabel();
        infoPanel.add(infoLabel);

        productPanel.add(searchPanel, BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        productPanel.add(infoPanel, BorderLayout.SOUTH);

        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(productPanel, BorderLayout.CENTER);

        add(mainPanel);

        refreshProductList();
    }

    private void refreshProductList() {
        productTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<Product> products = productManager.getAllProducts();

        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No products available at the moment.\nPlease check back later.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Product product : products) {
                String stockStatus;
                if (product.getQuantity() == 0) {
                    stockStatus = "Out of Stock";
                } else if (product.getQuantity() <= 5) {
                    stockStatus = "Low Stock";
                } else {
                    stockStatus = "Available";
                }

                productTableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    String.format("%.2f", product.getPrice()),
                    stockStatus,
                    sdf.format(product.getExpiryDate())
                });
            }
        }

        updateInfoLabel();
    }

    private void searchProducts(String searchTerm) {
        if (searchTerm.trim().isEmpty()) {
            refreshProductList();
            return;
        }

        productTableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        boolean found = false;

        for (Product product : productManager.getAllProducts()) {
            if (product.getProductName().toLowerCase().contains(searchTerm.toLowerCase())
                    || String.valueOf(product.getProductId()).contains(searchTerm)) {

                String stockStatus;
                if (product.getQuantity() == 0) {
                    stockStatus = "Out of Stock";
                } else if (product.getQuantity() <= 5) {
                    stockStatus = "Low Stock";
                } else {
                    stockStatus = "Available";
                }

                productTableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    String.format("%.2f", product.getPrice()),
                    stockStatus,
                    sdf.format(product.getExpiryDate())
                });
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this,
                    "No products found matching: " + searchTerm,
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
            refreshProductList();
        }

        updateInfoLabel();
    }

    private void updateInfoLabel() {
        ArrayList<Product> products = productManager.getAllProducts();
        int totalProducts = products.size();
        int availableProducts = (int) products.stream().filter(p -> p.getQuantity() > 0).count();
        int outOfStock = (int) products.stream().filter(p -> p.getQuantity() == 0).count();

        infoLabel.setText(String.format("Total Products: %d | Available: %d | Out of Stock: %d",
                totalProducts, availableProducts, outOfStock));
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
