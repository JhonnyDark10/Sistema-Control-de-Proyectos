package controlador;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Ctrl_EditarEjecucionVisualiza extends SelectorComposer {


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

	public int variable_propuesta = 0;
	
	private List<SisProyectoPropuesta> detallePropuesta;

	private List<SisProyectoPropuesta> porcentajeEdita;
	
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

	Ctrl_ListaEjecucion personaLista;

	int id = 0;
	private List<SisProyectoTarea> TareasDescripcion;
	private SisProyectoTarea descripcionSelecionada;


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
		cbo_categoria.setDisabled(true);



		personaLista = (Ctrl_ListaEjecucion)Executions.getCurrent().getArg().get("VentanaPadre");


		if(Executions.getCurrent().getArg().get("SisProyectoPropuesta")!=null){
			//Recupera Persona selecionada 
			persona = (SisProyectoPropuesta)Executions.getCurrent().getArg().get("SisProyectoPropuesta");
		}

		variable_propuesta = persona.getIdProyectoPropuesta();
		listarnuevamente();
		
		
	}



	public void listarnuevamente() {

		if (TareasAsignadas != null) {
			TareasAsignadas = null; 
		}
		
		
		//System.out.print("aqui entro para listar nuevamente");
		
		
		TareasAsignadas = tareasDao.getBuscarCabecera(persona.getIdProyectoPropuesta());

		LstTipoTareas.setModel(new ListModelList(TareasAsignadas));

		descripcionSelecionada = null;
	}



	




	//Boton salir
	@Wire Window winEmpleados;

	@Listen("onClick=#salir")
	public void salir(){
		winEmpleados.detach();

	}


	@Listen("onClick=#addporcentaje")
	public void añadir() {


		if(descripcionSelecionada  == null) {

			Clients.showNotification("Debe seleccionar una tarea");
			return;
		}else {

			//Actualiza la instancia antes de editar
			tareasDao.getEntityManager().refresh(descripcionSelecionada);//berifica el ultimo valor en la BD

			//Enviar por parametros - para editar persona 
			Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
			params.put("SisProyectoTarea", descripcionSelecionada);
			params.put("VentanaPadre", this);

			Window ventanaCargar = (Window)Executions.createComponents("/A_Proyectos_Ejecucion/IngresaPorcentaje.zul", winEmpleados,params);
			ventanaCargar.doModal();




		}


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

		return usuariodao.getTodos("");

	}



	public SegUsuario getTipoSelecionada() {
		return TipoSelecionada;
	}



	public void setTipoSelecionada(SegUsuario tipoSelecionada) {
		TipoSelecionada = tipoSelecionada;
	}


}


