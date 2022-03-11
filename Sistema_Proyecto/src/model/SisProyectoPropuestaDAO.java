package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisProyectoPropuestaDAO extends ClaseDAO{

	//Metodos para que retorne todo los tipos de especies 
	public List<SisProyectoPropuesta> getListaProyectoPropueta(String value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisProyectoPropuesta.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	
	
	public List<SisProyectoPropuesta> getListaPropuestaSeleccionada(int value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisBuscarPorId.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	
	
	
	
	public List<SisProyectoPropuesta> getListaRevision(int value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisBuscarRevision.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	
	
	public List<SisProyectoPropuesta> getListaProyectoEjecutar(String value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisProyectoEjecutados.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	
	
	public List<SisProyectoPropuesta> getListaProcentaje(int value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisBuscarPorEstadoPorcentaje.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	public List<SisProyectoPropuesta> getRecuperaCedulaTabla(String value){
		List<SisProyectoPropuesta> retorno = new ArrayList<SisProyectoPropuesta>();
		
			
		//txtagregar
		Query query = getEntityManager().createNamedQuery("SisCedula.findAll").setParameter("patron", value);
		retorno = (List<SisProyectoPropuesta>)query.getResultList();
		
		return retorno;
	}
	
	
	
}



