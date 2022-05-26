package com.roman;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
    private String title;
    private String description;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return title.equals(task.title) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
