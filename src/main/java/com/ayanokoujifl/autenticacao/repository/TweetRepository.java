package com.ayanokoujifl.autenticacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayanokoujifl.autenticacao.domain.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

}
