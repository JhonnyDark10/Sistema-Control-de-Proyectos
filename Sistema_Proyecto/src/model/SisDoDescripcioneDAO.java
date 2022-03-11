package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisDoDescripcioneDAO extends ClaseDAO {

	//Metodos para que retorne todo 
			public List<SisDoDescripcione> getDonDescripcion(){
				List<SisDoDescripcione> retorno = new ArrayList<SisDoDescripcione>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("SisDoDescripcione.findAll");
				retorno = (List<SisDoDescripcione>)query.getResultList();
				
				return retorno;
			}	
	
}