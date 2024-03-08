package twisk.monde;

import twisk.monde.*;
import org.junit.jupiter.api.Test;

class ActiviteTest {

    @Test
    void estUneActivite() {
        Activite a = new Activite("1");
        boolean b = a.estUneActivite();
        assert(b):"Error in estUneActivite()";

        ActiviteRestreinte c = new ActiviteRestreinte("Act 1");
        boolean d = c.estUneActivite();
        assert(d):"Error in estUneActivite()";

        SasEntree e = new SasEntree();
        boolean f = e.estUneActivite();
        assert(f):"Error in estUneActivite()";

        SasSortie g = new SasSortie();
        boolean h = g.estUneActivite();
        assert(h):"Error in estUneActivite()";
    }
}