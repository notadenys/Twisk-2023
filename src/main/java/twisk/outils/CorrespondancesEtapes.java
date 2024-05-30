package twisk.outils;

import twisk.monde.Etape;
import twisk.mondeIG.EtapeIG;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a mapping between stages in the simulation world and stages in the graphical interface world.
 */
public class CorrespondancesEtapes {
    private final Map<EtapeIG, Etape> correspondances;

    /**
     * Constructs a new CorrespondancesEtapes object.
     */
    public CorrespondancesEtapes() {
        this.correspondances = new HashMap<>();
    }

    /**
     * Adds a mapping between an EtapeIG and an Etape.
     *
     * @param etapeIG The EtapeIG.
     * @param etape   The Etape.
     */
    public void ajouter(EtapeIG etapeIG, Etape etape) {
        this.correspondances.put(etapeIG, etape);
    }

    /**
     * Gets the Etape associated with the provided EtapeIG.
     *
     * @param etapeIG The EtapeIG.
     * @return The corresponding Etape, or null if not found.
     */
    public Etape getEtape(EtapeIG etapeIG) {
        return this.correspondances.get(etapeIG);
    }

    /**
     * Gets the EtapeIG associated with the provided Etape.
     *
     * @param etapeActuelle The Etape.
     * @return The corresponding EtapeIG, or null if not found.
     */
    public EtapeIG getEtapeIG(Etape etapeActuelle) {
        for (Map.Entry<EtapeIG, Etape> entry : correspondances.entrySet()) {
            if (entry.getValue() == etapeActuelle) {
                return entry.getKey();
            }
        }
        return null;
    }
}
