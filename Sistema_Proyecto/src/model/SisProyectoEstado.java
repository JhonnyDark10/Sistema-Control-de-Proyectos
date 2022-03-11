package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sis_proyecto_estado database table.
 * 
 */
@Entity
@Table(name="sis_proyecto_estado")
@NamedQueries({
@NamedQuery(name="SisProyectoEstado.findAll", query="SELECT s FROM SisProyectoEstado s where s.estadoProyecto = 'A' and LOWER(s.nombreEstado) LIKE :patron"),
@NamedQuery(name="SisProyectoPropuesta.soloPropuesta", query="SELECT s FROM SisProyectoEstado s where s.idEstado = 1 and s.estadoProyecto = 'A'")
})
public class SisProyectoEstado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado")
	private int idEstado;

	@Column(name="estado_proyecto")
	private String estadoProyecto;

	@Column(name="nombre_estado")
	private String nombreEstado;

	//bi-directional many-to-one association to SisProyectoPropuesta
	@OneToMany(mappedBy="sisProyectoEstado")
	private List<SisProyectoPropuesta> sisProyectoPropuestas;

	public SisProyectoEstado() {
	}

	public int getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(int idEstado) {
		this.idEstado = idEstado;
	}

	public String getEstadoProyecto() {
		return this.estadoProyecto;
	}

	public void setEstadoProyecto(String estadoProyecto) {
		this.estadoProyecto = estadoProyecto;
	}

	public String getNombreEstado() {
		return this.nombreEstado;
	}

	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public List<SisProyectoPropuesta> getSisProyectoPropuestas() {
		return this.sisProyectoPropuestas;
	}

	public void setSisProyectoPropuestas(List<SisProyectoPropuesta> sisProyectoPropuestas) {
		this.sisProyectoPropuestas = sisProyectoPropuestas;
	}

	public SisProyectoPropuesta addSisProyectoPropuesta(SisProyectoPropuesta sisProyectoPropuesta) {
		getSisProyectoPropuestas().add(sisProyectoPropuesta);
		sisProyectoPropuesta.setSisProyectoEstado(this);

		return sisProyectoPropuesta;
	}

	public SisProyectoPropuesta removeSisProyectoPropuesta(SisProyectoPropuesta sisProyectoPropuesta) {
		getSisProyectoPropuestas().remove(sisProyectoPropuesta);
		sisProyectoPropuesta.setSisProyectoEstado(null);

		return sisProyectoPropuesta;
	}

}