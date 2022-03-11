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

import model.SisProyectoCategoria;
import model.SisProyectoCategoriaDAO;
import model.SisProyectoEstado;
import model.SisProyectoEstadoDAO;



//LISTADO DE LAS EMBARCACIONES QUE SOLICITARON EL SERVICIO DE DESEMBARQUE DE PESCA

@SuppressWarnings({ "serial", "rawtypes" })
public class Ctrl_ListaEstados extends SelectorComposer{

	@Wire 
	Textbox txtBuscar;

	@Wire 
	private Listbox LstTipoCategoria;

	//Lamar venta 
	@Wire
	Window winListaEmpleados;


	// Instancia el objeto para acceso a datos.
	private SisProyectoEstadoDAO SisEmbTipodao = new SisProyectoEstadoDAO();


	private List<SisProyectoEstado> Sistipocategoria;


	//aqui para seleccionar el objeto  a añadir o editar
	private SisProyectoEstado TipocategoriaSelecionada;






	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		listaIngreso();
	}


	//lista las embarcaciones a descargar

	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void listaIngreso(){
		if (Sistipocategoria != null) {
			Sistipocategoria = null; 
		}
		Sistipocategoria = SisEmbTipodao.getTipoEstado(txtBuscar.getText());
		LstTipoCategoria.setModel(new ListModelList(Sistipocategoria));
		
		//Limpiar
		TipocategoriaSelecionada = null;
	}

	
	
	//*******************************************
	//Escucha evento
	@Listen("onClick=#btnNuevo")
	public void nuevo(){

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SisProyectoEstado", null);
		params.put("VentanaPadre", this);


		Window ventanaCargar = (Window)Executions.createComponents("/A_Proyectos/IngresarEstado.zul", winListaEmpleados,params);
		ventanaCargar.doModal();
	}


	//*******************************************
	//Escucha evento
	@Listen("onClick=#btnEditar")
	public void editar(){

		if(TipocategoriaSelecionada == null){
			Clients.showNotification("Debe seleccionar un Registro");
			return;
		}
		//Actualiza la instancia antes de editar
		SisEmbTipodao.getEntityManager().refresh(TipocategoriaSelecionada);//berifica el ultimo valor en la BD

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SisProyectoEstado", TipocategoriaSelecionada);
		params.put("VentanaPadre", this);

		Window ventanaCargar = (Window)Executions.createComponents("/A_Proyectos/IngresarEstado.zul", winListaEmpleados,params);
		ventanaCargar.doModal();
	}
	//*********************ELIMINAR*************************
	/**
	 * Escucha el evento "onClick" del objeto "btnEliminar"
	 * Elimina logicamente una persona.
	 */
	@Listen("onClick=#btnEliminar")
	public void eliminar(){
		if (TipocategoriaSelecionada == null) {
			Clients.showNotification(" Debe seleccionar un Registro");
			return;
		}
		Messagebox.show("Desea Eliminar el Registro Seleccionado","Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						//personaSeleccionada.setEstado("X");
						TipocategoriaSelecionada.setEstadoProyecto("I");;
						SisEmbTipodao.getEntityManager().getTransaction().begin();
						SisEmbTipodao.getEntityManager().persist(TipocategoriaSelecionada);
						SisEmbTipodao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Registro Eliminado");
						listaIngreso();
					} catch (Exception e) {
						e.printStackTrace();
						SisEmbTipodao.getEntityManager().getTransaction().rollback();
					}
				}


			}
		});

	}


	public List<SisProyectoEstado> getSistipocategoria() {
		return Sistipocategoria;
	}


	public void setSistipocategoria(List<SisProyectoEstado> sistipocategoria) {
		Sistipocategoria = sistipocategoria;
	}


	public SisProyectoEstado getTipocategoriaSelecionada() {
		return TipocategoriaSelecionada;
	}


	public void setTipocategoriaSelecionada(SisProyectoEstado tipocategoriaSelecionada) {
		TipocategoriaSelecionada = tipocategoriaSelecionada;
	}


	

	//Getters and Setters



}       


