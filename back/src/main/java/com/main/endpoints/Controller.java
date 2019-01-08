package com.main.endpoints;

import com.main.webObject.QueryDTO;
import graphql.ExceptionWhileDataFetching;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * graphql default controller
 */
@Slf4j
@RestController
public class Controller {

    /**
     * graphql object
     */
    private final GraphQL graphql;

    /**
     * default constructor
     */
    Controller(GraphQL graphQL) {
        this.graphql = graphQL;
    }

    /**
     * graphql endpoint
     * @param request : request object
     */
    @PostMapping(value = "/api/endpoint")
    public Map endPoint(@RequestBody QueryDTO request) throws Throwable {

        ExecutionResult res = graphql.execute(request.getQuery());

        if(res.getErrors().isEmpty()) {
            return res.getData();
        }
        else {
            Map<String, String> errorMap = new HashMap<>();
            for(GraphQLError er : res.getErrors()) {

                if(er instanceof ExceptionWhileDataFetching) {
                    throw ((ExceptionWhileDataFetching) er).getException().getCause().getCause();
                }
                else {
                    log.error(er.getMessage());
                    errorMap.put(er.getErrorType().name(), er.getMessage());
                }
            }
            return errorMap;
        }

    }

}
