package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import model.SegMenu;
import model.SegPermiso;
import model.SegPermisoDAO;
import model.SegUsuario;
import model.SegUsuarioDAO;
import util.RecuperarClave;
import util.SecurityUtil;

public class Menu {
	
	//verifica cargar
	Menu buscar;
	private List<SegUsuario> verificar = new ArrayList<SegUsuario>();
	//hasta aqui
	@Wire Tree menu;
	@Wire Include areaContenido;
	SegMenu opcionSeleccionado;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SegPermisoDAO permisoDAO = new SegPermisoDAO();
	List<SegPermiso> listaPermisosPadre = new ArrayList<SegPermiso>();
	List<SegPermiso> listaPermisosTodos = new ArrayList<SegPermiso>();
	
	//aqui aumente para comprobar 
	List<SegUsuario> verificaCambio;
	//

	public static int variable_controlador = 0;
	public static int variable_nomUsu = 0;	
	public static String Glob_Cedula = "";
	public static String Glob_Correo ="";
	
	
	@Wire Label operador,nombreU;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		loadTree();
		//para recordar que cambie contraseña el usuario
		cargar();
		//para visualizar la descripcion del campo ue ingrese
		visualiza_mensaje();
	}
	
	public void visualiza_mensaje(){
		
		areaContenido.setSrc("V_Mensaje/Modulo_Administracion.zul");
	}
	
	
	
	public void cargar(){
		//VERIFICAR SI ESTA EN 1 O 0 PARA PROCEDER A HABILITAR LOS BOTONES
		verificar = usuarioDAO.getVerificaCambio(buscar.variable_nomUsu);
		if(verificar.size() > 0){
			
		}
	}
	
	
	public void loadTree() throws IOException{		
		SegUsuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim());

		//variable para guardar el controlador
		variable_controlador = usuario.getIdUsuario();
	
		//variable para buscar id de usuario
		variable_nomUsu = usuario.getIdUsuario();
		
		if (usuario != null){
			listaPermisosPadre = permisoDAO.getListaPermisosPadre(usuario.getSegPerfil().getId_perfil());
			listaPermisosTodos = permisoDAO.getListaPermisosTodos(usuario.getSegPerfil().getId_perfil());
			
			operador.setValue(usuario.getSegPerfil().getDescripcion());
			nombreU.setValue(usuario.getApellidoPaterno() + " " +
					         usuario.getNombre());
			if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<SegMenu> listaMenu = new ArrayList<SegMenu>();
				for(SegPermiso permiso : listaPermisosPadre) listaMenu.add(permiso.getSegMenu());
				menu.appendChild(getTreechildren(listaMenu)); 
				
			}			
		}
		listaPermisosPadre = null;
	}
	private Treechildren getTreechildren(List<SegMenu> listaMenu) {
		Treechildren retorno = new Treechildren();
		
		for(SegMenu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);
			
			retorno.appendChild(ti);
			List<SegMenu> listaPadreHijo = new ArrayList<SegMenu>();
			for(SegPermiso permiso : listaPermisosTodos) {
				if(permiso.getSegMenu().getId_menuPadre() == opcion.getIdMenu())
					listaPadreHijo.add(permiso.getSegMenu());
			}
			if (!listaPadreHijo.isEmpty()) {
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private Treeitem getTreeitem(SegMenu opcion) {
		Treeitem retorno = new Treeitem();
		
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());
		tc.setStyle("font-size: 0.83em;color:black;font: arial, helvetica, sans-serif;border-top-color: transparent;");

		//tc.setHeight("5px");
		//System.out.println("titulomenu: " + tc);
		if (opcion.getIcono() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getIcono());
		}
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}
	
	
	
	@NotifyChange({"areaContenido"})
	public void loadContenido(SegMenu opcion) {
		
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
	}
	
	
	
	public String getNombreUsuario() {
		return SecurityUtil.getUser().getUsername();
	}
	
	}
