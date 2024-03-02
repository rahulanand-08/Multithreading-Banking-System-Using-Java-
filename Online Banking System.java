import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

class BankAccount {
    String name;
    String username;
    String password;
    String accNumber;
    int balance = 0;
    ArrayList<String> history = new ArrayList<>();

    synchronized void updateBalance(int amount) {
        balance += amount;
    }

    void showBalance() {
        System.out.println("Balance: " + balance);
    }
}

class Transaction {
    static synchronized void withdraw(BankAccount account, int amount) {
        if (amount <= account.balance) {
            account.balance -= amount;
            account.history.add(getTimestamp() + " -" + amount + " Withdrawal");
            System.out.println("Amount Rs." + amount + "/- withdrawn successfully");
        } else {
            System.out.println("Insufficient balance to withdraw the cash");
        }
        account.showBalance();
    }

    static synchronized void deposit(BankAccount account, int amount) {
        account.updateBalance(amount);
        account.history.add(getTimestamp() + " +" + amount + " Deposit");
        System.out.println("Amount Rs." + amount + "/- deposited successfully");
        account.showBalance();
    }

    static synchronized void transfer(BankAccount sender, BankAccount receiver, int amount) {
        if (amount <= sender.balance) {
            sender.balance -= amount;
            receiver.balance += amount;
            sender.history.add(getTimestamp() + " -" + amount + " Transfer to " + receiver.name);
            receiver.history.add(getTimestamp() + " +" + amount + " Transfer from " + sender.name);
            System.out.println("Amount Rs." + amount + "/- transferred successfully");
        } else {
            System.out.println("Insufficient balance to transfer the cash");
        }
        sender.showBalance();
        receiver.showBalance();
    }

    private static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}

class Check {
    static synchronized void checkBalance(BankAccount account) {
        account.showBalance();
    }
}

class History {
    static synchronized void transactionHistory(BankAccount account) {
        System.out.println("Transaction History:");
        for (String transaction : account.history) {
            System.out.println(transaction);
        }
    }
}

public class ATM extends JFrame {
    private static final long serialVersionUID = 1L;
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<BankAccount> accounts = new ArrayList<>();

    public ATM() {
        setTitle("Banking System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton withdrawButton = new JButton("Withdraw");
        JButton depositButton = new JButton("Deposit");
        JButton transferButton = new JButton("Transfer");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton exitButton = new JButton("Exit");

        JPanel panel = new JPanel();
        panel.add(withdrawButton);
        panel.add(depositButton);
        panel.add(transferButton);
        panel.add(checkBalanceButton);
        panel.add(transactionHistoryButton);
        panel.add(exitButton);

        setContentPane(panel);
        setLocationRelativeTo(null);

        BankAccount account = new BankAccount();
        account.name = "Tina sharma";
        account.username = "tina";
        account.password = "password";
        account.accNumber = "123456";
        BankAccount account1 = new BankAccount();
        account1.name = "Rina Dey";
        account1.username = "rina";
        account1.password = "password";
        account1.accNumber = "123457";
        BankAccount account2 = new BankAccount();
        account2.name = "Mina Roy";
        account2.username = "min";
        account2.password = "password";
        account2.accNumber = "123458";
        
        accounts.add(account);
        accounts.add(account1);
        accounts.add(account2);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to withdraw:"));
                Transaction.withdraw(account, amount);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to deposit:"));
                Transaction.deposit(account, amount);
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String receiverAccNumber = JOptionPane.showInputDialog("Enter the receiver's account number:");
                BankAccount receiver = getAccount(receiverAccNumber);
                if (receiver == null) {
                    System.out.println("Receiver account not found!");
                    return;
                }
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to transfer:"));
                Transaction.transfer(account, receiver, amount);
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Check.checkBalance(account);
            }
        });

        transactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                History.transactionHistory(account);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private static BankAccount getAccount(String accNumber) {
        for (BankAccount account : accounts) {
            if (account.accNumber.equals(accNumber)) {
                return account;
            }
        }
        return null;
    }
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new ATM().setVisible(true);
        }
});
}
}
