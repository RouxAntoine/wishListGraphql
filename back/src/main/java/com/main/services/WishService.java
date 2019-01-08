package com.main.services;

import com.main.models.Wish;
import com.main.repositories.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * service for wish resource
 */
@Service
@Transactional(readOnly = true)
public class WishService {

    // repositories
    private final WishRepository wishRepository;

    @Autowired
    WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    /**
     * create a new Wish
     * @return new created wish
     */
    @Transactional
    public Wish createOrUpdateWish(Wish wishToPersist) {
        return this.wishRepository.save(wishToPersist);
    }

    /**
     * retrieve all wish
     * @return list of wishes
     */
    public List<Wish> getWishes() {
        return this.wishRepository.findAll();
    }

    /**
     * retrieve a wish by id
     * @param wishId : wish's id to retrieve
     * @return : searched wish
     */
    public Optional<Wish> getWish(String wishId) {
        return wishRepository.findById(wishId);
    }
}
