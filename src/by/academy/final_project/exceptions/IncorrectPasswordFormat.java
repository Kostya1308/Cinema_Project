package by.academy.final_project.exceptions;

import java.io.IOException;

public class IncorrectPasswordFormat extends IOException {
    public IncorrectPasswordFormat(String message) {
        super(message);
    }
}
