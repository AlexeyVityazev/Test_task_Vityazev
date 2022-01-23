package com.game.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; //ID игрока
    @Column(name = "name")
    private String name; //Имя персонажа (до 12 знаков включительно)
    @Column(name = "title")
    private String title; //Титул персонажа (до 30 знаков включительно)
    @Enumerated(EnumType.STRING)
    @Column(name = "race")
    private Race race; //Расса персонажа
    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession; //Профессия персонажа
    @Column(name = "experience")
    private Integer experience; //Опыт персонажа. Диапазон значений 0..10,000,000
    @Column(name = "level")
    private Integer level; //Уровень персонажа
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel; //Остаток опыта до следующего уровня
    @Column(name = "birthday")
    private Date birthday; //Дата регистрации
    //Диапазон значений года 2000..3000 включительно
    @Column(name = "banned")
    private Boolean banned;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
