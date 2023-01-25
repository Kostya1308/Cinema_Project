package by.academy.final_project.repository.consumer_repository;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.exceptions.NoSuchRole;
import by.academy.final_project.exceptions.PasswordIsNotEquals;
import by.academy.final_project.repository.Crud;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public interface ConsumerRepository extends Crud<Consumer> {

    boolean updateWithoutPassword(Consumer consumer, Consumer newConsumer);

    void isSuchLoginExist(Consumer consumer) throws NoSuchLogin;

    void isSuchLoginExist(String login) throws NoSuchLogin;

    Consumer read(int id);

    void isSuchRole(ConsumersRole role) throws NoSuchRole;

    ConsumersRole getRoleFromString(String role) throws NoSuchRole;

    String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException;

    void isChecked(String password, String login) throws PasswordIsNotEquals, NoSuchAlgorithmException, InvalidKeySpecException;

    boolean UpDiscountValue(Consumer consumer);

    boolean LowDiscountValue (Consumer consumer);

}
