package by.academy.final_project.services.consumer.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.exceptions.NoSuchRole;
import by.academy.final_project.exceptions.PasswordIsNotEquals;
import by.academy.final_project.services.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface ConsumerService extends Service<Consumer> {

    boolean addConsumer(Consumer consumer) throws NoSuchAlgorithmException, InvalidKeySpecException;

    Consumer getConsumer(String login);

    Consumer getConsumer(int id);

    boolean updateConsumer(Consumer consumer, Consumer newConsumer);

    boolean updateConsumerWithoutPassword(Consumer consumer, Consumer newConsumer) ;

    boolean deleteConsumer(Consumer consumer);

    void isSuchLoginExist(String login) throws NoSuchLogin;

    ConsumersRole getRoleFromString(String role) throws NoSuchRole;

    void idSuchRole(ConsumersRole role) throws NoSuchRole;

    void isChecked (String password, String login) throws NoSuchAlgorithmException, InvalidKeySpecException, PasswordIsNotEquals;

    boolean LowDiscountValue(Consumer consumer);

    boolean UpDiscountValue(Consumer consumer);






}
