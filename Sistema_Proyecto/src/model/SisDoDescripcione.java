package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_do_descripciones database table.
 * 
 */
@Entity
@Table(name="sis_do_descripciones")
@NamedQuery(name="SisDoDescripcione.findAll", query="SELECT s FROM SisDoDescripcione s")
public class SisDoDescripcione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_descripcion")
	private int idDescripcion;

	private int cantidad;

	@Column(name="estado_descripcion")
	private String estadoDescripcion;

	@Column(name="nombre_descripcion")
	private String nombreDescripcion;

	//bi-directional many-to-one association to SisDoDonacione
	@ManyToOne
	@JoinColumn(name="fk_id_donacion")
	private SisDoDonacione sisDoDonacione;

	public SisDoDescripcione() {
	}

	public int getIdDescripcion() {
		return this.idDescripcion;
	}

	public void setIdDescripcion(int idDescripcion) {
		this.idDescripcion = idDescripcion;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstadoDescripcion() {
		return this.estadoDescripcion;
	}

	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}

	public String getNombreDescripcion() {
		return this.nombreDescripcion;
	}

	public void setNombreDescripcion(String nombreDescripcion) {
		this.nombreDescripcion = nombreDescripcion;
	}

	public SisDoDonacione getSisDoDonacione() {
		return this.sisDoDonacione;
	}

	public void setSisDoDonacione(SisDoDonacione sisDoDonacione) {
		this.sisDoDonacione = sisDoDonacione;
	}

}