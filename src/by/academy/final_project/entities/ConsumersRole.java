package by.academy.final_project.entities;

public enum ConsumersRole {

    USER("Посетитель"), MANAGER("Менеджер"), ADMIN("Администратор");

    private final String name;

    ConsumersRole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
