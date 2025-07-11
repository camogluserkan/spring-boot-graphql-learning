package com.serkan.GraphQL_Project.controller;

import com.serkan.GraphQL_Project.model.*;
import com.serkan.GraphQL_Project.repository.DataRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SearchController {

    private final DataRepository dataRepository;

    public SearchController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    // The main resolver that satisfy search query
    @QueryMapping
    public List<Searchable> search(@Argument String keyword) {
        System.out.println("Searching for keyword: " + keyword);
        return dataRepository.search(keyword);
    }

    // New function to satisfy getEntities query in schema.
    @QueryMapping
    public List<Entity> getEntitiesByIds(@Argument List<String> ids) {
        return dataRepository.findEntitiesByIds(ids);
    }
}