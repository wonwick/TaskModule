package com.won.taskmodule;

/**
 * Created by Oshan Wickramaratne on 2017-09-06.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DetailedTask {

    private String contactName = "asd";
    private String contactNumber = "asd";
    private String deadline = "asd";
    private String description = "asd";


    public DetailedTask() {


    }

    public DetailedTask(String contactName, String contactNumber, String deadline, String description) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.deadline = deadline;
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }
}

