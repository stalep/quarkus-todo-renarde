package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.ws.rs.FormParam;

import java.util.Date;

@Entity
public class Todo extends PanacheEntity {
    @FormParam("task")
    public String task;
    @FormParam("done")
    public boolean done;
    @FormParam("donedate")
    public Date doneDate;
    @FormParam("priority")
    public Integer priority;
}
