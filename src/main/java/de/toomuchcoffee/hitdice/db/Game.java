package de.toomuchcoffee.hitdice.db;

import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.domain.item.Armor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.EnumType.STRING;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Game {
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

    @Enumerated(STRING)
    private Weapon weapon;

    @Enumerated(STRING)
    private Armor armor;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date modified;

}
