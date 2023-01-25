package by.academy.final_project.exceptions;

import java.io.IOException;

public class IncorrectDateFormat extends IOException {
    public IncorrectDateFormat(String message) {
        super(message);
    }
}
