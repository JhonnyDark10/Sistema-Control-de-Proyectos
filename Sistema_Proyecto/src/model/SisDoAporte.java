package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sis_do_aporte database table.
 * 
 */
@Entity
@Table(name="sis_do_aporte")
@NamedQuery(name="SisDoAporte.findAll", query="SELECT s FROM SisDoAporte s")
public class SisDoAporte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_aporte")
	private int idAporte;

	@Column(name="estado_aporte")
	private String estadoAporte;

	private float valor;

	//bi-directional many-to-one association to SisDoDonacione
	@ManyToOne
	@JoinColumn(name="fk_id_donacion")
	private SisDoDonacione sisDoDonacione;

	public SisDoAporte() {
	}

	public int getIdAporte() {
		return this.idAporte;
	}

	public void setIdAporte(int idAporte) {
		this.idAporte = idAporte;
	}

	public String getEstadoAporte() {
		return this.estadoAporte;
	}

	public void setEstadoAporte(String estadoAporte) {
		this.estadoAporte = estadoAporte;
	}

	public float getValor() {
		return this.valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public SisDoDonacione getSisDoDonacione() {
		return this.sisDoDonacione;
	}

	public void setSisDoDonacione(SisDoDonacione sisDoDonacione) {
		this.sisDoDonacione = sisDoDonacione;
	}

}