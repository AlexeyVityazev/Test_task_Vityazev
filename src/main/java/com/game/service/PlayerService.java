package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PlayerService {
//      private final PlayerRepository playerRepository;
//    @Autowired
//    public PlayerService(PlayerRepository playerRepository) {
//        this.playerRepository = playerRepository;
//    }

    public Player findById(Long id){
      //  return playerRepository.getOne(id);
        return null;
    }
    public List<Player> findAll(){
      //  return playerRepository.findAll();
        return null;
    }
    public Player savePlayer(Player player){

       // return playerRepository.save(player);
        return null;
    }
    public void deleteById(Long id){

        //playerRepository.deleteById(id);

    }

    public void deletePlayer(Long id){

        //playerRepository.deleteById(id);
    }
    public Player upDatePlayer(Player player){
      //  Player existingPlayer = playerRepository.findById(player.getId()).orElse(player);
      //  return existingPlayer;
        return null;
    }
}
