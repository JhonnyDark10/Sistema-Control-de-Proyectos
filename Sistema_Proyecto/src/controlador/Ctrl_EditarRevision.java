package controlador;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.zkoss.zul.Filedownload;
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

import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
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
public class Ctrl_EditarRevision extends SelectorComposer {


	@Wire Textbox calificacion,descripcionTarea,ValorTarea,txt_cedula,nombre,autor,correo,telefono,observaciones;
	@Wire Label numero;
	@Wire private Combobox cbo_estado,cbo_categoria,cbo_encargado;
	@Wire Listbox LstTipoTareas;
	@Wire Tabbox tb1;
	@Wire Grid datos;
	@Wire Datebox fechaInicio,fechaFin;
	Validar valida = new Validar();

    @Wire Button sumar1;

	private SegUsuario TipoSelecionada;
	// Instancia el objeto para acceso a datos.
		private SegPeriodoEncargoDAO usuarioDao = new SegPeriodoEncargoDAO();


		 public Integer codigoPL ;

			private List<SegPeriodoEncargo> UsuariosL;
			

		private List<SegPeriodoEncargo> Usuarios;

	
	private List<SisProyectoPropuesta> detallePropuesta;
	private List<SisProyectoPropuesta> detallePropuestaValor;

	//para llenar los combos
	private SisProyectoCategoriaDAO categoriadao = new SisProyectoCategoriaDAO();
	private SisProyectoEstadoDAO estadodao = new SisProyectoEstadoDAO();

	private SegUsuarioDAO usuariodao = new SegUsuarioDAO();

	// Para poder Grabar
	SisProyectoPropuestaDAO personaDao = new SisProyectoPropuestaDAO();
	private SisProyectoPropuesta persona;

	
	
	
	
	//ppara grabar detalles de tareas
	SisProyectoTareaDAO tareasDao = new SisProyectoTareaDAO();
	private SisProyectoTarea tareas;

	private List<SisProyectoTarea> TareasAsignadas;
	private List<SisProyectoTarea> verificaTotalCosto;

	Date date = new Date();

	Ctrl_ListaRevision personaLista;


	private List<SisProyectoTarea> TareasDescripcion;
	private SisProyectoTarea descripcionSelecionada;

	int cont = 0;

	ListModelList hardware = new ListModelList();


	//CLick derecho source crear implementmetodo,  do after compose
	//Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		txt_cedula.setReadonly(true);
	
		nombre.setReadonly(true);
		autor.setReadonly(true);
		correo.setReadonly(true);
		telefono.setReadonly(true);
		cbo_estado.setDisabled(true);
		cbo_categoria.setDisabled(true);
		//observaciones.setReadonly(true);
		observaciones.setDisabled(false);
		
		
		fechaInicio.setVisible(false);
		fechaFin.setVisible(false);
		cbo_encargado.setVisible(false);
		
		personaLista = (Ctrl_ListaRevision)Executions.getCurrent().getArg().get("VentanaPadre");
		
		
		if(Executions.getCurrent().getArg().get("SisProyectoPropuesta")!=null){
			//Recupera Persona selecionada 
			persona = (SisProyectoPropuesta)Executions.getCurrent().getArg().get("SisProyectoPropuesta");
		}
		
		listar();
	}



    public void listar() {
    	
    	TareasAsignadas = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

		LstTipoTareas.setModel(new ListModelList(TareasAsignadas));
    }

    

    
	@Listen("onClick=#sumar1")
	public void insertar_calificacion_propuesta(){
		
		
		if(calificacion.getText() == "") {
			
			//Clients.showNotification("Debe ingresar la calificacion entre 0 -100 - Observaciones del proyecto");
			Clients.showNotification("Alguna observacion sobre el proyecto e ingrese calificación");
			
			
		}else {
			
			if(Integer.parseInt(calificacion.getText()) < 70) {
				
				Clients.showNotification("El proyecto a sido rechazado por baja calificación");
				
				 //editar el estado de la cabecera de I por A y el costo total
				detallePropuesta = personaDao.getListaRevision(Integer.parseInt(numero.getValue()));
				
				//System.out.print(detallePropuesta.size());
				
				for(SisProyectoPropuesta det : detallePropuesta) {
					det.setObservacion(observaciones.getValue());

					det.setEstadoProyecto("R");
                    det.setCalificacionGeneral(Integer.parseInt(calificacion.getText()));
					
                    personaDao.getEntityManager().getTransaction().begin();
					personaDao.getEntityManager().persist(det);
					personaDao.getEntityManager().getTransaction().commit();
				}
				
				salir();
				personaLista.buscar();
				
				
				
			}else { 
				
				if(Integer.parseInt(calificacion.getText()) >=70  && Integer.parseInt(calificacion.getText()) <= 100) {	
					Clients.showNotification("El proyecto a sido aceptado por buena calificación");
				//validar si existe presupuesto en la caja del presidente asignado
				
				
				//obtengo valor del usuario
				Usuarios = usuarioDao.getListaPerfiles("");
				float valorA = 0;
				
				for(SegPeriodoEncargo eng : Usuarios) {

					
                    valorA = eng.getValorIngreso();
					
				}
				
               
				//obtengo valor del total presupuesto
				detallePropuestaValor = personaDao.getListaRevision(Integer.parseInt(numero.getValue()));
				
				float valorT = 0;
				
				for(SisProyectoPropuesta det : detallePropuestaValor) {

					
					valorT = det.getValorTotal();
				
				}
				
				//verifico para ver si procede
				if(valorA > valorT ) {
					Clients.showNotification("El proyecto a sido aceptado  y si hay dinero para la ejecucion");
					
					//habilitar mas opciones 
					
					calificacion.setVisible(false);
					sumar1.setVisible(true);
					
					//campos para almacenar variable
					fechaInicio.setVisible(true);
					fechaFin.setVisible(true);
					cbo_encargado.setVisible(true);
					
					
					 //actualizo la calificacion 
					
					detallePropuesta = personaDao.getListaRevision(Integer.parseInt(numero.getValue()));
										
					for(SisProyectoPropuesta det : detallePropuesta) {

						
	                    det.setCalificacionGeneral(Integer.parseInt(calificacion.getText()));
						
	                    personaDao.getEntityManager().getTransaction().begin();
						personaDao.getEntityManager().persist(det);
						personaDao.getEntityManager().getTransaction().commit();
					}
					
					
					//actualizo la resta de el valor de caja por valor del proyecto y actualizo el total 
					
					for(SegPeriodoEncargo eng : Usuarios) {

						
	                    eng.setValorIngreso(valorA - valorT); 
						
	                    usuarioDao.getEntityManager().getTransaction().begin();
	                    usuarioDao.getEntityManager().persist(eng);
	                    usuarioDao.getEntityManager().getTransaction().commit();
					}
					
					
					
					
					
					
				}else{
					Clients.showNotification("El presupuesto del proyecto excede el valor de caja");
					return;
				}
				
				
				//hasta aqui
			}
			}
		}
		
	}
    
    
	
	public void buscarl(){
		//System.out.println("INGRESO");

		if (UsuariosL != null) {
			UsuariosL = null; 
		}

		UsuariosL = usuarioDao.getListaPerfiles("");

		for(SegPeriodoEncargo det: UsuariosL) {
			codigoPL = det.getIdPeriodo();
		}
		
		
		
		
	}

	
	
	
	
	@Listen("onClick=#grabar")
	public void grabar(){

		Messagebox.show("Desea Grabar la Información",
				"Mensaje", 
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {

					try {
					
						if(fechaInicio.getText() == "") {
							Clients.showNotification("Primero debe asignar calificación");
							return;
						}else {
                            //editar el estado de la cabecera de E DE EJECUTADO
							detallePropuesta = personaDao.getListaRevision(Integer.parseInt(numero.getValue()));
							
							//validar que me alamcene el id del presidente 
							buscarl();
							
							for(SisProyectoPropuesta det : detallePropuesta) {

								det.setEstadoProyecto("E");
                                det.setFechaInicio(fechaInicio.getValue());
                                det.setFechaFin(fechaFin.getValue());
                                det.setSegUsuario(TipoSelecionada);
                                det.setUsuario(codigoPL);
                                det.setObservacion(observaciones.getValue());
								personaDao.getEntityManager().getTransaction().begin();
								personaDao.getEntityManager().persist(det);
								personaDao.getEntityManager().getTransaction().commit();
								
								
							}

							Clients.showNotification("Registro Almacenado");
							salir();
							personaLista.buscar();
						
						}



					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						personaDao.getEntityManager().getTransaction().rollback();
					}

				}
			}
		});
	}


	//Boton salir
	@Wire Window winEmpleados;

	@Listen("onClick=#salir")
	public void salir(){
		winEmpleados.detach();

	}




	
	public SisProyectoPropuesta getPersona() {
		return persona;
	}





	public void setPersona(SisProyectoPropuesta persona) {
		this.persona = persona;
	}





	//llenar combos
	public List<SisProyectoCategoria> getCategoriaProyectos(){

		return categoriadao.getTipoCategoria("");

	}

	public List<SisProyectoEstado> getEstadoProyectos(){

		return estadodao.getTipoEstado("");

	}



	private boolean validarDatos() {
		try {


			if(txt_cedula.getText() == "") {
				Clients.showNotification("Debe registrar cédula");
				return false;
			}
			if(nombre.getText() == "") {
				Clients.showNotification("Debe registrar nombre");
				return false;
			}

			if(autor.getText() == "") {
				Clients.showNotification("Debe registrar autor");
				return false;
			}
			if(correo.getText() == "") {
				Clients.showNotification("Debe registrar correo");
				return false;
			}

			if(telefono.getText() == "") {
				Clients.showNotification("Debe registrar teléfono");
				return false;
			}
			if(cbo_categoria.getSelectedIndex() == -1) {
				Clients.showNotification("Debe selecionar categoria");
				return false;
			}


			if(cbo_estado.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar estado");
				return false;
			}

			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}






	public List<SisProyectoTarea> getTareasDescripcion() {
		return TareasDescripcion;
	}






	public void setTareasDescripcion(List<SisProyectoTarea> tareasDescripcion) {
		TareasDescripcion = tareasDescripcion;
	}






	public SisProyectoTarea getDescripcionSelecionada() {
		return descripcionSelecionada;
	}






	public void setDescripcionSelecionada(SisProyectoTarea descripcionSelecionada) {
		this.descripcionSelecionada = descripcionSelecionada;
	}






	public List<SisProyectoTarea> getTareasAsignadas() {
		return TareasAsignadas;
	}






	public void setTareasAsignadas(List<SisProyectoTarea> tareasAsignadas) {
		TareasAsignadas = tareasAsignadas;
	}


	
	//llenar combos
		public List<SegUsuario> getLlenarUsuarios(){

			return usuariodao.getSinAmd();

		}



		public SegUsuario getTipoSelecionada() {
			return TipoSelecionada;
		}



		public void setTipoSelecionada(SegUsuario tipoSelecionada) {
			TipoSelecionada = tipoSelecionada;
		}

		
		
		//Funcion de descarga
		
		@Listen("onClick=#btnDescargar")
		public void descargar() throws FileNotFoundException {
			if(persona != null) {
				if(persona.getDocumento() != null) {
					Filedownload.save(new File(persona.getDocumento()), "pdf"); 
				}
			}
		}

}

