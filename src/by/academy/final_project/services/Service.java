package by.academy.final_project.services;

import java.util.Iterator;
import java.util.List;

public interface Service<T> {

    default void showItemsByLines(List<T> set) {
        Iterator<T> it = set.iterator();
        if (set.size() == 0) {
            System.out.println("позиции отсутсвуют.");
        } else {
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        }
        System.out.println();
    }

}
