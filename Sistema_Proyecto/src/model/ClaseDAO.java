package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ClaseDAO {
	// Crea una sola instancia de EntityManagerFactory para toda la applicacion.
		private static final String PERSISTENCE_UNIT_NAME = "Sistema_Proyecto";
		private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		
		// Objeto Entity Manager para cada instancia de un objeto que 
		// herede de esta clase.
		private EntityManager em;
		
		/**
		 * Retorna el Entity Mananager, si no existe lo crea.
		 * @return
		 */
		public EntityManager getEntityManager() {
			if (em == null){
		        em = emf.createEntityManager();
		    }
		    return em; 
		}
		
		//realizar la conexion a la base de datos
		
		
		public Connection conectar() {
			
			EntityManager entityManager; 
			entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
		    Connection connection = null;
		    entityManager.getTransaction().begin();
		    connection = entityManager.unwrap(Connection.class);
		    System.out.println("Conexion obtenida : " + connection.toString());
		    return connection;
		}
		
		
		
		
}
