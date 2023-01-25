package by.academy.final_project.repository.consumer_repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.entities.Ticket;
import by.academy.final_project.exceptions.DriverNotLoaded;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.exceptions.NoSuchRole;
import by.academy.final_project.exceptions.PasswordIsNotEquals;
import by.academy.final_project.repository.ticket.repository.TicketRepository;
import by.academy.final_project.repository.ticket.repository.TicketRepositoryImpl;
import by.academy.final_project.services.ticket.service.TicketService;
import by.academy.final_project.services.ticket.service.TicketServiceImpl;
import by.academy.final_project.util.ConnectionManager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ConsumerRepositoryImpl implements ConsumerRepository {

    @Override
    public boolean create(Consumer consumer) {
        try {
            isSuchLoginExist(consumer);
            return false;
        } catch (NoSuchLogin e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt =
                        connection.prepareStatement("INSERT INTO consumer (login, password, role, date_of_birth, discount) VALUES (?,?,?,?,?)");
                stmt.setString(1, consumer.getLogin());
                stmt.setString(2, createHash(consumer.getPassword()));
                stmt.setInt(3, consumer.getRole().ordinal() + 1);
                stmt.setString(4, consumer.getDateOfBirth().toString());
                stmt.setInt(5, consumer.getDiscount());
                stmt.execute();
                return true;
            } catch (SQLException | DriverNotLoaded e1) {
                System.out.println(e1.getMessage());
                return false;
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
                System.out.println(e2.getMessage() + "\n");
                return false;
            }
        }
    }

    @Override
    public List<Consumer> readAll() {
        List<Consumer> consumers = new ArrayList<>();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM consumer");
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                ConsumersRole role = getRoleFromString(resultSet.getString("role"));
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                int discount = resultSet.getInt("discount");
                Consumer consumer = new Consumer(id, login, password, role, dateOfBirth, discount);
                consumers.add(consumer);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        } catch (NoSuchRole e) {
            System.out.println(e.getMessage() + "\n");
        }
        return consumers;
    }

    @Override
    public Consumer read(Consumer consumer) {
        try {
            isSuchLoginExist(consumer);
        } catch (NoSuchLogin e) {
            try (Connection connection = ConnectionManager.open()) {
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM consumer WHERE login=?");
                stmt.setString(1, consumer.getLogin());
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    ConsumersRole role = getRoleFromString(resultSet.getString("role"));
                    LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                    int discount = resultSet.getInt("discount");
                    consumer = new Consumer(id, consumer.getLogin(), password, role, dateOfBirth, discount);
                }
            } catch (SQLException | DriverNotLoaded e1) {
                System.out.println(e1.getMessage());
            } catch (NoSuchRole e2) {
                System.out.println(e2.getMessage() + "\n");
            }
            return consumer;
        }
        return consumer;
    }

    public Consumer read(String login) {
        Consumer consumer = new Consumer();
        try {
            isSuchLoginExist(login);
        } catch (NoSuchLogin e) {
            return new Consumer();
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM consumer WHERE login=?");
            stmt.setString(1, login);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                ConsumersRole role = getRoleFromString(resultSet.getString("role"));
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                int discount = resultSet.getInt("discount");
                consumer = new Consumer(id, login, password, role, dateOfBirth, discount);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        } catch (NoSuchRole e) {
            System.out.println(e.getMessage() + "\n");
        }
        return consumer;
    }

    public Consumer read(int id) {
        Consumer consumer = new Consumer();
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM consumer WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String password = resultSet.getString("password");
                String login = resultSet.getString("login");
                ConsumersRole role = getRoleFromString(resultSet.getString("role"));
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                int discount = resultSet.getInt("discount");
                consumer = new Consumer(id, login, password, role, dateOfBirth, discount);
            }
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
        } catch (NoSuchRole e) {
            System.out.println(e.getMessage() + "\n");
        }
        return consumer;
    }

    @Override
    public boolean update(Consumer consumer, Consumer newConsumer) {
        try {
            isSuchLoginExist(consumer);
        } catch (NoSuchLogin e) {
            e.printStackTrace();
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("UPDATE consumer SET login=?, password=?, role=?, date_of_birth=?, discount=?  WHERE login=?");
            stmt.setString(1, newConsumer.getLogin());
            String newPassword = null;
            try {
                newPassword = createHash(newConsumer.getPassword());
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
            stmt.setString(2, newPassword);
            stmt.setInt(3, newConsumer.getRole().ordinal() + 1);
            stmt.setString(4, newConsumer.getDateOfBirth().toString());
            stmt.setInt(5, newConsumer.getDiscount());
            stmt.setString(6, consumer.getLogin());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateWithoutPassword(Consumer consumer, Consumer newConsumer) {
        try {
            isSuchLoginExist(consumer);
        } catch (NoSuchLogin e) {
            e.printStackTrace();
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("UPDATE consumer SET login=?, role=?, date_of_birth=?, discount=?  WHERE login=?");
            stmt.setString(1, newConsumer.getLogin());
            stmt.setInt(2, newConsumer.getRole().ordinal() + 1);
            stmt.setString(3, newConsumer.getDateOfBirth().toString());
            stmt.setInt(4, newConsumer.getDiscount());
            stmt.setString(5, consumer.getLogin());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Consumer consumer) {
        try {
            isSuchLoginExist(consumer);
        } catch (NoSuchLogin e) {
            e.printStackTrace();
            return false;
        }
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement stmt =
                    connection.prepareStatement("DELETE FROM consumer WHERE login=?");
            stmt.setString(1, consumer.getLogin());
            stmt.execute();
            return true;
        } catch (SQLException | DriverNotLoaded e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void isSuchLoginExist(String login) throws NoSuchLogin {
        boolean isExist = readAll().stream()
                .map(Consumer::getLogin)
                .anyMatch(login::equals);
        if (!isExist) {
            throw new NoSuchLogin("Пользователь " + login + " не найден.");
        }
    }

    public void isSuchLoginExist(Consumer consumer) throws NoSuchLogin {
        boolean isExist = readAll().stream()
                .map(Consumer::getLogin)
                .anyMatch(consumer.getLogin()::equals);
        if (!isExist) {
            throw new NoSuchLogin("Пользователь " + consumer.getLogin() + " не найден.");
        }
    }

    @Override
    public void isSuchRole(ConsumersRole role) throws NoSuchRole {
        boolean isSuchRole = Arrays.asList(ConsumersRole.class.getEnumConstants()).contains(role);
        if (!isSuchRole) {
            throw new NoSuchRole("Уровень доступа пользователя не найден.");
        }
    }

    @Override
    public ConsumersRole getRoleFromString(String role) throws NoSuchRole {
        ConsumersRole consumersRole;
        switch (role) {
            case "Посетитель" -> consumersRole = ConsumersRole.USER;
            case "Менеджер" -> consumersRole = ConsumersRole.MANAGER;
            case "Администратор" -> consumersRole = ConsumersRole.ADMIN;
            default -> throw new NoSuchRole("Уровень доступа пользователя не найден.");
        }
        return consumersRole;
    }

    @Override
    public String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(keySpec).getEncoded();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            stringBuilder.append(b);
        }
        stringBuilder.append(Arrays.toString(salt));
        return stringBuilder.toString();
    }

    @Override
    public void isChecked(String password, String login) throws
            PasswordIsNotEquals, NoSuchAlgorithmException, InvalidKeySpecException {

        String passwordFromBase = read(login).getPassword();
        StringBuilder passwordFromBaseStrBldr = new StringBuilder(passwordFromBase);
        int i = passwordFromBaseStrBldr.indexOf("[");
        String saltInString = passwordFromBaseStrBldr.substring((i + 1), (passwordFromBaseStrBldr.length() - 1));

        String[] split = saltInString.split(", ");
        byte[] salt = new byte[split.length];
        for (int j = 0; j < split.length; j++) {
            salt[j] = Byte.parseByte(split[j]);
        }

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(keySpec).getEncoded();

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            stringBuilder.append(b);
        }
        stringBuilder.append(Arrays.toString(salt));

        if (!passwordFromBase.equals(stringBuilder.toString())) {
            throw new PasswordIsNotEquals("Пароль не верный.");
        }
    }

    @Override
    public boolean UpDiscountValue(Consumer consumer) {
        int oldDiscount = read(consumer).getDiscount();
        TicketRepository ticketRepository = new TicketRepositoryImpl();
        TicketService ticketService = new TicketServiceImpl(ticketRepository);
        List<Ticket> consumersTickets = ticketService.getConsumersTickets(consumer.getConsumerId());
        if (consumersTickets.size() == 0) {
            return false;
        }
        if (consumersTickets.size() >= 3 && oldDiscount != 10) {
            Consumer newConsumer = new Consumer(consumer.getLogin(), consumer.getRole(), consumer.getDateOfBirth(), 10);
            updateWithoutPassword(consumer, newConsumer);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean LowDiscountValue(Consumer consumer) {
        int oldDiscount = read(consumer).getDiscount();
        TicketRepository ticketRepository = new TicketRepositoryImpl();
        TicketService ticketService = new TicketServiceImpl(ticketRepository);
        List<Ticket> consumersTickets = ticketService.getConsumersTickets(consumer.getConsumerId());
        if ((long) consumersTickets.size() == 3 && oldDiscount == 10) {
            Consumer newConsumer = new Consumer(consumer.getLogin(), consumer.getRole(), consumer.getDateOfBirth(), 0);
            updateWithoutPassword(consumer, newConsumer);
            return true;
        } else {
            return false;
        }
    }
}

