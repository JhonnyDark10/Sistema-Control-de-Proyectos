package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;


public class SegUsuarioDAO extends ClaseDAO{
	
	public SegUsuario getUsuario(String nombreUsuario) {
		SegUsuario usuario; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("SegUsuario.buscaUsuario");
		consulta.setParameter("nombreUsuario", nombreUsuario);
		usuario = (SegUsuario) consulta.getSingleResult();
		return usuario;
	}

	@SuppressWarnings("unchecked")
	public List<SegUsuario> getBuscarUsuario(String value,Integer idUsuario) {
		List<SegUsuario> resultado = new ArrayList<SegUsuario>(); 
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarPorUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", value.toLowerCase());
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getTodos(String value){
		List<SegUsuario> retorno = new ArrayList<SegUsuario>();
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}

		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SegUsuarioTodo.findAll").setParameter("patron", value);
		
		
		retorno = (List<SegUsuario>)query.getResultList();
		
		return retorno;
	}
	
	
	
	
	
	
	//esto aumente para cambio de clave
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getVerificaCambio(Integer id) {
		List<SegUsuario> retorno = new ArrayList<SegUsuario>();
		
		Query consulta;
		consulta = getEntityManager().createNamedQuery("SegUsuario.buscarCambioClave");
		consulta.setParameter("patron", id);
		retorno = (List<SegUsuario>)consulta.getResultList();
		return retorno;
	}
	
	//
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getSinAmd() {
		List<SegUsuario> retorno = new ArrayList<SegUsuario>();
		
		Query consulta;
		consulta = getEntityManager().createNamedQuery("SegUsuario.ListarSinAdm");
		
		retorno = (List<SegUsuario>)consulta.getResultList();
		return retorno;
	}
	//
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getRecuperaCedulaTabla(String value){
		List<SegUsuario> retorno = new ArrayList<SegUsuario>();
		
			
		//Hacer el Query
		Query query = getEntityManager().createNamedQuery("SisCedulaUsuario.findAll").setParameter("patron", value);
		retorno = (List<SegUsuario>)query.getResultList();
		
		return retorno;
	}

}
