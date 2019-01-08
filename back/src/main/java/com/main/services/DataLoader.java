package com.main.services;

import com.main.models.User;
import com.main.models.Wish;
import com.main.repositories.UserRepository;
import com.main.repositories.WishRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * chargement de données par defaut dans mongodb
 */
@Slf4j
@Component
public class DataLoader {

    // constante
    private static final String ME_MAIL = "antoinroux@hotmail.fr";
    private final static String ME_FIRSTNAME="antoine";
    private final static String ME_LASTNAME="roux";
    private static final String WISH_NAME_MONTRE = "montre";
    private static final String WISH_NAME_CHOCOLATE = "chocolate";

    // inject repository
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataLoader(WishRepository wishRepository, UserRepository userRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {
        User me = initUser();
        initWishes(me);
    }

    /**
     * init one user
     * @return
     */
    private User initUser() {
        return userRepository.findByFirstNameAndLastName(ME_FIRSTNAME, ME_LASTNAME)
                .map(user -> {
                    log.info("User "+ ME_FIRSTNAME + "/" + ME_LASTNAME + " already present");
                    return user;
                })
                .orElseGet(() -> {
                    User saved = this.userRepository.save(User.builder()
                            .firstName(ME_FIRSTNAME)
                            .lastName(ME_LASTNAME)
                            .mail(ME_MAIL)
                            .build());
                    log.info("User "+ saved.getFirstName() + " successfully created");
                    return saved;
                });
    }

    /**
     * initialise set of wishes
     * @param me
     */
    private void initWishes(User me) {
        Collection<Wish> myWishes = wishRepository.findByOwner_Id(me.getId());

        List<Wish> wishes = new ArrayList<>();

        // insert wish only if it doesn't exist
        if(myWishes.stream().noneMatch(wish -> wish.getName().equals(WISH_NAME_MONTRE))) {
            wishes.add(Wish.builder()
                    .name(WISH_NAME_MONTRE)
                    .owner(me)
                    .build());
        }
        else {
            log.info("Wish "+ WISH_NAME_MONTRE + " already present");
        }

        if(myWishes.stream().noneMatch(wish -> wish.getName().equals(WISH_NAME_CHOCOLATE))) {
            wishes.add(Wish.builder()
                    .name(WISH_NAME_CHOCOLATE)
                    .owner(me)
                    .build());
        }
        else {
            log.info("Wish "+ WISH_NAME_MONTRE + " already present");
        }

        wishes.forEach(wish -> {
            Wish savedWish = this.wishRepository.save(wish);
            log.info("Wish "+ savedWish.getName() + " successfully created");
        });

        if(wishes.size() > 0) {
            // update wish on user
            me.setWishes(wishes);
            userRepository.save(me);
            log.info("update user's wishes save wishes " + wishes.stream().map(Wish::getName).collect(Collectors.toList())
                    + " for user " + me.getFirstName());
        }
    }

}
