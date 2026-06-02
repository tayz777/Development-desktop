package com.gamevault.model;

import jakarta.persistence.*;

import java.util.Set;
import jakarta.persistence.Convert;

/**
 * Entité représentant un jeu vidéo dans la collection.
 *
 * Les annotations Hibernate sont déjà en place.
 * La couche base de données sera activée dans une prochaine étape.
 */
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String developer;

    private String publisher;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Convert(converter = com.gamevault.util.PlatformListConverter.class)
    @Column(name = "platform", nullable = false, columnDefinition = "TEXT")
    private Set<Platform> platform;

    @Column(name = "personal_rating")
    private Double personalRating;

    @Column(length = 2000)
    private String description;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.TO_PLAY;

    // ── Constructeurs ─────────────────────────────────────────────────────

    public Game() {}

    public Game(String title, Set<Platform> platform) {
        this.title    = title;
        this.platform = platform;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getTitle()                  { return title; }
    public void setTitle(String title)        { this.title = title; }

    public String getDeveloper()              { return developer; }
    public void setDeveloper(String d)        { this.developer = d; }

    public String getPublisher()              { return publisher; }
    public void setPublisher(String p)        { this.publisher = p; }

    public Integer getReleaseYear()           { return releaseYear; }
    public void setReleaseYear(Integer y)     { this.releaseYear = y; }

    public Set<Platform> getPlatform()        { return platform; }
    public void setPlatform(Set<Platform> p)  { this.platform = p; }

    public Double getPersonalRating()         { return personalRating; }
    public void setPersonalRating(Double r)   { this.personalRating = r; }

    public String getDescription()            { return description; }
    public void setDescription(String d)      { this.description = d; }

    public String getCoverImagePath()         { return coverImagePath; }
    public void setCoverImagePath(String p)   { this.coverImagePath = p; }

    public GameStatus getStatus()             { return status; }
    public void setStatus(GameStatus s)       { this.status = s; }

    @Override
    public String toString() {
        return "Game{id=" + id + ", title='" + title + "', platform=" + platform + "}";
    }
}
