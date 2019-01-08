package com.main.graphqlMappings;

import com.main.exceptions.NotModifiedException;
import com.main.exceptions.UserNotFoundException;
import com.main.exceptions.WishNotFoundException;
import com.main.models.User;
import com.main.models.Wish;
import com.main.services.UserService;
import com.main.services.WishService;
import graphql.annotations.annotationTypes.*;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * resolver de mutation
 */
@Slf4j
@Component
@GraphQLMutation
public class Mutations {

    // services
    private final UserService userService;
    private final WishService wishService;

    /**
     * default constructor
     */
    public Mutations(UserService userService, WishService wishService) {
        this.userService = userService;
        this.wishService = wishService;
    }

    /**
     * insert a new wish
     * @param env : variable environment
     * @param userId : owner of wish
     * @param name : wish name
     * @param description : wish description
     * @param price : wish price
     * @return created wish
     */
    @GraphQLField
    @GraphQLInvokeDetached
    @GraphQLDescription("insert wish for user")
    public Wish addWishForUser(
            DataFetchingEnvironment env,
            @NotNull @GraphQLName("name") String name,
            @GraphQLName("userId") String userId,
            @GraphQLName("description") String description,
            @GraphQLName("price") Double price
    ) {

        User owner = userService.getUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Wish.WishBuilder builder = Wish.builder()
                .owner(owner)
                .name(name);

        if(description != null) {
            builder.description(description);
        }

        if(price != null) {
            builder.price(price);
        }

        Wish toCreate = builder.build();
        log.info("create wish " + toCreate.getId());
        log.debug("create wish " + toCreate.toString());
        return wishService.createOrUpdateWish(toCreate);
    }

    /**
     * update an existing wish
     * @param env : variable environment
     * @param wishId : wish id to edit
     * @param ownerId : wish owner
     * @param name : wish name
     * @param description : description wish
     * @param price : price wish
     * @return updated wish
     */
    @GraphQLField
    @GraphQLInvokeDetached
    @GraphQLDescription("insert wish for user")
    public Wish updateWishForUser(
            DataFetchingEnvironment env,
            @NotNull @GraphQLName("wishId") String wishId,
            @GraphQLName("name") String name,
            @GraphQLName("ownerId") String ownerId,
            @GraphQLName("description") String description,
            @GraphQLName("price") Double price
    ) {
        boolean edited = false;

        Wish wish = wishService.getWish(wishId)
                .orElseThrow(() -> new WishNotFoundException(wishId));

        // edit wish only if name is update
        if(name != null && !name.equals(wish.getName())) {
            edited = true;

            log.debug("wish name update");
            wish.setName(name);
        }

        // edit wish only if owner is update
        if(ownerId != null && !ownerId.equals(wish.getOwner().getId())) {
            edited = true;

            log.debug("wish owner update");
            User owner = userService.getUser(ownerId)
                    .orElseThrow(() -> new UserNotFoundException(ownerId));
            wish.setOwner(owner);
        }

        // edit wish only if description is update
        if(description != null && !description.equals(wish.getDescription())) {
            edited = true;

            log.debug("wish description update");
            wish.setDescription(description);
        }
        // edit wish only if price is update
        if(price != null && !price.equals(wish.getPrice())) {
            edited = true;

            log.debug("wish price update");
            wish.setPrice(price);
        }

        // persist only if some change have been apply
        if(edited) {
            log.info("update wish " + wish.getId());
            log.debug("update wish " + wish.toString());
            return wishService.createOrUpdateWish(wish);
        }
        else {
            log.debug("wish " + wish.getId() + " not modified");
            throw new NotModifiedException(wish);
        }
    }

}
