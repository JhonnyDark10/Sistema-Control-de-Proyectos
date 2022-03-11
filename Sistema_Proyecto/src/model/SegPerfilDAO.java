package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegPerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getListaPerfiles(String value) {
		List<SegPerfil> retorno = new ArrayList<SegPerfil>();
		
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("SegPerfil.findAll").setParameter("patron", value);
		retorno = (List<SegPerfil>) query.getResultList();
		return retorno;
	}
}
