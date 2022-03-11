package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SisProyectoTareaDAO extends ClaseDAO {

	//Metodos para que retorne todo 
			public List<SisProyectoTarea> getProyectoTareas(){
				List<SisProyectoTarea> retorno = new ArrayList<SisProyectoTarea>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("SisProyectoTarea.findAll");
				retorno = (List<SisProyectoTarea>)query.getResultList();
				
				return retorno;
			}	
			
			
			
			public List<SisProyectoTarea> getBuscarCabecera(int value){
				List<SisProyectoTarea> retorno = new ArrayList<SisProyectoTarea>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("buscaCabecera.findAll").setParameter("patron", value);
				
				
				retorno = (List<SisProyectoTarea>)query.getResultList();
				
				return retorno;
			}
			
			
			
			
			public List<SisProyectoTarea> getEditaPorcentaje(int value){
				List<SisProyectoTarea> retorno = new ArrayList<SisProyectoTarea>();
				
				
				//Hacer el Query
				Query query = getEntityManager().createNamedQuery("buscaEditar.findAll").setParameter("patron", value);
				
				
				retorno = (List<SisProyectoTarea>)query.getResultList();
				
				return retorno;
			}
			
			
			
	
}
