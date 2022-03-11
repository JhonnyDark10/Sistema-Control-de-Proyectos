package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sis_proyecto_propuesta database table.
 * 
 */
@Entity
@Table(name="sis_proyecto_propuesta")


@NamedQueries({
	@NamedQuery(name="SisProyectoPropuesta.findAll", query="SELECT s FROM SisProyectoPropuesta s where s.estadoProyecto = 'A' and LOWER(s.nombre) LIKE :patron"),
	
	
	@NamedQuery(name="SisCedula.findAll", query="SELECT s FROM SisProyectoPropuesta s where (s.estadoProyecto = 'A' or s.estadoProyecto = 'E' or s.estadoProyecto = 'T' or s.estadoProyecto = 'I' ) and s.cedula = :patron"),
	
	@NamedQuery(name="SisBuscarPorId.findAll", query="SELECT s FROM SisProyectoPropuesta s where s.estadoProyecto = 'I' and s.idProyectoPropuesta = :patron"),
	@NamedQuery(name="SisBuscarRevision.findAll", query="SELECT s FROM SisProyectoPropuesta s where s.estadoProyecto = 'A' and s.idProyectoPropuesta = :patron"),
	@NamedQuery(name="SisProyectoEjecutados.findAll", query="SELECT s FROM SisProyectoPropuesta s where s.estadoProyecto = 'E' and LOWER(s.nombre) LIKE :patron"),
	
	@NamedQuery(name="SisBuscarPorEstadoPorcentaje.findAll", query="SELECT s FROM SisProyectoPropuesta s where s.estadoProyecto = 'E' and s.idProyectoPropuesta = :patron"),
	//
	
	//
	
})



public class SisProyectoPropuesta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_proyecto_propuesta")
	private int idProyectoPropuesta;

	private String autor;

	@Column(name="calificacion_general")
	private int calificacionGeneral;

	private String cedula;

	private int usuario;
	
	private String correo;

	private String documento;

	@Column(name="estado_proyecto")
	private String estadoProyecto;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	//bi-directional many-to-one association to SegUsuario
		@ManyToOne
		@JoinColumn(name="fk_usuario_encargado")
		private SegUsuario segUsuario;

	private String nombre;

	private String observacion;

	@Column(name="porcentaje_total")
	private int porcentajeTotal;

	private String telefono;

	@Column(name="valor_total")
	private float valorTotal;

	//bi-directional many-to-one association to SisProyectoCategoria
	@ManyToOne
	@JoinColumn(name="fk_categoria")
	private SisProyectoCategoria sisProyectoCategoria;

	//bi-directional many-to-one association to SisProyectoEstado
	@ManyToOne
	@JoinColumn(name="fk_estado")
	private SisProyectoEstado sisProyectoEstado;

	//bi-directional many-to-one association to SisProyectoTarea
	@OneToMany(mappedBy="sisProyectoPropuesta")
	private List<SisProyectoTarea> sisProyectoTareas;

	public SisProyectoPropuesta() {
	}

	public int getIdProyectoPropuesta() {
		return this.idProyectoPropuesta;
	}

	public void setIdProyectoPropuesta(int idProyectoPropuesta) {
		this.idProyectoPropuesta = idProyectoPropuesta;
	}

	public String getAutor() {
		return this.autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getCalificacionGeneral() {
		return this.calificacionGeneral;
	}

	public void setCalificacionGeneral(int calificacionGeneral) {
		this.calificacionGeneral = calificacionGeneral;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEstadoProyecto() {
		return this.estadoProyecto;
	}

	public void setEstadoProyecto(String estadoProyecto) {
		this.estadoProyecto = estadoProyecto;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	
	
	public SegUsuario getSegUsuario() {
		return this.segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public int getPorcentajeTotal() {
		return this.porcentajeTotal;
	}

	public void setPorcentajeTotal(int porcentajeTotal) {
		this.porcentajeTotal = porcentajeTotal;
	}

	public String getTelefono() {
		return this.telefono;
	}

	
	
	
	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public float getValorTotal() {
		return this.valorTotal;
	}

	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public SisProyectoCategoria getSisProyectoCategoria() {
		return this.sisProyectoCategoria;
	}

	public void setSisProyectoCategoria(SisProyectoCategoria sisProyectoCategoria) {
		this.sisProyectoCategoria = sisProyectoCategoria;
	}

	public SisProyectoEstado getSisProyectoEstado() {
		return this.sisProyectoEstado;
	}

	public void setSisProyectoEstado(SisProyectoEstado sisProyectoEstado) {
		this.sisProyectoEstado = sisProyectoEstado;
	}

	public List<SisProyectoTarea> getSisProyectoTareas() {
		return this.sisProyectoTareas;
	}

	public void setSisProyectoTareas(List<SisProyectoTarea> sisProyectoTareas) {
		this.sisProyectoTareas = sisProyectoTareas;
	}

	public SisProyectoTarea addSisProyectoTarea(SisProyectoTarea sisProyectoTarea) {
		getSisProyectoTareas().add(sisProyectoTarea);
		sisProyectoTarea.setSisProyectoPropuesta(this);

		return sisProyectoTarea;
	}

	public SisProyectoTarea removeSisProyectoTarea(SisProyectoTarea sisProyectoTarea) {
		getSisProyectoTareas().remove(sisProyectoTarea);
		sisProyectoTarea.setSisProyectoPropuesta(null);

		return sisProyectoTarea;
	}
	
	

}