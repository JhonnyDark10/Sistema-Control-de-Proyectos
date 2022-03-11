package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sis_do_donaciones database table.
 * 
 */
@Entity
@Table(name="sis_do_donaciones")

@NamedQuery(name="SisDoDonacione.findAll", query="SELECT s FROM SisDoDonacione s")




public class SisDoDonacione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_donacion")
	private int idDonacion;

	private String cedulaRuc;

	@Column(name="estado_donacion")
	private String estadoDonacion;

	private String nombre;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	private int usuario;
	
	@Column(name="nombre_aportante")
	private String nombreAportante;

	//bi-directional many-to-one association to SisDoAporte
	@OneToMany(mappedBy="sisDoDonacione")
	private List<SisDoAporte> sisDoAportes;

	//bi-directional many-to-one association to SisDoDescripcione
	@OneToMany(mappedBy="sisDoDonacione")
	private List<SisDoDescripcione> sisDoDescripciones;

	public SisDoDonacione() {
	}

	public int getIdDonacion() {
		return this.idDonacion;
	}

	public void setIdDonacion(int idDonacion) {
		this.idDonacion = idDonacion;
	}

	public String getCedulaRuc() {
		return this.cedulaRuc;
	}

	public void setCedulaRuc(String cedulaRuc) {
		this.cedulaRuc = cedulaRuc;
	}

	public String getEstadoDonacion() {
		return this.estadoDonacion;
	}

	public void setEstadoDonacion(String estadoDonacion) {
		this.estadoDonacion = estadoDonacion;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public String getNombreAportante() {
		return this.nombreAportante;
	}

	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setNombreAportante(String nombreAportante) {
		this.nombreAportante = nombreAportante;
	}

	public List<SisDoAporte> getSisDoAportes() {
		return this.sisDoAportes;
	}

	public void setSisDoAportes(List<SisDoAporte> sisDoAportes) {
		this.sisDoAportes = sisDoAportes;
	}

	public SisDoAporte addSisDoAporte(SisDoAporte sisDoAporte) {
		getSisDoAportes().add(sisDoAporte);
		sisDoAporte.setSisDoDonacione(this);

		return sisDoAporte;
	}

	public SisDoAporte removeSisDoAporte(SisDoAporte sisDoAporte) {
		getSisDoAportes().remove(sisDoAporte);
		sisDoAporte.setSisDoDonacione(null);

		return sisDoAporte;
	}

	public List<SisDoDescripcione> getSisDoDescripciones() {
		return this.sisDoDescripciones;
	}

	public void setSisDoDescripciones(List<SisDoDescripcione> sisDoDescripciones) {
		this.sisDoDescripciones = sisDoDescripciones;
	}

	public SisDoDescripcione addSisDoDescripcione(SisDoDescripcione sisDoDescripcione) {
		getSisDoDescripciones().add(sisDoDescripcione);
		sisDoDescripcione.setSisDoDonacione(this);

		return sisDoDescripcione;
	}

	public SisDoDescripcione removeSisDoDescripcione(SisDoDescripcione sisDoDescripcione) {
		getSisDoDescripciones().remove(sisDoDescripcione);
		sisDoDescripcione.setSisDoDonacione(null);

		return sisDoDescripcione;
	}

}