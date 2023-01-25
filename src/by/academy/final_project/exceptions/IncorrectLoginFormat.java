package by.academy.final_project.exceptions;

import java.io.IOException;

public class IncorrectLoginFormat extends IOException {
    public IncorrectLoginFormat(String message) {
        super(message);
    }
}
