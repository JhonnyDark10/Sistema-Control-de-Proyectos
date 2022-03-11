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

import model.SegPerfil;
import model.SegPerfilDAO;



//LISTADO DE PERFILES
@SuppressWarnings({ "serial", "rawtypes" })
public class Seg_ListaPerfiles extends SelectorComposer{

	@Wire 
	Textbox txtBuscar;

	@Wire 
	private Listbox LstPerfil;

	//Lamar venta 
	@Wire
	Window winListaEmpleados;


	// Instancia el objeto para acceso a datos.
	private SegPerfilDAO SisEmbTipodao = new SegPerfilDAO();


	private List<SegPerfil> Sisperfil;


	//aqui para seleccionar el objeto  a añadir o editar
	private SegPerfil TipoperfilSelecionada;






	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		listaIngreso();
	}


	//lista de perfiles

	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void listaIngreso(){
		if (Sisperfil != null) {
			Sisperfil = null; 
		}
		Sisperfil = SisEmbTipodao.getListaPerfiles(txtBuscar.getText());
		LstPerfil.setModel(new ListModelList(Sisperfil));
		
		//Limpiar
		TipoperfilSelecionada = null;
	}

	
	
	//*******************************************
	//Escucha evento
	@Listen("onClick=#btnNuevo")
	public void nuevo(){

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SegPerfil", null);
		params.put("VentanaPadre", this);


		Window ventanaCargar = (Window)Executions.createComponents("/A_ModuloSeguridad/Perfiles.zul", winListaEmpleados,params);
		ventanaCargar.doModal();
	}


	//*******************************************
	//Escucha evento
	@Listen("onClick=#btnEditar")
	public void editar(){

		if(TipoperfilSelecionada == null){
			Clients.showNotification("Debe seleccionar un Registro");
			return;
		}
		//Actualiza la instancia antes de editar
		SisEmbTipodao.getEntityManager().refresh(TipoperfilSelecionada);//berifica el ultimo valor en la BD

		//Enviar por parametros - para editar persona 
		Map<String, Object> params = new HashMap<String, Object>(); //Objeto Paramn tipo de objeto hasMap
		params.put("SegPerfil", TipoperfilSelecionada);
		params.put("VentanaPadre", this);

		Window ventanaCargar = (Window)Executions.createComponents("/A_ModuloSeguridad/Perfiles.zul", winListaEmpleados,params);
		ventanaCargar.doModal();
	}
	//*********************ELIMINAR*************************
	/**
	 * Escucha el evento "onClick" del objeto "btnEliminar"
	 * Elimina logicamente una persona.
	 */
	@Listen("onClick=#btnEliminar")
	public void eliminar(){
		if (TipoperfilSelecionada == null) {
			Clients.showNotification(" Debe seleccionar un Registro");
			return;
		}
		Messagebox.show("Desea Eliminar el Registro Seleccionado","Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						//personaSeleccionada.setEstado("X");
						TipoperfilSelecionada.setEstado("I");;
						SisEmbTipodao.getEntityManager().getTransaction().begin();
						SisEmbTipodao.getEntityManager().persist(TipoperfilSelecionada);
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


	public List<SegPerfil> getSisperfil() {
		return Sisperfil;
	}


	public void setSisperfil(List<SegPerfil> sisperfil) {
		Sisperfil = sisperfil;
	}


	public SegPerfil getTipoperfilSelecionada() {
		return TipoperfilSelecionada;
	}


	public void setTipoperfilSelecionada(SegPerfil tipoperfilSelecionada) {
		TipoperfilSelecionada = tipoperfilSelecionada;
	}

	

	//Getters and Setters



}       

