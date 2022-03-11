package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

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
public class Seg_ListaMenu extends SelectorComposer {


	//todo menu
	@Wire Tree menu;
	@Wire Include areaContenido;
	SegMenu opcionSeleccionado;

	List<SegMenu> lista;
	//todo menu
	SegMenuDAO permisoDAO = new SegMenuDAO();
	List<SegMenu> listaPermisosPadre = new ArrayList<SegMenu>();
	List<SegMenu> listaPermisosTodos = new ArrayList<SegMenu>();

	//Lamar venta 
	@Wire
	Window winPermisos;


	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);	
		loadTree();

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
				/* TODO Auto-generated method stub
				textnombre.setValue("");
				menupadre.setChecked(false);
				url.setChecked(false);*/

				opcionSeleccionado = opcion; 
				
				
				/*textDescripcion.setValue(opcionSeleccionado.getDescripcion());
				textPosicion.setValue(String.valueOf(opcionSeleccionado.getPosicion()));
				textUrl.setValue(opcionSeleccionado.getUrl());
				menuhijo.setChecked(false);
				if(opcionSeleccionado.getId_menuPadre() == 0){
					menupadre.setChecked(false);
					textnombre.setDisabled(true);
				}
				else{
					menupadre.setChecked(true);

					textnombre.setDisabled(false);
				}
				 */

				/*
				if(opcionSeleccionado.getUrlAsociado() == 0){
					url.setChecked(false);

					textUrl.setDisabled(true);
				}
				else{
					url.setChecked(true);
					textUrl.setDisabled(false);
				}
				 *//*
				lista = permisoDAO.getObtenerDescripcion(opcionSeleccionado.getId_menuPadre());	
				for(SegMenu det : lista) {
					textnombre.setValue(det.getDescripcion());  
				}*/
			}
		});

		tr.appendChild(tc);


		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}

	public SegMenu getOpcionSeleccionado() {
		return opcionSeleccionado;
	}

	public void setOpcionSeleccionado(SegMenu opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}	


	//*******************************************
	//Escucha evento
	@Command("btnNuevo")
	public void nuevo(){

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SegMenu", null);
		params.put("VentanaPadre", this);


		Window ventanaCargar = (Window)Executions.createComponents("/A_ModuloSeguridad/Menu.zul", winPermisos,params);
		ventanaCargar.doModal();
	}


	//*******************************************
	//Escucha evento
	@Command("btnEditar")
	public void editar(){

		if(opcionSeleccionado == null){
			Clients.showNotification("Debe seleccionar un Registro");
			return;
		}
		//Actualiza la instancia antes de editar
		permisoDAO.getEntityManager().refresh(opcionSeleccionado);//berifica el ultimo valor en la BD

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SegMenu", opcionSeleccionado);
		params.put("VentanaPadre", this);

		Window ventanaCargar = (Window)Executions.createComponents("/A_ModuloSeguridad/Menu.zul", winPermisos,params);
		ventanaCargar.doModal();
		
	}



	@Command("btnEliminar")
	public void delete(){	
		if (opcionSeleccionado == null){
			Clients.showNotification("Debe selecionar un registro");
			return;
		}
		Messagebox.show("Desea Eliminar el Registro Seleccionado","Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {


					try {			
						//grabar la cabecera de factura 		
						opcionSeleccionado.setEstado("I");

						permisoDAO.getEntityManager().getTransaction().begin();
						permisoDAO.getEntityManager().persist(opcionSeleccionado);
						permisoDAO.getEntityManager().getTransaction().commit();
						
						
						Clients.showNotification("Registro Eliminado");
                        
						opcionSeleccionado = null;
						menu.getChildren().clear();
						loadTree();
						
					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						permisoDAO.getEntityManager().getTransaction().rollback();
						Clients.showNotification("Ingreso Cancelado");
						
					}
				}


			}
		});


	}




}
