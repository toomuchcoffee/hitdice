package de.toomuchcoffee.hitdice.db;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class)
})
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

    @Type(type = "string-array")
    @Column(columnDefinition = "text[]")
    private String[] items;

    @CreatedDate
    private Date created;
}
