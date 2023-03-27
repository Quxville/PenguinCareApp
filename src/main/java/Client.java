import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public Client() {
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3308);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("PenguinCare");

        while(true) {
            while(true) {
                System.out.println("Menu:");
                System.out.println("1 - Add user");
                System.out.println("2 - Add penguin");
                System.out.println("3 - Adopt penguin");
                System.out.println("4 - Find penguin");
                System.out.println("0 - Exit program");
                int choice = scanner.nextInt();
                if (choice == 1) {
                    System.out.println("User:");
                    System.out.println("Input first name: ");
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    System.out.println("Input last name: ");
                    String surname = scanner.nextLine();
                    System.out.println("Input budget: ");
                    int budget = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("1;" + name + ";" + surname + ";" + budget);
                    out.println("1;" + name + ";" + surname + ";" + budget);
                } else if (choice == 2) {
                    System.out.println("Penguin: ");
                    System.out.println("Input penguin name: ");
                    scanner.nextLine();
                    String penguinName = scanner.nextLine();
                    System.out.println("Input penguin age:");
                    double penguinAge = scanner.nextDouble();
                    System.out.println("2;" + penguinName + ";" + penguinAge);
                    out.println("2;" + penguinName + ";" + penguinAge);
                } else if (choice == 3) {
                    scanner.nextLine();
                    System.out.println("Assigning penguin to user: ");
                    System.out.println("Input penguin name: ");
//                    scanner.nextLine();
                    String penguinName = scanner.nextLine();
                    System.out.println("Input penguin age:");
                    double penguinAge = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Input user name: ");
                    String userName = scanner.nextLine();
                    System.out.println("Input user surname: ");
                    String userSurname = scanner.nextLine();
                    System.out.println("3;" + penguinName + ";" + penguinAge + ";" + userName + ";" + userSurname);
                    out.println("3;" + penguinName + ";" + penguinAge + ";" + userName + ";" + userSurname);
                } else if (choice == 4){
                    scanner.nextLine();
                    System.out.println("Find penguin");
                    System.out.println("Input penguin name:");
                    String penguinName = scanner.nextLine();
                    System.out.println("Input penguin age: ");
                    double penguinAge = scanner.nextDouble();
                    out.println("4;" + penguinName + ";" + penguinAge);
                    DataInputStream din = new DataInputStream(socket.getInputStream());
                    String responseData = din.readUTF();
                    System.out.println("Server response:\n" + responseData);
                    String response = in.readLine();
                } else if (choice == 0) {
                    System.out.println("Closing app");
                    out.println("user left connection");
                    socket.close();
                    return;
                }
            }
        }
    }
}
