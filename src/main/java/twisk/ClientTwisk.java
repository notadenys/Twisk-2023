package twisk;

import twisk.monde.*;
import twisk.outils.ClassLoaderPerso;
import twisk.outils.FabriqueNumero;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientTwisk {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Monde monde = new Monde();
        Activite zoo = new Activite("zoo", 4, 2);
        Guichet guichet = new Guichet("guichet",2);
        Activite toboggan = new ActiviteRestreinte("toboggan", 2, 1);
        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);
        monde.aCommeSortie(toboggan);
        simulerMonde(monde);
        System.gc();

        FabriqueNumero.getInstance().reset();
        Monde monde2 = new Monde();
        Activite zoo2 = new Activite("zoo", 4, 2);
        Guichet guichet2 = new Guichet("guichet",2);
        Activite another_activity = new Activite("another_activity", 4, 2);
        Guichet another_guichet = new Guichet("another_guichet", 2);
        Activite toboggan2 = new ActiviteRestreinte("toboggan", 2, 1);
        monde2.ajouter(guichet2, zoo2, another_activity, another_guichet, toboggan2);
        monde2.aCommeEntree(zoo2);
        zoo2.ajouterSuccesseur(guichet2);
        guichet2.ajouterSuccesseur(another_activity);
        another_activity.ajouterSuccesseur(another_guichet);
        another_guichet.ajouterSuccesseur(toboggan2);
        monde2.aCommeSortie(toboggan2);
        simulerMonde(monde2);
    }

    public static void simulerMonde(Monde monde) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassLoaderPerso classLoader = new ClassLoaderPerso(ClientTwisk.class.getClassLoader());
        Class<?> c = classLoader.loadClass("twisk.simulation.Simulation");
        Constructor<?> constr = c.getConstructor();
        Object o = constr.newInstance();
        Method setNbClients = c.getMethod("setNbClients", int.class);
        setNbClients.invoke(o, 5);
        Method simuler = c.getMethod("simuler", Monde.class);
        simuler.invoke(o, monde);
    }
}
