package by.academy.final_project.exceptions;

import java.io.IOException;

public class NoSuchRole extends IOException {
    public NoSuchRole(String message) {
        super(message);
    }
}
