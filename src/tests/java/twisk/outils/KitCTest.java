package twisk.outils;

import org.junit.jupiter.api.Test;

import java.io.File;

public class KitCTest {
    @Test
    void creerEnvironnement()
    {
        KitC kitC = new KitC();
        kitC.creerEnvironnement();
        assert new File("/tmp/twisk/programmeC.o").isFile() : "Error in creerEnvironnement()";
        assert new File("/tmp/twisk/def.h").isFile() : "Error in creerEnvironnement()";
    }

    @Test
    void creerFichier()
    {
        KitC kitC = new KitC();
        kitC.creerEnvironnement();
        kitC.creerFichier("blablabla");
        assert new File("/tmp/twisk/client.c").isFile() : "Error in creerFichier()";
    }
}
