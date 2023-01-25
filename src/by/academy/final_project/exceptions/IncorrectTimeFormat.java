package by.academy.final_project.exceptions;

import java.io.IOException;

public class IncorrectTimeFormat extends IOException {
    public IncorrectTimeFormat(String message) {
        super(message);
    }
}
