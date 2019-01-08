package com.main.models;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * user document
 */
@Getter
@Setter
@Builder
@GraphQLName("User")
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@CompoundIndexes({
        @CompoundIndex(name = "first_last_name", unique = true, def = "{'lastname': 1, 'firstname': 1}")
})
public class User {

    @Id
    @GraphQLField
    String id;

    @NotNull
    @GraphQLField
    @Field("firstname")
    String firstName;

    @NotNull
    @GraphQLField
    @Field("lastname")
    String lastName;

    @NotNull
    @GraphQLField
    String mail;

    @DBRef
    @GraphQLField
    @Builder.Default
    List<Wish> wishes = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
