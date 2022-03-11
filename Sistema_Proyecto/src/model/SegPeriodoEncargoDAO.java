package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegPeriodoEncargoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegPeriodoEncargo> getListaPerfiles(String value) {
		List<SegPeriodoEncargo> retorno = new ArrayList<SegPeriodoEncargo>();
		
		
		if (value == null || value.length() == 0) {
			value = "%";
		}else{
			value = "%" + value.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("SegPeriodoEncargo.findAll").setParameter("patron", value);
		retorno = (List<SegPeriodoEncargo>) query.getResultList();
		return retorno;
	}
}
