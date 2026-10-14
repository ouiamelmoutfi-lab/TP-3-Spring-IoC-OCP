package com.exemple;

import com.exemple.config.AppConfig;
import com.exemple.config.DaoAliasConfig;
import com.exemple.config.DynamicDaoConfig;
import com.exemple.config.PrimaryConfig;
import com.exemple.dao.DaoImplProd;
import com.exemple.dao.IDao;
import com.exemple.metier.IMetier;
import com.exemple.metier.MetierMultiple;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void testDaoParDefaut() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertEquals(200.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void testPrimary() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class, PrimaryConfig.class)) {
            assertSame(context.getBean("daoPrincipal"), context.getBean(IDao.class));
            assertEquals(200.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void testAlias() {
        try (AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class, DaoAliasConfig.class)) {
            assertSame(context.getBean("daoAlias"), context.getBean(IDao.class));
            assertEquals(440.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void testProfilProd() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("prod");
            context.register(AppConfig.class);
            context.refresh();
            assertTrue(context.getBean(IDao.class) instanceof DaoImplProd);
            assertEquals(200.0, context.getBean(IMetier.class).calcul(), 0.001);
        }
    }

    @Test
    public void testSomme() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("multiple");
            context.register(AppConfig.class);
            context.refresh();
            MetierMultiple metier = context.getBean(MetierMultiple.class);
            assertEquals(new TreeSet<>(Arrays.asList("dao", "dao2", "daoFile", "daoApi")),
                    new TreeSet<>(metier.getDaoMap().keySet()));
            assertEquals(4, context.getBeansOfType(IDao.class).size());
            assertEquals(650.0, metier.calcul(), 0.001);
        }
    }

    @Test
    public void testCibleInconnue() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(
                    new MapPropertySource("test", Collections.singletonMap("dao.target", "inconnue")));
            context.register(AppConfig.class, DynamicDaoConfig.class);
            BeanCreationException exception = assertThrows(BeanCreationException.class, context::refresh);
            assertTrue(exception.getMostSpecificCause() instanceof IllegalArgumentException);
            assertTrue(exception.getMostSpecificCause().getMessage().contains("inconnue"));
        }
    }

    @Test
    public void testArgumentInconnu() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[] {"inconnue"}));
    }
}
