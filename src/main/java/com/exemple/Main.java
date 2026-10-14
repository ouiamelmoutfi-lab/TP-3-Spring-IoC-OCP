package com.exemple;

import com.exemple.config.AppConfig;
import com.exemple.config.DaoAliasConfig;
import com.exemple.config.DynamicDaoConfig;
import com.exemple.metier.IMetier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args){

        AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(AppConfig.class, DaoAliasConfig.class, DynamicDaoConfig.class);

        //AnnotationConfigApplicationContext context =
           //     new AnnotationConfigApplicationContext(AppConfig.class);

        context.getEnvironment().setActiveProfiles("prod");
        // Récupère le bean métier
        IMetier metier = context.getBean(IMetier.class);

        // Exécute le calcul
        double resultat = metier.calcul();
        System.out.println("Résultat final : " + resultat);

        // Ferme le conteneur
        context.close();
    }
}
