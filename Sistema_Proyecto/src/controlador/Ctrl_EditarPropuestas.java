package controlador;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.annotation.Command;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
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
public class Ctrl_EditarPropuestas extends SelectorComposer {


	@Wire Textbox descripcionTarea,ValorTarea,txt_cedula,nombre,autor,correo,telefono,observaciones,nom_doc;
	@Wire private Combobox cbo_estado,cbo_categoria;
	@Wire Listbox LstTipoTareas;
	
	
	Validar valida = new Validar();


	private List<SisProyectoPropuesta> ListaRecuperaDatos;
	private List<SisProyectoPropuesta> detallePropuesta;

	//para llenar los combos
	private SisProyectoCategoriaDAO categoriadao = new SisProyectoCategoriaDAO();
	private SisProyectoEstadoDAO estadodao = new SisProyectoEstadoDAO();
	private SisProyectoEstadoDAO estadopropuestadao = new SisProyectoEstadoDAO();

	// Para poder Grabar
	SisProyectoPropuestaDAO personaDao = new SisProyectoPropuestaDAO();
	private SisProyectoPropuesta persona;

	//ppara grabar detalles de tareas
	SisProyectoTareaDAO tareasDao = new SisProyectoTareaDAO();
	private SisProyectoTarea tareas;

	private List<SisProyectoTarea> TareasAsignadas;
	private List<SisProyectoTarea> verificaTotalCosto;

	Date date = new Date();

	Ctrl_ListaPropuestas personaLista;
	private SegPeriodoEncargoDAO usuarioDao = new SegPeriodoEncargoDAO();
	private List<SegPeriodoEncargo> UsuariosL;
	private List<SisProyectoTarea> TareasDescripcion;
	private SisProyectoTarea descripcionSelecionada;

	int cont = 0;

	ListModelList hardware = new ListModelList();


	public Integer codigoGP; //CODIGO GUARDAR PERIODO ;
	
	//CLick derecho source crear implementmetodo,  do after compose
	//Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		personaLista = (Ctrl_ListaPropuestas)Executions.getCurrent().getArg().get("VentanaPadre");
		persona = new SisProyectoPropuesta();

		tareas = new SisProyectoTarea();
		hardware = new ListModelList();
		LstTipoTareas.setModel(hardware);
		observaciones.setDisabled(true);
			}






	@Listen("onClick=#grabar")
	public void grabar(){

		Messagebox.show("Desea Grabar la Información Propuesta de Proyecto",
				"Mensaje", 
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {

					try {
						
						//valido que no existan datos vacios
						if(validarDatos() == true){

							//buscar todas las tareas y obtener la suma total
							float acumular = 0;
							verificaTotalCosto = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

							for(SisProyectoTarea det : verificaTotalCosto) {

								acumular = acumular + det.getCostoTarea();

							}


							//editar el estado de la cabecera de I por A y el costo total
							detallePropuesta = personaDao.getListaPropuestaSeleccionada(persona.getIdProyectoPropuesta());
							for(SisProyectoPropuesta det : detallePropuesta) {
								
								det.setAutor(autor.getValue());
								det.setCorreo(correo.getValue());
								det.setTelefono(telefono.getValue());
								//
								det.setEstadoProyecto("A");
								det.setValorTotal(acumular);
								personaDao.getEntityManager().getTransaction().begin();
								personaDao.getEntityManager().persist(det);
								personaDao.getEntityManager().getTransaction().commit();
							}
						}else {
							return;
						}



						Clients.showNotification("Registro Almacenado");
						salir();
						personaLista.buscar();

					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						personaDao.getEntityManager().getTransaction().rollback();
					}

				}
			}
		});
	}


	
	
	
	///RECUPERA EL ID DEL PERIODO ACTIVO
	public void buscarPERIODO(){
		//System.out.println("INGRESO");

		if (UsuariosL != null) {
			UsuariosL = null; 
		}

		UsuariosL = usuarioDao.getListaPerfiles("");

		for(SegPeriodoEncargo det: UsuariosL) {
			codigoGP = det.getIdPeriodo();
		}
		
		
		
		
	}

	
	
	//Boton salir
	@Wire Window winEmpleados;

	@Listen("onClick=#salir")
	public void salir(){
		winEmpleados.detach();

	}




	@Listen("onClick=#sumar")
	public void agregar(){

		if(cont == 0) {
			try {
				personaDao.getEntityManager().getTransaction().begin();

				if(validarDatos() == true){

					//aqui se guarda el di del perodo activo
					
					buscarPERIODO();
					
					
					
					System.out.print("aqui entro ");
					//alamcena datos de propuesta 
					persona.setUsuario(codigoGP);
					persona.setEstadoProyecto("I");                           
					persona.setFechaRegistro(date);
					persona.setPorcentajeTotal(0);
					persona.setValorTotal(0);
								
					persona.setDocumento(util.FileUtil.cargaArchivo(media));
					personaDao.getEntityManager().persist(persona);
					personaDao.getEntityManager().getTransaction().commit();
					Clients.showNotification("Registro Almacenado");			
				}else{
					System.out.print("anegativo entro ");
					return;
				}
				//Clients.showNotification("Registro Almacenado");

				tareasDao.getEntityManager().getTransaction().begin();
				tareas.setCostoTarea(Float.parseFloat(ValorTarea.getText()));
				tareas.setNombreTarea(descripcionTarea.getText());
				tareas.setEstadoTarea("A");
				tareas.setPorcentajeTarea(0);
				tareas.setSisProyectoPropuesta(persona);	

				tareasDao.getEntityManager().persist(tareas);
				tareasDao.getEntityManager().getTransaction().commit();

				tareas = new SisProyectoTarea();

				cont = 1;
				ValorTarea.setText("");
				descripcionTarea.setText("");
				//listar las tareas
				TareasAsignadas = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

				LstTipoTareas.setModel(new ListModelList(TareasAsignadas));

				//Limpiar
				descripcionSelecionada = null;


			} catch (Exception e) {
				//Si hay error, revierte la transaccion
				personaDao.getEntityManager().getTransaction().rollback();
			}
		}else
		{
			//almacenar datos de los detalles de tareas
			tareasDao.getEntityManager().getTransaction().begin();
			tareas.setCostoTarea(Float.parseFloat(ValorTarea.getText()));
			tareas.setNombreTarea(descripcionTarea.getText());
			tareas.setEstadoTarea("A");
			tareas.setPorcentajeTarea(0);
			tareas.setSisProyectoPropuesta(persona);	

			tareasDao.getEntityManager().persist(tareas);
			tareasDao.getEntityManager().getTransaction().commit();

			tareas = new SisProyectoTarea();
			ValorTarea.setText("");
			descripcionTarea.setText("");
			//aqui lista

			TareasAsignadas = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

			LstTipoTareas.setModel(new ListModelList(TareasAsignadas));

			//Limpiar
			descripcionSelecionada = null;

		}
	}

	@Listen("onClick=#restar")
	public void restar(){

		if (descripcionSelecionada == null) {
			Clients.showNotification(" Debe seleccionar un Registro");
			return;
		}
		Messagebox.show("Desea Eliminar el Registro Seleccionado","Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						//personaSeleccionada.setEstado("X");
						descripcionSelecionada.setEstadoTarea("I");;
						tareasDao.getEntityManager().getTransaction().begin();
						tareasDao.getEntityManager().persist(descripcionSelecionada);
						tareasDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Tarea Eliminada");


						TareasAsignadas = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

						LstTipoTareas.setModel(new ListModelList(TareasAsignadas));

						//Limpiar
						descripcionSelecionada = null;
					} catch (Exception e) {
						e.printStackTrace();
						tareasDao.getEntityManager().getTransaction().rollback();
					}
				}


			}
		});

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

	
	public List<SisProyectoEstado> 	getTipoEstadoPropuesta(){

		return estadopropuestadao.getTipoEstadoPropuesta();

	}



	
	private boolean validarDatos() {
		try {
				
			if(validarDatoscedula() == false){
				Clients.showNotification("cedula inconrrecta");
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
	
	
	
		//txt agregar
	@Listen("onOK=#txt_cedula")
	public void RecuperaCedula(){
		
			
		ListaRecuperaDatos = personaDao.getRecuperaCedulaTabla(txt_cedula.getText());		
		
		if (ListaRecuperaDatos.size() > 0) {
			
			//recupero datos
			for(SisProyectoPropuesta ced : ListaRecuperaDatos) {

				autor.setText(ced.getAutor());
				
				correo.setText(ced.getCorreo());
				telefono.setText(ced.getTelefono());
			
				Clients.showNotification("Datos recuperados");
			   return;
				
			}
			
		}else {
			//limpia los datos
			
			Clients.showNotification("Usuario no a registrado ninguna propuesta");
			//autor.setText("");
			//nombre.setText("");
			//correo.setText("");
			//telefono.setText("");
			//observaciones.setText("");
		}
		
		
	}
	
	private boolean validarDatoscedula() {
		try {

						
			if(valida.validarDeCedula(txt_cedula.getText()) == false) {
				Clients.showNotification("Cédula no valida");
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	
	//
		//subir documento
	//
	Media media;
	
	@Listen("onUpload = button#subir")
	public void onUpload(UploadEvent event) {
	    try {
	        System.out.println("before upload " + event.getMedia().getName());
	        media = event.getMedia();
	     
	        nom_doc.setValue(media.getName());
	        
	        
			System.out.println(media);
		
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	        Messagebox.show("Upload failed");
	    }
	}
	
	
	//

	//
	
	
}
