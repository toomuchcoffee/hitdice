package de.toomuchcoffee.hitdice.domain.monster;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.domain.combat.CombatAction;
import de.toomuchcoffee.hitdice.domain.combat.Combatant;
import de.toomuchcoffee.hitdice.domain.combat.WeaponAttack;
import de.toomuchcoffee.hitdice.domain.item.HandWeapon;

import static de.toomuchcoffee.hitdice.domain.Dice.D20;
import static de.toomuchcoffee.hitdice.domain.Dice.D6;

public class RustMonster extends Monster {
    public RustMonster() {
        super("Rust monster",
                3,
                0,
                2,
                new WeaponAttack(new CustomWeapon("tail", 1, D6, 0)),
                new CombatAction() {
                    @Override
                    public boolean condition(Combatant attacker, Combatant defender) {
                        return defender instanceof Hero && D20.check(7);
                    }

                    @Override
                    public String onSuccess(Combatant attacker, Combatant defender) {
                        Hero hero = (Hero) defender;
                        if (hero.getWeapon() != null && hero.getWeapon() instanceof HandWeapon && ((HandWeapon) hero.getWeapon()).isMetallic()) {
                            hero.setWeapon(null);
                            return "Oh no! The $%&§ rust monster hit your weapon and it crumbles to rust.";
                        } else if (hero.getArmor() != null && hero.getArmor().isMetallic()) {
                            hero.setArmor(null);
                            return "Friggin rust monster! It hit your armor and it crumbles to rust.";
                        } else {
                            return "The rust monster has destroyed all your metal, so no harm done this time!";
                        }
                    }
                });
    }
}
