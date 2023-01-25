package by.academy.final_project.exceptions;

import java.io.IOException;

public class NoSuchGenre extends IOException {

    public NoSuchGenre(String message) {
        super(message);
    }
}
