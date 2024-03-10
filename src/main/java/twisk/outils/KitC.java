package twisk.outils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class KitC {
    public void creerEnvironnement() {
        Path directory = Paths.get("/tmp/twisk");
        try {
            Files.createDirectories(directory);
            String[] liste = {"programmeC.o", "def.h"};
            for (String nom : liste) {
                InputStream src = getClass().getResourceAsStream("/codeC/" + nom);
                Path dest = directory.resolve(nom);
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
