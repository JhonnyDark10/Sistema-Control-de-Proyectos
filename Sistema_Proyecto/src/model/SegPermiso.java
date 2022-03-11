package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the seg_permiso database table.
 * 
 */
@Entity
@Table(name="seg_permiso")
@NamedQueries({
	@NamedQuery(name="SegPermisoElimina.findAll", query="SELECT s FROM SegPermiso s where s.estado = 'A' and s.segMenu.idMenu = :patron and s.segPerfil.id_perfil =:patronP order by s.segMenu.posicion asc"),
	@NamedQuery(name="SegPermiso.findAll", query="SELECT s FROM SegPermiso s where s.estado = 'A'"),
	@NamedQuery(name="SegPermisoT.findAll", query="SELECT s FROM SegPermiso s where s.estado = 'A' and s.segMenu.id_menuPadre = 0 order by s.segMenu.posicion asc"),
	@NamedQuery(name="SegPermiso.buscarPadrePorPerfil", 
	query="SELECT s FROM SegPermiso s where s.segPerfil.id_perfil = :idperfil and s.estado = 'A' and s.segMenu.id_menuPadre = 0 order by s.segMenu.posicion asc"),
	@NamedQuery(name="SegPermiso.buscarTodosPorPerfil", 
	query="SELECT s FROM SegPermiso s where s.segPerfil.id_perfil = :idperfil and s.estado = 'A'"),
})
public class SegPermiso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_permiso")
	private int idPermiso;

	private String estado;

	//bi-directional many-to-one association to SegMenu
	@ManyToOne
	@JoinColumn(name="id_fk_menu")
	private SegMenu segMenu;

	//bi-directional many-to-one association to SegPerfil
	@ManyToOne
	@JoinColumn(name="id_fk_perfil")
	private SegPerfil segPerfil;

	public SegPermiso() {
	}

	public int getIdPermiso() {
		return this.idPermiso;
	}

	public void setIdPermiso(int idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public SegMenu getSegMenu() {
		return this.segMenu;
	}

	public void setSegMenu(SegMenu segMenu) {
		this.segMenu = segMenu;
	}

	public SegPerfil getSegPerfil() {
		return this.segPerfil;
	}

	public void setSegPerfil(SegPerfil segPerfil) {
		this.segPerfil = segPerfil;
	}

}