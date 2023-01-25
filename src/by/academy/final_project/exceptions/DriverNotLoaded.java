package by.academy.final_project.exceptions;

public class DriverNotLoaded extends ClassNotFoundException{

    public DriverNotLoaded(String message) {
        super(message);
    }

}
