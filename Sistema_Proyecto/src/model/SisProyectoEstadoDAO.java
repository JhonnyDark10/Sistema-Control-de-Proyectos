package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisProyectoEstadoDAO extends ClaseDAO{

	//Metodos para que retorne todo los tipos de especies 
	public List<SisProyectoEstado> getTipoEstado(String value){
		List<SisProyectoEstado> retorno = new ArrayList<SisProyectoEstado>();
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisProyectoEstado.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoEstado>)query.getResultList();
		
		return retorno;
	}
	
	
	
	public List<SisProyectoEstado> getTipoEstadoPropuesta(){
		List<SisProyectoEstado> retorno = new ArrayList<SisProyectoEstado>();
		
		Query query = getEntityManager().createNamedQuery("SisProyectoPropuesta.soloPropuesta");
		retorno = (List<SisProyectoEstado>)query.getResultList();
		
		return retorno;
	}
	
}
