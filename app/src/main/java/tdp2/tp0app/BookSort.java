package tdp2.tp0app;

import java.util.Comparator;

public class BookSort implements Comparator<Book> {
    public int compare(Book a, Book b) {
        return a.name.compareTo(b.name);
    }
}
