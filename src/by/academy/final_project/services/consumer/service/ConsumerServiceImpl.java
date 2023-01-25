package by.academy.final_project.services.consumer.service;

import by.academy.final_project.entities.Consumer;
import by.academy.final_project.entities.ConsumersRole;
import by.academy.final_project.exceptions.NoSuchLogin;
import by.academy.final_project.exceptions.NoSuchRole;
import by.academy.final_project.exceptions.PasswordIsNotEquals;
import by.academy.final_project.repository.consumer_repository.ConsumerRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ConsumerServiceImpl implements ConsumerService {

    ConsumerRepository consumerRepository;

    public ConsumerServiceImpl(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public boolean addConsumer(Consumer consumer) {
        return consumerRepository.create(consumer);
    }

    @Override
    public Consumer getConsumer(String login) {
        return consumerRepository.read(login);
    }

    @Override
    public Consumer getConsumer(int id) {
        return consumerRepository.read(id);
    }


    @Override
    public boolean updateConsumer(Consumer consumer, Consumer newConsumer) {
        return consumerRepository.update(consumer, newConsumer);
    }

    @Override
    public boolean updateConsumerWithoutPassword(Consumer consumer, Consumer newConsumer) {
        return consumerRepository.updateWithoutPassword(consumer, newConsumer);
    }

    @Override
    public boolean deleteConsumer(Consumer consumer) {
        return consumerRepository.delete(consumer);
    }

    @Override
    public void isSuchLoginExist(String login) throws NoSuchLogin {
        consumerRepository.isSuchLoginExist(login);
    }


    @Override
    public ConsumersRole getRoleFromString(String role) throws NoSuchRole {
        return consumerRepository.getRoleFromString(role);
    }

    @Override
    public void idSuchRole(ConsumersRole role) throws NoSuchRole {
        consumerRepository.isSuchRole(role);
    }


    @Override
    public void isChecked(String password, String login) throws NoSuchAlgorithmException, InvalidKeySpecException, PasswordIsNotEquals {
        consumerRepository.isChecked(password, login);
    }

    @Override
    public boolean LowDiscountValue(Consumer consumer) {
        return consumerRepository.LowDiscountValue(consumer);
    }

    @Override
    public boolean UpDiscountValue(Consumer consumer) {
        return consumerRepository.UpDiscountValue(consumer);
    }
}
