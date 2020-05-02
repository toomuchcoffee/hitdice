package de.toomuchcoffee.hitdice.controller.dto;

import de.toomuchcoffee.hitdice.domain.equipment.Item;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "forId")
@EqualsAndHashCode
public class ModalData {
    private final String id;
    private String title;
    private String body;
    private List<ModalButton> buttons;

    private ModalData(String id, String title, String body, ModalButton... buttons) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.buttons = Arrays.asList(buttons);
    }

    public static ModalData treasureModal(Item item) {
        return new ModalData(
                "treasure",
                "Treasure!",
                String.format("You found a %s!", item.getDisplayName()),
                ModalButton.of("Take it!", "/dungeon/collect"),
                ModalButton.of("Trash it!", "/dungeon/clear"),
                ModalButton.of("Leave it!", "/dungeon"));
    }

    public static ModalData magicDoorModal() {
        return new ModalData(
                "magicDoor",
                "Magic Door!",
                "Enter the door to get to the next dungeon level!",
                ModalButton.of("Enter", "/dungeon/enter"),
                ModalButton.of("Not yet...", "/dungeon"));
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class ModalButton {
        private final String label;
        private final String url;
    }
}
