package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {
    PlayerService playerService;

    @Autowired
    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/rest/players")
    public List<Player> findAll() {
        List<Player> players = playerService.findAll();
        return players;
    }

    @GetMapping("/rest/players")
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> allPlayers = playerService.findAll();
        return new ResponseEntity<>(allPlayers, HttpStatus.OK);
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
