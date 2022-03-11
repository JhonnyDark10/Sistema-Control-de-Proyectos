package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisDoAporteDAO extends ClaseDAO {

	//Metodos para que retorne todo 
			public List<SisDoAporte> getDonAporte(){
				List<SisDoAporte> retorno = new ArrayList<SisDoAporte>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("SisDoAporte.findAll");
				retorno = (List<SisDoAporte>)query.getResultList();
				
				return retorno;
			}	
	
}