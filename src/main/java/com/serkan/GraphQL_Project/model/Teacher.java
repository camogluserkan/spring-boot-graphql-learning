package com.serkan.GraphQL_Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teacher implements Searchable, Entity{
    private String id;
    private String name;

    @Override
    public String getDisplayName(){
        return this.name;
    }
}