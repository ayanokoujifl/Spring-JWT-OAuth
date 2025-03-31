package com.ayanokoujifl.autenticacao.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ayanokoujifl.autenticacao.domain.Role;
import com.ayanokoujifl.autenticacao.domain.Tweet;
import com.ayanokoujifl.autenticacao.dto.TweetDto;
import com.ayanokoujifl.autenticacao.repository.TweetRepository;
import com.ayanokoujifl.autenticacao.repository.UserRepository;

@RestController
public class TweetController {

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private UserRepository userRepository;

	// Add methods to handle HTTP requests here
	@PostMapping("/tweets")
	public ResponseEntity<Tweet> createTweet(@RequestBody TweetDto dto, JwtAuthenticationToken token) {
		var user = userRepository.findById(UUID.fromString(token.getName()));
		Tweet tweet = new Tweet();
		tweet.setUser(user.orElseThrow());
		tweet.setContent(dto.content());
		tweetRepository.save(tweet);
		return ResponseEntity.ok(tweet);
	}

	@DeleteMapping("/tweets/{tweetId}")
	public ResponseEntity<Void> deleteTweet(@PathVariable Long tweetId, JwtAuthenticationToken token) {
		var user = userRepository.findById(UUID.fromString(token.getName()));
		var tweet = tweetRepository.findById(tweetId);

		boolean isAdmin = user.get().getRoles().stream()
				.anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name().toLowerCase()));

		if (isAdmin || tweet.get().getUser().getId().equals(UUID.fromString(token.getName()))) {
			tweetRepository.delete(tweet.get());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(403).build();
		}

	}

}
