package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the seg_perfil database table.
 * 
 */
@Entity
@Table(name="seg_perfil")
@NamedQuery(name="SegPerfil.findAll", query="SELECT s FROM SegPerfil s where s.estado = 'A' and LOWER(s.codigo) LIKE :patron")
public class SegPerfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_perfil;

	private String codigo;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to SegPermiso
	@OneToMany(mappedBy="segPerfil")
	private List<SegPermiso> segPermisos;

	//bi-directional many-to-one association to SegUsuario
	@OneToMany(mappedBy="segPerfil")
	private List<SegUsuario> segUsuarios;

	public SegPerfil() {
	}

	public int getId_perfil() {
		return this.id_perfil;
	}

	public void setId_perfil(int id_perfil) {
		this.id_perfil = id_perfil;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<SegPermiso> getSegPermisos() {
		return this.segPermisos;
	}

	public void setSegPermisos(List<SegPermiso> segPermisos) {
		this.segPermisos = segPermisos;
	}

	public SegPermiso addSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().add(segPermiso);
		segPermiso.setSegPerfil(this);

		return segPermiso;
	}

	public SegPermiso removeSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().remove(segPermiso);
		segPermiso.setSegPerfil(null);

		return segPermiso;
	}

	public List<SegUsuario> getSegUsuarios() {
		return this.segUsuarios;
	}

	public void setSegUsuarios(List<SegUsuario> segUsuarios) {
		this.segUsuarios = segUsuarios;
	}

	public SegUsuario addSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().add(segUsuario);
		segUsuario.setSegPerfil(this);

		return segUsuario;
	}

	public SegUsuario removeSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().remove(segUsuario);
		segUsuario.setSegPerfil(null);

		return segUsuario;
	}

}