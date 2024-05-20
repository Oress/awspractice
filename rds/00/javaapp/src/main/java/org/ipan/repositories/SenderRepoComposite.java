package org.ipan.repositories;

import java.util.Optional;

import org.ipan.model.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SenderRepoComposite {
    private final SenderDataRepo senderRepo;
    private final RedisOperations redisOps;

    public SenderRepoComposite(SenderDataRepo senderRepo, @Qualifier("redisTemplate") RedisOperations redisOps) {
        this.senderRepo = senderRepo;
        this.redisOps = redisOps;
    }

    public Person save(Person p) {
        // let it be simple y/n cache
        redisOps.opsForValue().set(p.getEmail(), "exists");
        return senderRepo.save(p);
    }

    public Optional<Person> findByEmail(String email) {
        // search in cache first
        // if not found => search in db and put in cache.

        Optional<Person> result = senderRepo.findByEmail(email);
        result.ifPresent(p -> {
            // put into redis
        });

        return result;
    }
}
