
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import model.Penguin;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Server {
    public Server() {
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3308);
        System.out.println("Server listening on port 3308");

        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");
            (new Thread(() -> {
                handleRequest(socket);
            })).start();
        }
    }

    private static void handleRequest(Socket socket) {
        try {
            while(true) {
                SessionFactory sessionFactory = (new Configuration()).configure().buildSessionFactory();
                Session session = sessionFactory.openSession();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String request = in.readLine();
                String[] words = request.split(";");
                if (words[0].equals("1")) {
                    Transaction transaction = session.beginTransaction();
                    User user = new User();
                    user.setName(words[1]);
                    user.setSurname(words[2]);
                    user.setBudget(Integer.parseInt(words[3]));
                    session.save(user);
                    transaction.commit();
                    session.close();
                    out.println("Created new user");
                } else if (words[0].equals("0")) {
                    System.out.println("Sesja zamknieta, user zapisany");
                    System.out.println("Sesja zamknieta przez serwe poniewaz uzytkonik opuscil polaczenie");
                    socket.close();
                } else if (words[0].equals("2")) {
                    session.beginTransaction();
                    Penguin penguin = new Penguin();
                    penguin.setPenguinName(words[1]);
                    penguin.setPenguinAge(Double.parseDouble(words[2]));
                    session.save(penguin);
                    session.getTransaction().commit();
                    session.close();
                    out.println("Created new penguin");
                } else if (words[0].equals("3")) {
                    session.beginTransaction();
                    Optional<User> optionalUser = session.createQuery("Select u from User as u where u.name=:userName AND u.surname=:userSurname", User.class)
                            .setParameter("name", words[3])
                            .setParameter("surname", words[4])
                            .getResultList()
                            .stream()
                            .findFirst();
                    Optional<Penguin> optionalPenguin = session.createQuery("Select p from Penguin as p where p.penguinName= :penguinName AND p.penguinAge=:penguinAge", Penguin.class)
                            .setParameter("penguinName", words[1])
                            .setParameter("penguinAge", words[2])
                            .getResultList()
                            .stream()
                            .findFirst();
                    if(optionalPenguin.isPresent()){
                        Penguin penguin = optionalPenguin.get();
                        if(optionalUser.isPresent()){
                            User user = optionalUser.get();
                            penguin.setUser(user);
                            session.save(penguin);
                            session.save(user);
                        } else {
                            //jesli nie istnieje
                        }
                    }else {
                        //jesli nie istnieje
                    }

                    session.getTransaction().commit();
                    session.close();

                    System.out.println("penguin assigned");
                    session.close();
                } else if (words[0].equals("4")) {
                    DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
                    StringBuilder response = new StringBuilder();
                    session.createQuery("Select p from Penguin as p where p.penguinName= :penguinName AND penguinAge=:penguinAge", Penguin.class)
                            .setParameter("penguinName", words[1])
                            .setParameter("penguinAge", words[2])
                            .getResultList()
                            .forEach(penguin->response.append(penguin));

//                    transaction.commit();
                    dout.writeUTF(response.toString());
                    dout.flush();
                    session.close();
                    out.println("Wanted penguin returned");
                }
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        }
    }
}