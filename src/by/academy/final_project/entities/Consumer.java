package by.academy.final_project.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Consumer {

    private int consumerId;
    private String login;
    private String password;
    private ConsumersRole role;
    LocalDate dateOfBirth;
    int discount;

    public Consumer() {
    }

    public Consumer(String login, String password, ConsumersRole role, LocalDate dateOfBirth, int discount) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.discount = discount;
    }

    public Consumer(String login, ConsumersRole role, LocalDate dateOfBirth, int discount) {
        this.login = login;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.discount = discount;
    }

    public Consumer(int consumer_Id, String login, String password, ConsumersRole role, LocalDate dateOfBirth, int discount) {
        this.consumerId = consumer_Id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.discount = discount;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ConsumersRole getRole() {
        return role;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getDiscount() {
        return discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consumer consumer = (Consumer) o;
        return Objects.equals(login, consumer.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "Пользователь " + login + ", уровень доступа - " + role + ", дата рождения - " + dateOfBirth + ", скидка - " + discount + "%.";
    }
}
