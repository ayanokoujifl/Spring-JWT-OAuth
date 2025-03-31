package com.ayanokoujifl.autenticacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ayanokoujifl.autenticacao.repository.TweetRepository;

@RestController
public class TweetController {

	@Autowired
	private TweetRepository tweetRepository;
	
}
