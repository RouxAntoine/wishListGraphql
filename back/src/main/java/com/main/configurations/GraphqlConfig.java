package com.main.configurations;

import com.main.graphqlMappings.Mutations;
import com.main.graphqlMappings.Queries;
import graphql.GraphQL;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.annotations.processor.util.ReflectionKit;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * configuration for graphql object
 */
@Slf4j
@Configuration
public class GraphqlConfig {

    /**
     * pattern for load graphql file
     */
    private static final String SCHEMA_GRAPHQLS_PATTERN = "schema/*.graphqls";

    /**
     * récupération de chemin relatif de fichier d'une resource
     * @param resource
     * @return
     */
    private InputStream getFile(Resource resource) {
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            log.error("failed to retrieve canonical path for resource name " + resource.getFilename());
            throw new RuntimeException("failed to retrieve canonical path for resource name " + resource.getFilename());
        }
    }

    /**
     * charge une liste de fichier graphls from patterns
     * @return
     */
    private List<InputStreamReader> loadGraphFiles() {
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] filesResource = resourceResolver.getResources(SCHEMA_GRAPHQLS_PATTERN);

            return Stream.of(filesResource)
                    .map(this::getFile)
                    .map(InputStreamReader::new)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("file for pattern " + SCHEMA_GRAPHQLS_PATTERN + "not found");
            throw new RuntimeException("file for pattern " + SCHEMA_GRAPHQLS_PATTERN + "not found");
        }
    }

    /**
     * création de graphql orienté schema
     * @return
     */
    @Bean
    public GraphQLSchema createSchemaFromFiles() {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        RuntimeWiring runtime = RuntimeWiring.newRuntimeWiring()
                .build();

        // each registry is merged into the main registry
        loadGraphFiles().forEach(file -> {
            typeRegistry.merge(schemaParser.parse(file));
        });

        return schemaGenerator.makeExecutableSchema(typeRegistry, runtime);
    }

    /**
     * graphql schema
     * @return
     */
    @Bean
    public GraphQLSchema createSchemaFromAnnotations(ApplicationContext ctx) {
        ReflectionKit.setApplicationContext(ctx);

        return GraphQLSchema.newSchema()
                .query(GraphQLAnnotations.object(Queries.class))
                .mutation(GraphQLAnnotations.object(Mutations.class))
                .build();
    }

    /**
     * graphql object
     * @param schema
     * @return
     */
    @Bean
    public GraphQL createGraphQL(@Qualifier("createSchemaFromAnnotations") GraphQLSchema schema) {
        return GraphQL.newGraphQL(schema).build();
    }
}
