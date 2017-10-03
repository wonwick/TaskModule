package com.won.taskmodule;

/**
 * Created by Oshan Wickramaratne on 2017-09-06.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Task {
    private String name;
    private String area;
    private String id;

    public Task() {


    }

    public Task(String name, String area, String id) {
        this.name = name;
        this.area = area;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getId() {
        return id;
    }
}
