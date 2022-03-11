package model;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the seg_usuario database table.
 * 
 */
@Entity
@Table(name="seg_usuario")
@NamedQueries({
	@NamedQuery(name="SegUsuarioTodo.findAll", query="SELECT s FROM SegUsuario s where s.estado = 'A' and LOWER(s.cedula) LIKE :patron"),
	@NamedQuery(name="SegUsuario.findAll", query="SELECT s FROM SegUsuario s"),
	@NamedQuery(name="SegUsuario.buscaUsuario", 
	query="SELECT s FROM SegUsuario s WHERE s.usuario = :nombreUsuario and s.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarPorUsuario", 
	query="SELECT s FROM SegUsuario s where s.usuario = :patron and s.idUsuario <> :idUsuario and s.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarCambioClave", 
	query="SELECT s FROM SegUsuario s where s.idUsuario = :patron and s.verifica = 1 and s.estado='A'"),
	@NamedQuery(name="SegUsuario.ListarSinAdm", 
	query="SELECT s FROM SegUsuario s where  s.segPerfil.id_perfil <> 1  and  s.estado='A'"),
	
	@NamedQuery(name="SisCedulaUsuario.findAll", 
	query="SELECT s FROM SegUsuario s where (s.estado = 'A' or s.estado = 'I' ) and s.cedula = :patron")
	
	
})
public class SegUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private int idUsuario;

	@Column(name="APELLIDO_MATERNO")
	private String apellidoMaterno;

	@Column(name="APELLIDO_PATERNO")
	private String apellidoPaterno;

	private String cedula;

	private String celular;

	private String clave;

	private int controlador;

	private String email;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Time hora;

	private String nombre;

	private String telefono;

	private String usuario;

	private int verifica;

		
	//bi-directional many-to-one association to SegPeriodoEncargo
	@OneToMany(mappedBy="segUsuario")
	private List<SegPeriodoEncargo> segPeriodoEncargos;

	
		
	//bi-directional many-to-one association to SegPerfil
	@ManyToOne
	@JoinColumn(name="id_fk_perfil")
	private SegPerfil segPerfil;

	
	//bi-directional many-to-one association to SisProyectoPropuesta
		@OneToMany(mappedBy="segUsuario")
		private List<SisProyectoPropuesta> sisProyectoPropuestas;

		
	public SegUsuario() {
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getControlador() {
		return this.controlador;
	}

	public void setControlador(int controlador) {
		this.controlador = controlador;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getVerifica() {
		return this.verifica;
	}

	public void setVerifica(int verifica) {
		this.verifica = verifica;
	}

	public List<SegPeriodoEncargo> getSegPeriodoEncargos() {
		return this.segPeriodoEncargos;
	}

	public void setSegPeriodoEncargos(List<SegPeriodoEncargo> segPeriodoEncargos) {
		this.segPeriodoEncargos = segPeriodoEncargos;
	}

	public SegPeriodoEncargo addSegPeriodoEncargo(SegPeriodoEncargo segPeriodoEncargo) {
		getSegPeriodoEncargos().add(segPeriodoEncargo);
		segPeriodoEncargo.setSegUsuario(this);

		return segPeriodoEncargo;
	}

	public SegPeriodoEncargo removeSegPeriodoEncargo(SegPeriodoEncargo segPeriodoEncargo) {
		getSegPeriodoEncargos().remove(segPeriodoEncargo);
		segPeriodoEncargo.setSegUsuario(null);

		return segPeriodoEncargo;
	}

	public List<SisProyectoPropuesta> getSisProyectoPropuestas() {
		return this.sisProyectoPropuestas;
	}

	public void setSisProyectoPropuestas(List<SisProyectoPropuesta> sisProyectoPropuestas) {
		this.sisProyectoPropuestas = sisProyectoPropuestas;
	}
	
	public SegPerfil getSegPerfil() {
		return this.segPerfil;
	}

	public void setSegPerfil(SegPerfil segPerfil) {
		this.segPerfil = segPerfil;
	}

}