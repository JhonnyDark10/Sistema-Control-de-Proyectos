package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegMenuDAO extends ClaseDAO{

	@SuppressWarnings("unchecked")
	public List<SegMenu> getOpciones(){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenu.findAll");
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegMenu> getListaMenuHijo(Integer idPadre){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenu.buscarHijos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPadre", idPadre);
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<SegMenu> getRecuperaPadres(){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenuP.findAll");
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegMenu> getRecuperaTodos(){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenuT.findAll");
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegMenu> getObtenerDescripcion(Integer idPadre){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegObtenerNombre.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", idPadre);
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
}
