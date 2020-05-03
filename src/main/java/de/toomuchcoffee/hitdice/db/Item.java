package de.toomuchcoffee.hitdice.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    @EqualsAndHashCode.Exclude
    private Hero hero;

    @CreatedDate
    private Date created;
}
