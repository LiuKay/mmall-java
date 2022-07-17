package com.github.kay.mmall.domain.account;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

@CacheConfig(cacheNames = "repository.account")
public interface AccountRepository extends CrudRepository<Account,Integer> {

    @Override
    Iterable<Account> findAll();

    @Cacheable(key = "#username")
    Account findByUsername(String username);

    boolean existsByUsernameOrEmailOrTelephone(String username, String email, String telephone);

    Collection<Account> findByUsernameOrEmailOrTelephone(String username, String email, String telephone);

    @Cacheable(key = "#username")
    boolean existsByUsername(String username);

    @Caching(evict = {
            @CacheEvict(key = "#s.id"),
            @CacheEvict(key = "#s.username")
    })
    @Override
    <S extends Account> S save(S s);

    @CacheEvict(allEntries = true)
    @Override
    <S extends Account> Iterable<S> saveAll(Iterable<S> iterable);

    @Cacheable(key = "#id")
    @Override
    Optional<Account> findById(Integer id);

    @Cacheable(key = "#id")
    @Override
    boolean existsById(Integer id);

    @CacheEvict(key = "#id")
    @Override
    void deleteById(Integer id);

    @CacheEvict(key = "#account.id")
    @Override
    void delete(Account account);

    @CacheEvict(allEntries = true)
    @Override
    void deleteAll(Iterable<? extends Account> iterable);

    @CacheEvict(allEntries = true)
    @Override
    void deleteAll();
}
