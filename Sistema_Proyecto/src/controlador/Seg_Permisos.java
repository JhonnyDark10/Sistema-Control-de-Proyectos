package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import model.SegMenu;
import model.SegMenuDAO;
import model.SegPerfil;
import model.SegPerfilDAO;
import model.SegPermiso;
import model.SegPermisoDAO;
import model.SegUsuario;
import model.SegUsuarioDAO;

import util.SecurityUtil;

@SuppressWarnings({ "serial", "rawtypes" })
public class Seg_Permisos extends SelectorComposer {
	
	@Wire Combobox combo;
	
	//@Wire Toolbarbutton btnRecuperar;
	//todo menu
	@Wire Tree menu;
	@Wire Include areaContenido;
	SegMenu opcionSeleccionado;
	
		
	//combo
	private SegPerfilDAO PerfilLista= new SegPerfilDAO();
	private SegPerfil perfilSelecionada;
	
	//todo menu
	SegMenuDAO permisoDAO = new SegMenuDAO();
	List<SegMenu> listaPermisosPadre = new ArrayList<SegMenu>();
	List<SegMenu> listaPermisosTodos = new ArrayList<SegMenu>();
	
	List<SegPermiso> lista;
	List<SegPermiso> listaPermisos;
	//menu segun perfil
	//menu segun perfil
	@Wire Tree menu1;
	SegMenu opcionSeleccionado1;
	SegPermiso opcionSeleccionadaP;
	SegPermisoDAO permiso1DAO = new SegPermisoDAO();
	List<SegPermiso> listaPermisosPadre1 = new ArrayList<SegPermiso>();
	List<SegPermiso> listaPermisosTodos1 = new ArrayList<SegPermiso>();
	
	//para guardar y eliminar 
	private SegPermiso controlingresotipo;
	private SegPermisoDAO Sispermisodao = new SegPermisoDAO();
	
	@Wire Label operador;
	
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);	
		loadTree();
          controlingresotipo = null;
		
		if(Executions.getCurrent().getArg().get("SegPermiso")!=null){
			//Persona Nueva
			controlingresotipo = new SegPermiso();
			
			
		}else{
			controlingresotipo = new SegPermiso();
			
			
		}
	}
	
	//@Command("btn_Recuperar")
	
	@Command("comboseleccionado")
	public void deleteOrder(@ContextParam(ContextType.VIEW) Component view) throws IOException {
		Selectors.wireComponents(view, this, false);
		menu1.getChildren().clear();
		
	    cargarTree();  
	}
	
	
	////////////////////////////////////////////////////////
	//recupera todo los menus que estan en la base de datos
	///////////////////////////////////////////////////////
	public void loadTree() throws IOException{		

		listaPermisosPadre = permisoDAO.getRecuperaPadres();
		listaPermisosTodos = permisoDAO.getRecuperaTodos();	

		if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
			//capturar solo los menus
			List<SegMenu> listaMenu = new ArrayList<SegMenu>();
			for(SegMenu permiso : listaPermisosPadre) listaMenu.add(permiso);
			menu.appendChild(getTreechildren(listaMenu));

		}			

		listaPermisosPadre = null;
	}
	
	private Treechildren getTreechildren(List<SegMenu> listaMenu) {
		Treechildren retorno = new Treechildren();

		for(SegMenu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);

			retorno.appendChild(ti);
			List<SegMenu> listaPadreHijo = new ArrayList<SegMenu>();
			for(SegMenu permiso : listaPermisosTodos) {
				if(permiso.getId_menuPadre() == opcion.getIdMenu())
					listaPadreHijo.add(permiso);
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



		tc.setStyle("font-size: 0.82em;color:black;font: arial, helvetica, sans-serif;border-top-color: transparent;");

		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
			}
		});

		tr.appendChild(tc);


		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}	

	////////////////////////////////////////////////////////
	//recupera el combo con los perfiles
	///////////////////////////////////////////////////////

	public List<SegPerfil> getPerfilesLista() {
		return PerfilLista.getListaPerfiles("");
	}



	public SegPerfil getPerfilSelecionada() {
		return perfilSelecionada;
	}



	public void setPerfilSelecionada(SegPerfil perfilSelecionada) {
		this.perfilSelecionada = perfilSelecionada;
	}


	////////////////////////////////////////////////////////
	//recupera todo los menus segun el perfil seleccionado
	///////////////////////////////////////////////////////


	public SegMenu getOpcionSeleccionado() {
		return opcionSeleccionado;
	}


	public void setOpcionSeleccionado(SegMenu opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}


	public SegMenu getOpcionSeleccionado1() {
		return opcionSeleccionado1;
	}


	public void setOpcionSeleccionado1(SegMenu opcionSeleccionado1) {
		this.opcionSeleccionado1 = opcionSeleccionado1;
	}


	public SegPermiso getOpcionSeleccionadaP() {
		return opcionSeleccionadaP;
	}


	public void setOpcionSeleccionadaP(SegPermiso opcionSeleccionadaP) {
		this.opcionSeleccionadaP = opcionSeleccionadaP;
	}


	public void cargarTree() throws IOException{		
		  
		    System.out.println("ingresa a esta funcion jajaja pilas");
			listaPermisosPadre1 = permiso1DAO.getListaPermisosPadre(perfilSelecionada.getId_perfil());
			listaPermisosTodos1 = permiso1DAO.getListaPermisosTodos(perfilSelecionada.getId_perfil());
			
			
			if (listaPermisosPadre1.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<SegMenu> listaMenu = new ArrayList<SegMenu>();
				for(SegPermiso permiso : listaPermisosPadre1) listaMenu.add(permiso.getSegMenu());
				menu1.appendChild(getTreechildren1(listaMenu)); 
				
			}			
		
		listaPermisosPadre1 = null;
	}
	
	
	private Treechildren getTreechildren1(List<SegMenu> listaMenu) {
		Treechildren retorno = new Treechildren();
		
		for(SegMenu opcion : listaMenu) {
			Treeitem ti = getTreeitem1(opcion);
			
			retorno.appendChild(ti);
			List<SegMenu> listaPadreHijo = new ArrayList<SegMenu>();
			for(SegPermiso permiso : listaPermisosTodos1) {
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
	private Treeitem getTreeitem1(SegMenu opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());
		tc.setStyle("font-size: 0.82em;color:black;font: arial, helvetica, sans-serif;border-top-color: transparent;");
	
		//tc.setHeight("5px");
		//System.out.println("titulomenu: " + tc);
		
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
			
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}

	///////////////////////////////////////////////////
	/////////// añadir y eliminar /////////////////////
	//////////////////////////////////////////////////
	
	@Command("btn_Anadir")
	public void add() throws IOException {
		try {			
			//grabar la cabecera de factura 
			
			if(opcionSeleccionado == null){
				Clients.showNotification("Debe escoger una opción del menú");
				return;
			}
			
			if(perfilSelecionada == null){
				Clients.showNotification("Debe escoger Una Opción de perfil");
				return;
			}
			
			listaPermisos =  permiso1DAO.getListaPermisosSeleccionado(opcionSeleccionado.getIdMenu(), perfilSelecionada.getId_perfil());
			
			if (listaPermisos.size() > 0){
				Clients.showNotification("Opción del menú ya registrada");
				return;
			}
			
			controlingresotipo.setSegMenu(opcionSeleccionado);
			controlingresotipo.setSegPerfil(perfilSelecionada);
			controlingresotipo.setEstado("A");
			
			Sispermisodao.getEntityManager().getTransaction().begin();
			Sispermisodao.getEntityManager().persist(controlingresotipo);
			Sispermisodao.getEntityManager().getTransaction().commit();
	
			
			menu1.getChildren().clear();
		    cargarTree();
			
			controlingresotipo = new SegPermiso();
	
		} catch (Exception e) {
			//Si hay error, revierte la transaccion
			Sispermisodao.getEntityManager().getTransaction().rollback();
			Clients.showNotification("Ingreso Cancelado");
		}
	  
	}
	
	@Command("btn_Eliminar")
	public void delete(){	
		try {			
			//grabar la cabecera de factura 
		
		  // menu.getChildren().clear();
		   //loadTree();
		
			
			if(perfilSelecionada == null){
				Clients.showNotification("Debe escoger Una Opción de perfil");
				return;
			}
			
			
			if(opcionSeleccionado == null){
				Clients.showNotification("Debe escoger una opción del menú en Accesos");
				return;
			}
			
			lista =  permiso1DAO.getListaPermisosSeleccionado(opcionSeleccionado.getIdMenu(), perfilSelecionada.getId_perfil());
			if(lista.size() > 0){
				
			}else{
				Clients.showNotification("Debe escoger una opción del menú en Accesos");
				return;
			}
			
			for(SegPermiso det : lista) {
				det.setEstado("I");
				
				permiso1DAO.getEntityManager().getTransaction().begin();
				permiso1DAO.getEntityManager().persist(det);
				permiso1DAO.getEntityManager().getTransaction().commit();
				System.out.print("ACTUALIZADO SERVICIO");
			}		    
						
			menu1.getChildren().clear();
		    cargarTree();
			
		} catch (Exception e) {
			//Si hay error, revierte la transaccion
			permiso1DAO.getEntityManager().getTransaction().rollback();
			Clients.showNotification("Ingreso Cancelado");
		} 
	}
}
