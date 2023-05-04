package com.example.chatapp.ui.main.contacts;

import java.util.Arrays;
import java.util.List;

public class ContactGenerator {

    private static final ContactCard[] CARDS;
    private static final String[] testNames = {"Deanna Smith","Heidi Carney","Sean Thomas","Kyle Travis","Bethany Gilbert","Linda Robles",
            "Courtney Jones","Jennifer Rojas","Brian Fisher","Rebecca Pineda","Robert Jones","Lisa Nguyen DDS","Tiffany Lambert",
            "Gabriel Thompson","Alexandria Moses","Chad Greene","Jamie Morrison","Julie Werner","Cameron Pierce","Brittany Williamson"};

    static {
        CARDS = new ContactCard[testNames.length];

        for (int i = 0; i < CARDS.length; i++) {
            CARDS[i] = new ContactCard.Builder(testNames[i]).addEmail(testNames[i].split(" ")[1] + "@gmail.com").addNick(testNames[i].split(" ")[0]).build();

        }
    }

    public static List<ContactCard> getCardList() {return Arrays.asList(CARDS);}

    public static ContactCard[] getCARDS() {return Arrays.copyOf(CARDS, CARDS.length);}

    private ContactGenerator() {}


}
