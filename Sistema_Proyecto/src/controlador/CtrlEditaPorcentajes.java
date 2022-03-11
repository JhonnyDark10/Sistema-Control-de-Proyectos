package controlador;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import model.SegUsuario;
import model.SegUsuarioDAO;

import model.SisProyectoCategoria;
import model.SisProyectoCategoriaDAO;
import model.SisProyectoEstado;
import model.SisProyectoEstadoDAO;
import model.SisProyectoPropuesta;
import model.SisProyectoPropuestaDAO;
import model.SisProyectoTarea;
import model.SisProyectoTareaDAO;
import util.Validar;



@SuppressWarnings({ "serial", "rawtypes" })
public class CtrlEditaPorcentajes extends SelectorComposer {


	@Wire Textbox porcentaje;
	
	
	
	// Para poder Grabar
	SisProyectoTareaDAO personaDao = new SisProyectoTareaDAO();
	private SisProyectoTarea personaTarea;

	Ctrl_EditarEjecucion personaLista1;
	private List<SisProyectoPropuesta> porcentajeEdita;	
	private List<SisProyectoTarea> TareasAsignadas;
	SisProyectoTareaDAO tareasDao = new SisProyectoTareaDAO();
	private List<SisProyectoTarea> detallePropuesta;
	SisProyectoPropuestaDAO propuestaaDao = new SisProyectoPropuestaDAO();
	//CLick derecho source crear implementmetodo,  do after compose
	//Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
				
		personaLista1 = (Ctrl_EditarEjecucion)Executions.getCurrent().getArg().get("VentanaPadre");
		
		
		personaTarea=null;
		
		if(Executions.getCurrent().getArg().get("SisProyectoTarea")!=null){
			//Recupera Persona selecionada 
			personaTarea = (SisProyectoTarea)Executions.getCurrent().getArg().get("SisProyectoTarea");
			
			System.out.print("Entro Procentaje");
		}
		else{
			//Persona Nueva
			personaTarea = new SisProyectoTarea();
		}
		personaLista1.listarnuevamente();

	}


    
    
	@Listen("onClick=#grabar")
	public void grabar(){
		

		if(Integer.parseInt(porcentaje.getText()) < 101) {
			
		Messagebox.show("Desea Grabar la Información",
				"Mensaje", 
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {

					try {
					
						
						if(validarDatos() == true){
							
						}else{
							return;
						}
	
                            //editar el estado de la cabecera de E DE EJECUTADO
							detallePropuesta = personaDao.getEditaPorcentaje(personaTarea.getIdTarea());
							
							
							
							for(SisProyectoTarea det : detallePropuesta) {

								det.setPorcentajeTarea(Integer.parseInt(porcentaje.getText()));
                                
								
								personaDao.getEntityManager().getTransaction().begin();
								personaDao.getEntityManager().persist(det);
								personaDao.getEntityManager().getTransaction().commit();
								
								
							}
							
							personaLista1.listarnuevamente();
							
							Clients.showNotification("Registro Almacenado");
							salir();
							
							//actualizar
							
							actualizar_porcentaje();

					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						personaDao.getEntityManager().getTransaction().rollback();
					}

				}
			}
		});
		
		}
		
	
	}

	
	
	public void actualizar_porcentaje() {
		
		

		int acumula = 0;

		
		TareasAsignadas = tareasDao.getBuscarCabecera(personaLista1.variable_propuesta);
		for(SisProyectoTarea det : TareasAsignadas) {

			acumula = acumula  + det.getPorcentajeTarea();
			System.out.print("" + acumula);
			System.out.print(".");
		}

		//cargar y actualizar el porcentaje total del proyecto
       int total = 0;
        total = TareasAsignadas.size();
        System.out.print("***");
        System.out.print("" + acumula);
        System.out.print("-");
        System.out.print("" + total);
		//editar el estado de la cabecera de E y el PORCENTAJE
		porcentajeEdita = propuestaaDao.getListaProcentaje(personaLista1.variable_propuesta);
		for(SisProyectoPropuesta det : porcentajeEdita) {
			
			det.setPorcentajeTotal(acumula/total);
			propuestaaDao.getEntityManager().getTransaction().begin();
			propuestaaDao.getEntityManager().persist(det);
			propuestaaDao.getEntityManager().getTransaction().commit();
		}

		}

	

	//Boton salir
	@Wire Window winPorcentajes;

	@Listen("onClick=#salir")
	public void salir(){
		winPorcentajes.detach();

	}







	private boolean validarDatos() {
		try {


			if(porcentaje.getText() == "") {
				Clients.showNotification("Debe registrar porcentaje");
				return false;
			}
			
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}




	public SisProyectoTarea getPersonaTarea() {
		return personaTarea;
	}




	public void setPersonaTarea(SisProyectoTarea personaTarea) {
		this.personaTarea = personaTarea;
	}







}

