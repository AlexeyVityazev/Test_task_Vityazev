package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadRequestException;
import com.game.exception.PlayerNotFoundException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    public void save(Player customer) {
        playerRepository.save(customer);
    }

    public List<Player> listAll() {
        return playerRepository.findAll();
    }

    public Player get(Long id) {
        return playerRepository.findById(id).get();
    }

    public void delete(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
        } else {
            throw new PlayerNotFoundException("Player is not found");
        }
    }

    public Long idChecker(String id) {
        if (id == null || id.equals("0") || id.equals("")) {
            throw new BadRequestException("ID is incorrect");
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID is not a number", e);
        }
    }

    public Player getPlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFoundException("Player is not found");
        }
        return playerRepository.findById(id).get();
    }

    public Player createPlayer(Player playerRequired) {
        if (playerRequired.getName() == null
                || playerRequired.getTitle() == null
                || playerRequired.getRace() == null
                || playerRequired.getProfession() == null
                || playerRequired.getBirthday() == null
                || playerRequired.getExperience() == null) {
            throw new BadRequestException("Please fill in all required fields");
        }
        paramsChecker(playerRequired);
        if (playerRequired.getBanned() == null) {
            playerRequired.setBanned(false);
        }
        playerRequired.setLevel(calculateLevel(playerRequired.getExperience()));
        playerRequired.setUntilNextLevel(calculateUntilNextLevel(playerRequired.getLevel(), playerRequired.getExperience()));
        return playerRepository.saveAndFlush(playerRequired);
    }

    private void paramsChecker(Player playerRequired) {
        paramsRangeChecker(playerRequired);
        if (playerRequired.getProfession() == null) {
            throw new BadRequestException("The player profession is out of range");
        }
        if (playerRequired.getRace() == null) {
            throw new BadRequestException("The player Race is out of range");
        }
    }

    private void paramsRangeChecker(Player playerRequired) {
        if (playerRequired.getName() != null && (playerRequired.getName().length() < 1 || playerRequired.getName().length() > 12)) {
            throw new BadRequestException("The player name is too long or absent");
        }

        if (playerRequired.getTitle() != null && playerRequired.getTitle().length() > 30) {
            throw new BadRequestException("The title is too long or absent");
        }

        if (playerRequired.getExperience() != null && (playerRequired.getExperience() < 0 || playerRequired.getExperience() > 10_000_000)) {
            throw new BadRequestException("The player experience is out of range");
        }
        if (playerRequired.getBirthday() != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(playerRequired.getBirthday());
            if (date.get(Calendar.YEAR) < 2000 || date.get(Calendar.YEAR) > 3000) {
                throw new BadRequestException("The Player birthday is out of range");
            }
        }
    }

    public Player updatePlayer(Long id, Player playerRequired) {
        if (!playerRepository.existsById(id)) {
            throw new PlayerNotFoundException("Player is not found");
        }
        paramsRangeChecker(playerRequired);
        Player changedPlayer = playerRepository.findById(id).get();

        if (playerRequired.getName() != null)
            changedPlayer.setName(playerRequired.getName());

        if (playerRequired.getTitle() != null)
            changedPlayer.setTitle(playerRequired.getTitle());

        if (playerRequired.getRace() != null)
            changedPlayer.setRace(playerRequired.getRace());

        if (playerRequired.getProfession() != null)
            changedPlayer.setProfession(playerRequired.getProfession());

        if (playerRequired.getBirthday() != null) {
            changedPlayer.setBirthday(playerRequired.getBirthday());
        }
        if (playerRequired.getExperience() != null) {
            changedPlayer.setExperience(playerRequired.getExperience());
            int level = calculateLevel(playerRequired.getExperience());
            changedPlayer.setLevel(level);

            changedPlayer.setUntilNextLevel(calculateUntilNextLevel(level, playerRequired.getExperience()));
        }
        if (playerRequired.getBanned() != null) {
            changedPlayer.setBanned(playerRequired.getBanned());
        }
        return playerRepository.save(changedPlayer);
    }

    private int calculateUntilNextLevel(int level, int experience) {
        return 50 * (level + 1) * (level + 2) - experience;
    }

    private int calculateLevel(int experience) {
        return (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    public List<Player> getAllExistingPlayerList(Specification<Player> specification) {
        return playerRepository.findAll(specification);
    }

    public Page<Player> getAllExistingPlayerList(Specification<Player> specification, Pageable sortedByName) {
        return playerRepository.findAll(specification, sortedByName);
    }

    public Specification<Player> nameFilter(String name) {
        return (root, query, criteriaBuilder) -> name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public Specification<Player> titleFilter(String title) {
        return (root, query, criteriaBuilder) -> title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public Specification<Player> raceFilter(Race race) {
        return (root, query, criteriaBuilder) -> race == null ? null : criteriaBuilder.equal(root.get("race"), race);
    }

    public Specification<Player> professionFilter(Profession profession) {
        return (root, query, criteriaBuilder) -> profession == null ? null : criteriaBuilder.equal(root.get("profession"), profession);
    }

    public Specification<Player> dateFilter(Long after, Long before) {
        return (root, query, criteriaBuilder) -> {
            if (after == null && before == null) {
                return null;
            }
            if (after == null) {
                Date before1 = new Date(before);
                return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), before1);
            }
            if (before == null) {
                Date after1 = new Date(after);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), after1);
            }
            Date before1 = new Date(before);
            Date after1 = new Date(after);
            return criteriaBuilder.between(root.get("birthday"), after1, before1);
        };
    }

    public Specification<Player> bannedFilter(Boolean banned) {
        return (root, query, criteriaBuilder) -> {
            if (banned == null) {
                return null;
            }
            if (banned) {
                return criteriaBuilder.isTrue(root.get("banned"));
            } else {
                return criteriaBuilder.isFalse(root.get("banned"));
            }
        };
    }

    public Specification<Player> experianceFilter(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("experience"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), min);
            }
            return criteriaBuilder.between(root.get("experience"), min, max);
        };
    }

    public Specification<Player> levelFilter(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("level"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("level"), min);
            }
            return criteriaBuilder.between(root.get("level"), min, max);
        };
    }
}

