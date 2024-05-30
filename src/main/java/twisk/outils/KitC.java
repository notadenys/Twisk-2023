package twisk.outils;

import twisk.simulation.Client;
import twisk.simulation.GestionnaireClients;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for working with C code in the simulation environment.
 */
public class KitC {

    /**
     * Creates the necessary environment for compiling and running C code.
     */
    public void creerEnvironnement() {
        // Create the directory if it doesn't exist
        Path directory = Paths.get("/tmp/twisk");
        try {
            Files.createDirectories(directory);
            // Copy required files to the directory
            String[] liste = {"programmeC.o", "def.h", "codeNatif.o"};
            for (String nom : liste) {
                InputStream src = getClass().getResourceAsStream("/codeC/" + nom);
                Path dest = directory.resolve(nom);
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a C file with the provided code.
     *
     * @param codeC The C code to write to the file.
     */
    public void creerFichier(String codeC) {
        // Create the directory if it doesn't exist
        Path directory = Paths.get("/tmp/twisk");
        try {
            Files.createDirectories(directory);
            // Write the code to a file
            BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/client.c"));
            writer.write(codeC);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compiles the C code.
     */
    public void compiler() {
        // Compile the C code
        ProcessBuilder pb = new ProcessBuilder("gcc", "-Wall", "-ansi", "-pedantic", "-fPIC", "-c", "/tmp/twisk/client.c", "-o", "/tmp/twisk/client.o");
        try {
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the shared library from the compiled C code.
     */
    public void construireLaBibliotheque() {
        // Build the shared library
        ProcessBuilder pb = new ProcessBuilder("gcc", "-shared", "/tmp/twisk/programmeC.o", "/tmp/twisk/codeNatif.o", "/tmp/twisk/client.o", "-o", "/tmp/twisk/libTwisk.so");
        try {
            pb.inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kills the simulation by terminating all client processes.
     *
     * @param clients The GestionnaireClients containing the client processes.
     */
    public void killSimulation(GestionnaireClients clients) {
        try {
            // Terminate all client processes
            for (Client client : clients) {
                ProcessBuilder process = new ProcessBuilder("kill", "-9", String.valueOf(client.getNumeroClient()));
                process.inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
