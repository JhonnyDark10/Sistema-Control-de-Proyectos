package controlador;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import model.SegPerfil;
import model.SegPerfilDAO;
import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
import model.SegUsuario;
import model.SegUsuarioDAO;

import util.RecuperarClave;



@SuppressWarnings({ "serial", "rawtypes" })
public class Ctrl_EditarPresidentes extends SelectorComposer {

	@Wire Textbox usuario,clave,recupera_nombre;
	@Wire Label numero;
	@Wire Combobox combo_empleado,combo_perfil;
	@Wire Label nombres;
	
	Menu controlador;
	//private DepartamentoDao paisDao = new DepartamentoDao();
	private SegUsuarioDAO empleadodao = new SegUsuarioDAO();
	

	// Para poder Grabar
	SegPeriodoEncargoDAO personaDao = new SegPeriodoEncargoDAO();
	private SegPeriodoEncargo persona;

	//COntiene la ventana padre para invocar a la actualizacion
	private Ctrl_ListaPresidentes personaLista;


	private List<SegUsuario> EmpleadosR;


	Date date = new Date();
	//CLick derecho source crear implementmetodo,  do after compose
	//Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		
		//Reecupera la Ventana Padre
		personaLista = (Ctrl_ListaPresidentes)Executions.getCurrent().getArg().get("VentanaPadre");

		persona=null;
		if(Executions.getCurrent().getArg().get("SegPeriodoEncargo")!=null){
			//Recupera Persona selecionada

			persona = (SegPeriodoEncargo)Executions.getCurrent().getArg().get("SegPeriodoEncargo");
			//recupera();
			//clave.setValue();
		}else{
			//Persona Nueva
			persona = new SegPeriodoEncargo();
		}
	}



	@Listen("onClick=#grabar")
	public void grabar(){
		Messagebox.show("Desea Guardar la Información",
				"Mensaje", 
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
				try {
					
										
					
					Time hora =  new  Time (date.getTime());
					personaDao.getEntityManager().getTransaction().begin();
					//Almacena Datos

					if(persona.getIdPeriodo()== 0){
						//Si es nuevo "persist"
						
						//campos de auditoria
						persona.setFecha(date);
						persona.setEstado("A");
						
						personaDao.getEntityManager().persist(persona);
					}else{
						

						persona = (SegPeriodoEncargo) personaDao.getEntityManager().merge(persona);
					}


					//cierra la transaccion
					personaDao.getEntityManager().getTransaction().commit();
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


	@Listen("onOK=#combo_empleado;onSelect=#combo_empleado;")
	public void recupera(){

		EmpleadosR = empleadodao.getTodos(combo_empleado.getText());
		for(SegUsuario det : EmpleadosR) {

			nombres.setValue(det.getApellidoPaterno() + " " + det.getApellidoMaterno() + " " + det.getNombre());
		}
	}

	//Boton salir
	@Wire Window winCuentas;

	@Listen("onClick=#salir")
	public void salir(){
		winCuentas.detach();

	}



	

	public SegPeriodoEncargo getPersona() {
		return persona;
	}



	public void setPersona(SegPeriodoEncargo persona) {
		this.persona = persona;
	}





	public List<SegUsuario> getEmpleadosR() {
		return EmpleadosR;
	}



	public void setEmpleadosR(List<SegUsuario> empleadosR) {
		EmpleadosR = empleadosR;
	}



	//llenar combos
	public List<SegUsuario> getEmpleados(){

		return empleadodao.getSinAmd();

	}
	
}

