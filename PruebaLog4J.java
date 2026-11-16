import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PruebaLog4J {

    private static final Logger logger =
            LogManager.getLogger(PruebaLog4J.class);

    public static void main(String[] args) {

        logger.info("Prueba de log4j: el sistema bancario ha iniciado.");
        logger.warn("Este es un mensaje de advertencia.");
        logger.error("Este es un mensaje de error de prueba.");

        System.out.println("Prueba de Log4j finalizada.");
    }
}

