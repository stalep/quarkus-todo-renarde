package rest;

import io.quarkiverse.renarde.Controller;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Todo;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;
import java.util.Date;
import java.util.List;

@Blocking
public class Todos extends Controller {
    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance index(List<Todo> todos, String filter);
        public static native TemplateInstance edit(Todo todo);
    }

    public TemplateInstance index(@QueryParam("filter") String filter) {
        List<Todo> todos = doFind(filter);
        return Templates.index(todos, filter);
    }

    private List<Todo> doFind(String filter) {
        System.out.println("filter = " + filter);
        if(filter == null || filter.isEmpty())
            return Todo.listAll();
        else
            return Todo.find("LOWER(task) LIKE LOWER(?1)", "%"+filter+"%").list();
    }

    @POST
    public void delete(@RestPath Long id) {
        Todo todo = Todo.findById(id);
        notFoundIfNull(todo);
        todo.delete();
        flash("message", "Task deleted");
        index(null);
    }

    @POST
    public void done(@RestPath Long id) {
        Todo todo = Todo.findById(id);
        notFoundIfNull(todo);
        todo.done = !todo.done;
        if(todo.done)
            todo.doneDate = new Date();
        flash("message", "Task updated");
        index(null);
    }

    @POST
    public void add(@NotBlank @RestForm String task,@RestForm int priority) {
        System.out.println("prio = " + priority);
        if(validationFailed()) {
            index(null);
        }
        Todo todo = new Todo();
        todo.priority = priority;
        todo.task = task;
        todo.persist();
        flash("message", "Task added");
        index(null);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/Todos/{id}/edit")
    public TemplateInstance updateForm(@PathParam("id") long id) {
        Todo loaded = Todo.findById(id);

        if (loaded == null) {
            flash("warning", "Could not find Todo with id "+id);
            index(null);
        }

        return Templates.edit(loaded);
    }

    @POST
    @Transactional
    @Path("/Todos/edit")
    public void updateTodo(@RestQuery("id") long id,
                           @RestForm @NotNull String task,
                           @RestForm Boolean done,
                           @RestForm @NotNull Integer priority) {
        System.out.println("done = " + done);

        Todo updated = Todo.findById(id);
        if(updated == null) {
            flash("warning", "Could not find Todo with id "+id);
            index(null);
        }

        updated.task = task;
        if(done == null)
            updated.done = false;
        else {
            updated.done = true;
            updated.doneDate = new Date();
        }
        updated.priority = priority;
        updated.persist();

        flash("message", "Task updated");
        index(null);
    }
}

