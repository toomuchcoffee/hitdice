package de.toomuchcoffee.hitdice.domain;

import de.toomuchcoffee.hitdice.service.CombatService.CombatAction;
import de.toomuchcoffee.hitdice.service.CombatService.CombatAction.WeaponAttack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static de.toomuchcoffee.hitdice.domain.Armor.NONE;

@Getter
@Setter
public abstract class Combatant {
    protected String name;

    protected Attribute strength;
    protected Attribute dexterity;
    protected Attribute stamina;

    protected int currentStamina;

    protected Weapon weapon;
    protected Armor armor = NONE;

    protected List<CombatAction> combatActions = newArrayList(new WeaponAttack());

    public boolean isAlive() {
        return getCurrentStamina() > 0;
    }

    public void decreaseCurrentStaminaBy(int decreasement) {
        this.currentStamina -= decreasement;
    }

}
