package by.academy.final_project.comparators;

import by.academy.final_project.entities.Event;

import java.util.Comparator;

public class EventTimeComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getEventTime().compareTo(o2.getEventTime());
    }
}
