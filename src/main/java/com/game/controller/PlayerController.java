package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    PlayerService playerservice;

    @Autowired
    public void setService(PlayerService service) {
        this.playerservice = service;
    }

    @DeleteMapping("/rest/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id") String id) {
        Long iD = playerservice.idChecker(id);
        playerservice.delete(iD);
    }

    @GetMapping("/rest/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Player getPlayer(@PathVariable("id") String id) {
        Long iD = playerservice.idChecker(id);
        return playerservice.getPlayer(iD);
    }

    @PostMapping("/rest/players")
    @ResponseStatus(HttpStatus.OK)
    public Player createPlayer(@RequestBody Player player) {
        return playerservice.createPlayer(player);
    }

    @PostMapping("/rest/players/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Player updatePlayer(@PathVariable("id") String id, @RequestBody Player player) {
        Long iD = playerservice.idChecker(id);
        return playerservice.updatePlayer(iD, player);
    }

    @GetMapping("/rest/players")
    @ResponseStatus(HttpStatus.OK)
    public List<Player> getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return playerservice.getAllExistingPlayerList(
                        Specification.where(
                                        playerservice.nameFilter(name)
                                                .and(playerservice.titleFilter(title)))
                                .and(playerservice.raceFilter(race))
                                .and(playerservice.professionFilter(profession))
                                .and(playerservice.dateFilter(after, before))
                                .and(playerservice.bannedFilter(banned))
                                .and(playerservice.experianceFilter(minExperience, maxExperience))
                                .and(playerservice.levelFilter(minLevel, maxLevel))
                        , pageable)
                .getContent();
    }

    @GetMapping("/rest/players/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCount(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "race", required = false) Race race,
                            @RequestParam(value = "profession", required = false) Profession profession,
                            @RequestParam(value = "after", required = false) Long after,
                            @RequestParam(value = "before", required = false) Long before,
                            @RequestParam(value = "banned", required = false) Boolean banned,
                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return playerservice.getAllExistingPlayerList(
                Specification.where(playerservice.nameFilter(name)
                                .and(playerservice.titleFilter(title)))
                        .and(playerservice.raceFilter(race))
                        .and(playerservice.professionFilter(profession))
                        .and(playerservice.dateFilter(after, before))
                        .and(playerservice.bannedFilter(banned))
                        .and(playerservice.experianceFilter(minExperience, maxExperience))
                        .and(playerservice.levelFilter(minLevel, maxLevel))).size();
    }
}