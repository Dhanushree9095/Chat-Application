import java.util.Scanner;

// Message class to encapsulate the details of a message
class Message {
    private String sender;
    private String receiver;
    private String content;

    // Constructor
    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return sender + " to " + receiver + ": " + content;
    }
}

// Base User class with common properties
class User {
    private String username;
    private String password;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters (Encapsulation)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // A basic method to send a message (Polymorphism: can be overridden by subclasses)
    public void sendMessage(Message message) {
        System.out.println(message.toString());
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

// Admin class inherits from User, with additional privileges
class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    // Override sendMessage method to allow deleting messages
    @Override
    public void sendMessage(Message message) {
        System.out.println("Admin " + getUsername() + " sends a message: " + message.toString());
    }

    // Admin-specific method to delete messages
    public void deleteMessage(Message message) {
        System.out.println("Admin " + getUsername() + " deleted a message: " + message.toString());
    }
}

// RegularUser class inherits from User, with limited privileges
class RegularUser extends User {
    public RegularUser(String username, String password) {
        super(username, password);
    }

    // Override sendMessage method to simply send the message
    @Override
    public void sendMessage(Message message) {
        System.out.println(getUsername() + " sends: " + message.toString());
    }
}

// Chat class to handle the communication between users
class Chat {
    private User currentUser;
    private User otherUser;
    private Scanner scanner;

    public Chat(User currentUser, User otherUser) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.scanner = new Scanner(System.in);
    }

    // Method to simulate chat between two users
    public void startChat() {
        System.out.println("Chat started with " + otherUser.getUsername() + ". Type 'exit' to end.");
        while (true) {
            System.out.print(currentUser.getUsername() + ": ");
            String messageContent = scanner.nextLine();

            if (messageContent.equalsIgnoreCase("exit")) {
                System.out.println("Ending chat...");
                break;
            }

            Message message = new Message(currentUser.getUsername(), otherUser.getUsername(), messageContent);
            currentUser.sendMessage(message);

            // Swap users for the next turn
            User temp = currentUser;
            currentUser = otherUser;
            otherUser = temp;
        }
    }
}

// Main ChatApp to simulate the conversation
public class ChatApp {
    public static void main(String[] args) {
        // Create users
        Scanner scanner = new Scanner(System.in);
       
        User admin = new Admin("admin", "admin123");
        User user1 = new RegularUser("Dhanu", "pwd 12");
        User user2 = new RegularUser("Harsh", "pwd 23");

        // Authenticate users
        System.out.print("Enter your username: ");
        String enteredUsername = scanner.nextLine();
        System.out.print("Enter your password: ");
        String enteredPassword = scanner.nextLine();

        User authenticatedUser = null;

        if (user1.authenticate(enteredUsername, enteredPassword)) {
            authenticatedUser = user1;
            System.out.println("Welcome " + user1.getUsername() + "!");
        } else if (user2.authenticate(enteredUsername, enteredPassword)) {
            authenticatedUser = user2;
            System.out.println("Welcome " + user2.getUsername() + "!");
        } else if (admin.authenticate(enteredUsername, enteredPassword)) {
            authenticatedUser = admin;
            System.out.println("Welcome Admin!");
        } else {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        // Start chat session if the user is not an admin
        if (authenticatedUser instanceof RegularUser) {
            if (authenticatedUser == user1) {
                Chat chat = new Chat(user1, user2); // User1 (Alice) chatting with User2 (Bob)
                chat.startChat();
            } else if (authenticatedUser == user2) {
                Chat chat = new Chat(user2, user1); // User2 (Bob) chatting with User1 (Alice)
                chat.startChat();
            }
        } else {
            System.out.println("Admin cannot chat in this version. Exiting...");
        }

        scanner.close();
    }
}
