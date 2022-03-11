package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import model.SisProyectoPropuesta;
import model.SisProyectoPropuestaDAO;


@SuppressWarnings({ "serial", "rawtypes" })
public class Ctrl_ListaEjecucion extends SelectorComposer{

	@Wire 
	Textbox txtBuscar;

	@Wire 
	private Listbox LstEmpleados;

	//Lamar venta 
	@Wire
	Window winListaEmpleados;


	// Instancia el objeto para acceso a datos.
	private SisProyectoPropuestaDAO EmpleadosDao = new SisProyectoPropuestaDAO();



	private List<SisProyectoPropuesta> Empleados;


	//aqui para seleccionar el objeto  a añadir o editar
	private SisProyectoPropuesta empleadoSelecionada;


	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		buscar();
	}


	

	@Listen("onChanging=#txtBuscar;onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		//System.out.println("INGRESO");

		if (Empleados != null) {
			Empleados = null; 
		}

		Empleados = EmpleadosDao.getListaProyectoEjecutar(txtBuscar.getValue());

		LstEmpleados.setModel(new ListModelList(Empleados));

		//Limpiar
		empleadoSelecionada = null;
	}

	//*******************************************
	//Escucha evento
	@Listen("onClick=#btnNuevo")
	public void editar(){

		if(empleadoSelecionada == null){
			Clients.showNotification("Debe seleccionar un Registro");
			return;
		}
		//Actualiza la instancia antes de editar
		EmpleadosDao.getEntityManager().refresh(empleadoSelecionada);//berifica el ultimo valor en la BD

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SisProyectoPropuesta", empleadoSelecionada);
		params.put("VentanaPadre", this);

		Window ventanaCargar = (Window)Executions.createComponents("/A_Proyectos_Ejecucion/IngresaEjecucion.zul", winListaEmpleados,params);
		ventanaCargar.doModal();
	}
	
	//Escucha evento
			@Listen("onClick=#btnVisualiza")
			public void btnVisualizaTareas(){
				if(empleadoSelecionada == null){
					Clients.showNotification("Debe seleccionar un Registro");
					return;
				}
				//Actualiza la instancia antes de editar
				EmpleadosDao.getEntityManager().refresh(empleadoSelecionada);//berifica el ultimo valor en la BD

				//Enviar por parametros - para editar persona 
				Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
				params.put("SisProyectoPropuesta", empleadoSelecionada);
				params.put("VentanaPadre", this);

				Window ventanaCargar = (Window)Executions.createComponents("/A_Proyectos_Seguimiento/Visualiza.zul", winListaEmpleados,params);
				ventanaCargar.doModal();
			}
			
			
	
	
	//Escucha evento
		@Listen("onClick=#btnActualizaR")
		public void terminarProyecto(){
			
			if (empleadoSelecionada == null) {
				Clients.showNotification(" Debe seleccionar un Registro");
				return;
			}
			Messagebox.show("Desea Dar por terminado el Proyecto","Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (empleadoSelecionada.getPorcentajeTotal() == 100 ) {
						
						
						
					if (event.getName().equals("onYes")) {
						try {
							
							empleadoSelecionada.setEstadoProyecto("T");;
							EmpleadosDao.getEntityManager().getTransaction().begin();
							EmpleadosDao.getEntityManager().persist(empleadoSelecionada);
							EmpleadosDao.getEntityManager().getTransaction().commit();;
							Clients.showNotification("Proyecto Concluido");
							buscar();
						} catch (Exception e) {
							e.printStackTrace();
							EmpleadosDao.getEntityManager().getTransaction().rollback();
						}
					}

					}
					else {
						Clients.showNotification("El proyecto debe estar al 100%");
					}
						
				}
			});
			
		}
		
		
		

	public List<SisProyectoPropuesta> getEmpleados() {
		return Empleados;
	}


	public void setEmpleados(List<SisProyectoPropuesta> empleados) {
		Empleados = empleados;
	}


	public SisProyectoPropuesta getEmpleadoSelecionada() {
		return empleadoSelecionada;
	}


	public void setEmpleadoSelecionada(SisProyectoPropuesta empleadoSelecionada) {
		this.empleadoSelecionada = empleadoSelecionada;
	}

}       

