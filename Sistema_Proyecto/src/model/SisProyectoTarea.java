package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_proyecto_tarea database table.
 * 
 */
@Entity
@Table(name="sis_proyecto_tarea")
@NamedQuery(name="SisProyectoTarea.findAll", query="SELECT s FROM SisProyectoTarea s")




@NamedQueries({
	@NamedQuery(name="SisProyectoTarea.findAll", query="SELECT s FROM SisProyectoTarea s"),
	@NamedQuery(name="buscaCabecera.findAll", query="SELECT s FROM SisProyectoTarea s where s.estadoTarea = 'A' and s.sisProyectoPropuesta.idProyectoPropuesta = :patron"),
	@NamedQuery(name="buscaEditar.findAll", query="SELECT s FROM SisProyectoTarea s where s.estadoTarea = 'A' and s.idTarea = :patron")
})
public class SisProyectoTarea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tarea")
	private int idTarea;

	@Column(name="costo_tarea")
	private float costoTarea;

	@Column(name="estado_tarea")
	private String estadoTarea;

	@Column(name="nombre_tarea")
	private String nombreTarea;

	@Column(name="porcentaje_tarea")
	private int porcentajeTarea;

	//bi-directional many-to-one association to SisProyectoPropuesta
	@ManyToOne
	@JoinColumn(name="fk_id_propuesta")
	private SisProyectoPropuesta sisProyectoPropuesta;

	public SisProyectoTarea() {
	}

	public int getIdTarea() {
		return this.idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public float getCostoTarea() {
		return this.costoTarea;
	}

	public void setCostoTarea(float costoTarea) {
		this.costoTarea = costoTarea;
	}

	public String getEstadoTarea() {
		return this.estadoTarea;
	}

	public void setEstadoTarea(String estadoTarea) {
		this.estadoTarea = estadoTarea;
	}

	public String getNombreTarea() {
		return this.nombreTarea;
	}

	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}

	public int getPorcentajeTarea() {
		return this.porcentajeTarea;
	}

	public void setPorcentajeTarea(int porcentajeTarea) {
		this.porcentajeTarea = porcentajeTarea;
	}

	public SisProyectoPropuesta getSisProyectoPropuesta() {
		return this.sisProyectoPropuesta;
	}

	public void setSisProyectoPropuesta(SisProyectoPropuesta sisProyectoPropuesta) {
		this.sisProyectoPropuesta = sisProyectoPropuesta;
	}

}