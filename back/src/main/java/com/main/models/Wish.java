package com.main.models;

import com.mongodb.lang.Nullable;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * wish document
 */
@Getter
@Setter
@Builder
@GraphQLName("Wish")
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wish")
public class Wish {

    @Id
    @GraphQLField
    String id;

    @Indexed(unique = true)
    @NotNull
    @GraphQLField
    String name;

    @Nullable
    @GraphQLField
    String description;

    @Nullable
    @GraphQLField
    Double price;

    @DBRef
    @NotNull
    @GraphQLField
    User owner;

    @Override
    public String toString() {
        return "Wish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
