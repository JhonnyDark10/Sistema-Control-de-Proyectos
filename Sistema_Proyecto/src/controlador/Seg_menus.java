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
public class Seg_menus extends SelectorComposer {

	@Wire Textbox textDescripcion,textPosicion,textUrl,textnombre;
	@Wire Checkbox menupadre,url,menuhijo;
	@Wire Label numero;
	//todo menu
	
	@Wire Include areaContenido;
	SegMenu opcionSeleccionado;

	List<SegMenu> lista;
	List<SegMenu> listaHijo;
	//todo menu
	SegMenuDAO permisoDAO = new SegMenuDAO();
	List<SegMenu> listaPermisosPadre = new ArrayList<SegMenu>();
	List<SegMenu> listaPermisosTodos = new ArrayList<SegMenu>();

	//para guardar y eliminar 
	private SegMenu controlingresotipo;

	private Seg_ListaMenu personaLista;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);	
		
		
		
		controlingresotipo = null;

		personaLista = (Seg_ListaMenu)Executions.getCurrent().getArg().get("VentanaPadre");

		
		if(Executions.getCurrent().getArg().get("SegMenu")!=null){
			//Recupera Persona selecionada 
			controlingresotipo = (SegMenu)Executions.getCurrent().getArg().get("SegMenu");
			
			textDescripcion.setValue(controlingresotipo.getDescripcion());
			textPosicion.setValue(String.valueOf(controlingresotipo.getPosicion()));
			
			//padres
			if(controlingresotipo.getId_menuPadre() == 0){
				menupadre.setChecked(true);
				menupadre.setDisabled(true);
				textnombre.setDisabled(true);
				menuhijo.setDisabled(true);
				url.setDisabled(true);
			}
			else{
				menupadre.setDisabled(true);
				menupadre.setChecked(false);
               	textnombre.setDisabled(true);
               	menuhijo.setDisabled(true);
               	menuhijo.setChecked(true);
               	url.setDisabled(true);
               	url.setChecked(true);
			}
			//url
			if(controlingresotipo.getUrlAsociado() == 0){
				url.setChecked(false);

				textUrl.setDisabled(true);
			}
			else{
				url.setChecked(true);
				textUrl.setDisabled(false);
				textUrl.setValue(controlingresotipo.getUrl());
			}
			//recupera padre
			lista = permisoDAO.getObtenerDescripcion(controlingresotipo.getId_menuPadre());	
			for(SegMenu det : lista) {
				textnombre.setValue(det.getDescripcion());  
			}
			url.setDisabled(true);
			menuhijo.setDisabled(true);
			menupadre.setDisabled(true);
		}else{
					
			controlingresotipo = new SegMenu();

		}
	}

	public SegMenu getOpcionSeleccionado() {
		return opcionSeleccionado;
	}

	public void setOpcionSeleccionado(SegMenu opcionSeleccionado) {
		this.opcionSeleccionado = opcionSeleccionado;
	}	


	@Command("menupadre")
	public void recupera() throws IOException {
		
		if(menupadre.isChecked() == true){
			
			menuhijo.setDisabled(true);
			menuhijo.setChecked(false);
			textnombre.setDisabled(true);
			textnombre.setValue("");
			url.setDisabled(true);
			url.setChecked(false);
			textUrl.setDisabled(true);
			textUrl.setValue("");
		}else{
			
						
			menuhijo.setDisabled(false);
			menuhijo.setChecked(false);
			textnombre.setValue("");
			url.setDisabled(false);
			url.setChecked(false);
			textUrl.setDisabled(false);
			textUrl.setValue("");
		}
	}
	
	
	
	@Command("menuhijo")
	public void jajajaja() throws IOException {
		
		if(personaLista.opcionSeleccionado == null){
			Clients.showNotification("seleccione un registro");
		
			winTipoActividades.detach();
			return;
		}
		
		
		if(menuhijo.isChecked() == true	){
			
			menupadre.setDisabled(true);
			textnombre.setDisabled(true);
			
			listaHijo = permisoDAO.getObtenerDescripcion(Integer.valueOf(numero.getValue()));	
			for(SegMenu det : listaHijo) {
				textnombre.setValue(det.getDescripcion());  
			}
			
			url.setChecked(true);
			textDescripcion.setValue("");
			textPosicion.setValue("");
			textUrl.setValue("");		

		}else{
			url.setChecked(false);
			textnombre.setDisabled(true);
			textnombre.setValue("");
			menupadre.setDisabled(false);
			
		}
	}

	
	//Boton salir
	@Wire Window winTipoActividades;

	@Command("salir")
	public void salir(){
		winTipoActividades.detach();

	}
	
    //nuevo
	public void nuevo() throws IOException {
		controlingresotipo = null;
		menupadre.setChecked(false);
		url.setChecked(false);
		textDescripcion.setValue("");
		textPosicion.setValue("");
		textUrl.setValue("");
		textnombre.setValue("");
		menuhijo.setChecked(false);
	}
	
	//grabar y editar los menus
	@Command("grabar")
	public void grabar(){

		Messagebox.show("Desea Grabar la Información",
				"Mensaje", 
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {

					try {		
						//
						permisoDAO.getEntityManager().getTransaction().begin();
						//Almacena Datos
						if(validarDatos() == true){
							if(controlingresotipo.getIdMenu() == 0){
								//Si es nuevo "persist"
								List<SegMenu> personaListar = permisoDAO.getRecuperaTodos();
								if (personaListar.size() > 0){
									
									for(SegMenu det : personaListar ){
										
										if (det.getDescripcion() == textDescripcion.getText()){
											Clients.showNotification("Nombre ya registrado");
											return;
										}
									}
								
								}
								
								//verificar para guarar hijo o padre
								//grabar la cabecera de factura 

								if(menuhijo.isChecked() ==  true){

									controlingresotipo.setId_menuPadre(Integer.valueOf(numero.getValue()));
									controlingresotipo.setIcono("A_Iconos/file.gif");
									controlingresotipo.setDescripcion(textDescripcion.getValue());
									controlingresotipo.setPosicion(Integer.parseInt(textPosicion.getValue()));
									controlingresotipo.setUrl(textUrl.getValue());
								}else{
									controlingresotipo.setId_menuPadre(0);
									controlingresotipo.setIcono("A_Iconos/carp.gif");
									controlingresotipo.setDescripcion(textDescripcion.getValue());
									controlingresotipo.setPosicion(Integer.parseInt(textPosicion.getValue()));
									controlingresotipo.setUrl("");

								}
								

								if(url.isChecked() == true){

									controlingresotipo.setUrlAsociado(1);
								}else{
									controlingresotipo.setUrlAsociado(0);
								}
								controlingresotipo.setEstado("A");
								
								
								permisoDAO.getEntityManager().persist(controlingresotipo);
							}else{
								//*******************
								//los datos persisten 
								//*******************
								controlingresotipo = (SegMenu) permisoDAO.getEntityManager().merge(controlingresotipo);
							
								if(menuhijo.isChecked() ==  true){
									controlingresotipo.setDescripcion(textDescripcion.getValue());
									controlingresotipo.setPosicion(Integer.parseInt(textPosicion.getValue()));
									if(url.isChecked() == true){

										controlingresotipo.setUrl(textUrl.getValue());
										controlingresotipo.setUrlAsociado(1);
									}else{
										controlingresotipo.setUrlAsociado(0);
									}
								
								}else{
									controlingresotipo.setDescripcion(textDescripcion.getValue());
									controlingresotipo.setPosicion(Integer.parseInt(textPosicion.getValue()));
								}
							
							}
						}else{
							return;
						}
						//cierra la transaccion
						permisoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Registro Almacenado");
						salir();
						personaLista.opcionSeleccionado = null;
						personaLista.menu.getChildren().clear();
						personaLista.loadTree();
					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						permisoDAO.getEntityManager().getTransaction().rollback();
					}

				}
			}
		});
	}

	//validar datos
	


	private boolean validarDatos() {
		try {

			if(menupadre.isChecked() == true) {
				if(textDescripcion.getText() == ""){
					Clients.showNotification("Debe registrar descripción");
					return false;
				}
				if(textPosicion.getText() == ""){
					Clients.showNotification("Debe registrar posición");
					return false;
				}
			}
			
			if(menuhijo.isChecked() == true){
				if(textDescripcion.getText() == ""){
					Clients.showNotification("Debe registrar descripción");
					return false;
				}
				if(textPosicion.getText() == ""){
					Clients.showNotification("Debe registrar posición");
					return false;
				}
				if(textUrl.getText() == ""){
					Clients.showNotification("Debe registrar url");
					return false;
				}
			}
		
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
}
