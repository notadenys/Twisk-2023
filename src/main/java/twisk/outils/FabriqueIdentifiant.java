package twisk.outils;

public class FabriqueIdentifiant {

        private int cptEtape;

        private static FabriqueIdentifiant instance;

        private FabriqueIdentifiant() {
            this.cptEtape = 0;
        }

        /**
         * @return instance of FabriqueNumero
         */
        public static FabriqueIdentifiant getInstance() {
            if (instance == null) {
                instance = new FabriqueIdentifiant();
            }
            return instance;
        }

        /**
         * @return number of stage
         */
        public int getNumeroEtape() {
            return instance.cptEtape++;
        }

        /**
         * reset counter to 0 if we have more than one world
         */
        public void reset() {
            instance.cptEtape = 0;
        }
}
