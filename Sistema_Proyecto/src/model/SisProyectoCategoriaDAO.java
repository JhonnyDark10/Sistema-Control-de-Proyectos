package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisProyectoCategoriaDAO extends ClaseDAO{

	//Metodos para que retorne todo los tipos de especies 
	public List<SisProyectoCategoria> getTipoCategoria(String value){
		List<SisProyectoCategoria> retorno = new ArrayList<SisProyectoCategoria>();
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisProyectoCategoria.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoCategoria>)query.getResultList();
		
		return retorno;
	}
}
