package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sis_proyecto_categoria database table.
 * 
 */
@Entity
@Table(name="sis_proyecto_categoria")
@NamedQuery(name="SisProyectoCategoria.findAll", query="SELECT s FROM SisProyectoCategoria s where s.estadoCategoria = 'A' and LOWER(s.nombreCategoria) LIKE :patron")
public class SisProyectoCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_categoria")
	private int idCategoria;

	@Column(name="estado_categoria")
	private String estadoCategoria;

	@Column(name="nombre_categoria")
	private String nombreCategoria;

	//bi-directional many-to-one association to SisProyectoPropuesta
	@OneToMany(mappedBy="sisProyectoCategoria")
	private List<SisProyectoPropuesta> sisProyectoPropuestas;

	public SisProyectoCategoria() {
	}

	public int getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getEstadoCategoria() {
		return this.estadoCategoria;
	}

	public void setEstadoCategoria(String estadoCategoria) {
		this.estadoCategoria = estadoCategoria;
	}

	public String getNombreCategoria() {
		return this.nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public List<SisProyectoPropuesta> getSisProyectoPropuestas() {
		return this.sisProyectoPropuestas;
	}

	public void setSisProyectoPropuestas(List<SisProyectoPropuesta> sisProyectoPropuestas) {
		this.sisProyectoPropuestas = sisProyectoPropuestas;
	}

	public SisProyectoPropuesta addSisProyectoPropuesta(SisProyectoPropuesta sisProyectoPropuesta) {
		getSisProyectoPropuestas().add(sisProyectoPropuesta);
		sisProyectoPropuesta.setSisProyectoCategoria(this);

		return sisProyectoPropuesta;
	}

	public SisProyectoPropuesta removeSisProyectoPropuesta(SisProyectoPropuesta sisProyectoPropuesta) {
		getSisProyectoPropuestas().remove(sisProyectoPropuesta);
		sisProyectoPropuesta.setSisProyectoCategoria(null);

		return sisProyectoPropuesta;
	}

}