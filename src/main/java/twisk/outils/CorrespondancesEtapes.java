package twisk.outils;

import twisk.monde.Etape;
import twisk.mondeIG.EtapeIG;
import java.util.HashMap;
import java.util.Map;

public class CorrespondancesEtapes {
    private final Map<EtapeIG, Etape> correspondances ;

    public CorrespondancesEtapes(){
        this.correspondances = new HashMap<>();
    }

    public void ajouter(EtapeIG etapeIG, Etape etape) {
        this.correspondances.put(etapeIG, etape);
    }

    public Etape getEtape(EtapeIG etapeIG){
        return this.correspondances.get(etapeIG);
    }
    public EtapeIG getEtapeIG(Etape etapeActuelle) {
        for (Map.Entry<EtapeIG, Etape> entry : correspondances.entrySet()) {
            if (entry.getValue() == etapeActuelle) {
                return entry.getKey();
            }
        }
        return null;
    }
}
