import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String password;
    private double balance;

    public BankAccount(String accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 1500.0; // Initial balance
    }

    public BankAccount(String accountNumber, String password, double balance) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    @Override
    public String toString() {
        return accountNumber + "," + password + "," + balance;
    }
}

class AccountCreation {
    public BankAccount createAccount(Scanner scanner, HashMap<String, BankAccount> accounts) {
        System.out.println("\n--- Create a New Account ---");
        System.out.print("Enter a new account number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account number already exists. Please try again.");
            return null;
        }

        System.out.print("Set your password: ");
        String password = scanner.nextLine();

        BankAccount account = new BankAccount(accountNumber, password);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully with an initial balance of Rs. 1500!");
        return account;
    }
}

class Withdraw {
    public void withdrawAmount(BankAccount account, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
        } else if (amount > account.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            account.setBalance(account.getBalance() - amount);
            System.out.println("Withdrawal successful.");
            System.out.println("Remaining balance: Rs. " + account.getBalance());
        }
    }
}

class Deposit {
    public void depositAmount(BankAccount account, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
        } else {
            account.setBalance(account.getBalance() + amount);
            System.out.println("Deposit successful.");
            System.out.println("Current balance: Rs. " + account.getBalance());
        }
    }
}

class BalanceCheck {
    public void checkBalance(Scanner scanner, HashMap<String, BankAccount> accounts) {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        if (!accounts.containsKey(accountNumber)) {
            System.out.println("Account not found.");
            return;
        }

        BankAccount account = accounts.get(accountNumber);

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (account.validatePassword(password)) {
            System.out.println("Current balance: Rs. " + account.getBalance());
        } else {
            System.out.println("Invalid password.");
        }
    }
}

class ChangePassword {
    public void updatePassword(BankAccount account, String oldPassword, String newPassword) {
        if (account.validatePassword(oldPassword)) {
            account.setPassword(newPassword);
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Incorrect old password.");
        }
    }
}

class FileHandler {
    private static final String FILE_NAME = "BankingSystem/accounts.txt"; 
    public static void saveAccountsToFile(HashMap<String, BankAccount> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("AccountNumber,Password,Balance\n"); // Column headers
            for (BankAccount account : accounts.values()) {
                writer.write(account.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts to file: " + e.getMessage());
        }
    }

    public static HashMap<String, BankAccount> loadAccountsFromFile() {
        HashMap<String, BankAccount> accounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine(); // Skip column headers
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String accountNumber = parts[0];
                String password = parts[1];
                double balance = Double.parseDouble(parts[2]);
                accounts.put(accountNumber, new BankAccount(accountNumber, password, balance));
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts from file: " + e.getMessage());
        }
        return accounts;
    }
}

public class BMS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, BankAccount> accounts = FileHandler.loadAccountsFromFile();

        AccountCreation accountCreation = new AccountCreation();
        Withdraw withdraw = new Withdraw();
        Deposit deposit = new Deposit();
        BalanceCheck balanceCheck = new BalanceCheck();
        ChangePassword changePassword = new ChangePassword();

        int choice;
        do {
            System.out.println("\n=============================");
            System.out.println("       WELCOME TO SK BANK       ");
            System.out.println("             MENU               ");            
            System.out.println("=============================");
            System.out.println("1.  Create Account");
            System.out.println("2.  Deposit");
            System.out.println("3.  Withdraw");
            System.out.println("4.  Check Balance");
            System.out.println("5.  Change Password");
            System.out.println("6.  Exit");
            System.out.println("=============================");

            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    accountCreation.createAccount(scanner, accounts);
                    break;

                case 2:
                    System.out.print("Enter account number: ");
                    String depositAccountNumber = scanner.nextLine();

                    if (!accounts.containsKey(depositAccountNumber)) {
                        System.out.println("Account not found.");
                        break;
                    }

                    BankAccount depositAccount = accounts.get(depositAccountNumber);
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = scanner.nextDouble();
                    deposit.depositAmount(depositAccount, depositAmount);
                    break;

                case 3:
                    System.out.print("Enter account number: ");
                    String withdrawAccountNumber = scanner.nextLine();

                    if (!accounts.containsKey(withdrawAccountNumber)) {
                        System.out.println("Account not found.");
                        break;
                    }

                    BankAccount withdrawAccount = accounts.get(withdrawAccountNumber);
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawAmount = scanner.nextDouble();
                    withdraw.withdrawAmount(withdrawAccount, withdrawAmount);
                    break;

                case 4:
                    balanceCheck.checkBalance(scanner, accounts);
                    break;

                case 5:
                    System.out.print("Enter account number: ");
                    String changePasswordAccountNumber = scanner.nextLine();

                    if (!accounts.containsKey(changePasswordAccountNumber)) {
                        System.out.println("Account not found.");
                        break;
                    }

                    BankAccount changePasswordAccount = accounts.get(changePasswordAccountNumber);
                    System.out.print("Enter old password: ");
                    String oldPassword = scanner.nextLine();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();
                    changePassword.updatePassword(changePasswordAccount, oldPassword, newPassword);
                    break;

                case 6:
                    FileHandler.saveAccountsToFile(accounts);
                    System.out.println("Thank you for using the Bank Management System. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);

        scanner.close();
    }
}
