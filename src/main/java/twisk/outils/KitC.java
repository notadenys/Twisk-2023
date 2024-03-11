package twisk.outils;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

    public void creerFichier(String codeC)
    {
        Path directory = Paths.get("/tmp/twisk");
        try {
            Files.createDirectories(directory);
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/client.c"));
            writer.write(codeC);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compiler()
    {
        ProcessBuilder pb = new ProcessBuilder("gcc", "-Wall", "-ansi", "-pedantic", "-fPIC", "-c", "/tmp/twisk/client.c", "-o", "/tmp/twisk/client.o");
        try {
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void construireLaBibliotheque()
    {
        ProcessBuilder pb = new ProcessBuilder("gcc", "-shared", "/tmp/twisk/programmeC.o", "/tmp/twisk/client.o", "-o", "/tmp/twisk/libTwisk.so");
        try {
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
