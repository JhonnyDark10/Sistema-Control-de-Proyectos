package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisDoDonacioneDAO extends ClaseDAO {

	//Metodos para que retorne todo 
			public List<SisDoDonacione> getDonacionesG(){
				List<SisDoDonacione> retorno = new ArrayList<SisDoDonacione>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("SisDoDonacione.findAll");
				retorno = (List<SisDoDonacione>)query.getResultList();
				
				return retorno;
			}	
	
}
