package com.main.graphqlMappings;


import com.main.models.User;
import com.main.models.Wish;
import com.main.services.UserService;
import com.main.services.WishService;
import graphql.annotations.annotationTypes.GraphQLDescription;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * resolver de query
 */
@Slf4j
@Component
@GraphQLName("query")
public class Queries {

    // dependencies
    private final UserService userService;
    private final WishService wishService;

    /**
     * default constructor
     */
    public Queries(UserService userService, WishService wishService) {
        this.userService = userService;
        this.wishService = wishService;
    }

    @GraphQLField
    @GraphQLInvokeDetached
    @GraphQLDescription("retrieve user list")
    public List<User> users(DataFetchingEnvironment e) {
        log.info("GET users list");
        return this.userService.getUsers();
    }

    @GraphQLField
    @GraphQLInvokeDetached
    @GraphQLDescription("retrieve wish list")
    public List<Wish> wishes(DataFetchingEnvironment e) {
        log.info("GET wishes list");
        return this.wishService.getWishes();
    }
}