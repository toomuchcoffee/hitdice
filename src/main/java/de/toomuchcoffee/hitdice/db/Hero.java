package de.toomuchcoffee.hitdice.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Hero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer experience;
    private Integer level;
    private Integer strength;
    private Integer dexterity;
    private Integer stamina;
    @Column(name = "max_health")
    private Integer maxHealth;
    private Integer health;

    @OneToMany(mappedBy = "hero", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    private List<Item> items;

    @CreatedDate
    private Date created;
}
