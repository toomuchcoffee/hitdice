package de.toomuchcoffee.hitdice.db;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private ItemType itemType;

    private boolean metallic;
    @Column(name = "display_name")
    private String displayName;
    private int ordinal;

    @Type(type = "jsonb")
    @Column(name = "properties", columnDefinition = "jsonb")
    private Map<String, Object> properties;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    @EqualsAndHashCode.Exclude
    private Hero hero;

    @CreatedDate
    private Date created;
}
