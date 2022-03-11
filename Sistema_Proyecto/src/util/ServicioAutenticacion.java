package util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zkoss.zk.ui.util.Clients;

import model.SegPerfil;
import model.SegPermiso;
import model.SegUsuario;
import model.SegUsuarioDAO;



/**
 * Detalle de datos de los usuarios que acceden al sistema.
 * Debe implementar UserDetailsService para poder usarse como fuente de datos de Spring
 * security.
 */
public class ServicioAutenticacion implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		try {
         	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
			SegUsuario usuario; 
			User usuarioSpring;
			List<GrantedAuthority> privilegios; 
			usuario = usuarioDAO.getUsuario(nombreUsuario);
			privilegios = obtienePrivilegios(usuario.getSegPerfil());
								
			usuarioSpring = new User(usuario.getUsuario(), usuario.getClave(), privilegios);
			return usuarioSpring;
					
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			//auditoriaDAO.getEntityManager().getTransaction().rollback();
			return null;
		}

	}

	/**
	 * Elabora una lista de objetos del tipo GrantedAuthority en base a los permisos
	 * del usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	private List<GrantedAuthority> obtienePrivilegios(SegPerfil perfil) {
		List<GrantedAuthority> listaPrivilegios = new ArrayList<GrantedAuthority>(); 

		for(SegPermiso permiso  : perfil.getSegPermisos())
			listaPrivilegios.add(new SimpleGrantedAuthority(permiso.getSegPerfil().getDescripcion()));

		return listaPrivilegios;
	}

}
