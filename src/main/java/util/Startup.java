package util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import model.Todo;

import java.util.Date;

public class Startup {
    @Transactional
    public void onStartup(@Observes StartupEvent start) {
        Todo todo = new Todo();
        todo.task = "Buy coffee";
        todo.done = true;
        todo.priority = 1;
        todo.doneDate = new Date();
        todo.persist();

        todo = new Todo();
        todo.task = "Drink coffee";
        todo.priority = 2;
        todo.persist();
    }
}
