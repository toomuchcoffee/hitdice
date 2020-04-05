package de.toomuchcoffee.hitdice.domain.monster;


import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.Weapon;
import de.toomuchcoffee.hitdice.service.CombatService;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static java.lang.Math.max;

public class MindFlayer extends Monster {
    public MindFlayer() {
        super("Mind Flayer",
                6,
                4,
                0,
                new CombatAction() {
                    public boolean condition(Combatant attacker, Combatant defender) {
                        int attackScore = max(1, attacker.getAttack() - defender.getDefense());
                        return D20.check(attackScore);
                    }

                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        Weapon weapon = hero.getWeapon();
                        int damage = max(1, weapon.getDice().roll(weapon.getDiceNumber())
                                + weapon.getBonus()
                                + defender.getDamageBonus()
                                - defender.getArmorClass());
                        defender.reduceHealth(damage);
                        return String.format(CombatService.CAUSED_DAMAGE_MESSAGE, attacker.getName(), defender.getName(), damage);
                    }
                });
    }


}
