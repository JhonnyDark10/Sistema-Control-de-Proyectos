package controlador;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import model.SegPerfil;
import model.SegPerfilDAO;
import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
import model.SegUsuario;
import model.SegUsuarioDAO;
import model.SisDoAporte;
import model.SisDoAporteDAO;
import model.SisDoDescripcione;
import model.SisDoDescripcioneDAO;
import model.SisDoDonacione;
import model.SisDoDonacioneDAO;
import util.RecuperarClave;
import util.Validar;



@SuppressWarnings({ "serial", "rawtypes" })
public class Ctrl_Donaciones extends SelectorComposer {

	@Wire Textbox fechaactual,horaactual,cedula,institucion,aportante,valor,descripcion,cantidad;
	@Wire Label lvalor,ldescripcion,lcantidad;
	@Wire Radio radio_efectivo,radio_bienes ;

	Validar valida = new Validar();
	Date date = new Date();


	private SisDoDonacione donaciones;
	private SisDoAporte don_aporte;
	private SisDoDescripcione don_descripcion;
	private List<SisDoDonacione> ListaRecuperaDatos;

	private SisDoDonacioneDAO donacionesdao = new SisDoDonacioneDAO();
	private SisDoAporteDAO doaportedao = new SisDoAporteDAO();
	private SisDoDescripcioneDAO dodescripciondao = new SisDoDescripcioneDAO();


	private List<SegPeriodoEncargo> detalleServicio;
	private SegPeriodoEncargoDAO detalleServicioDAO = new SegPeriodoEncargoDAO();
	
	
	
	// Instancia el objeto para acceso a datos.
		private SegPeriodoEncargoDAO usuarioDao = new SegPeriodoEncargoDAO();

	    public Integer codigoPU ;

		private List<SegPeriodoEncargo> Usuarios;
		
		
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub


		super.doAfterCompose(comp);	

		donaciones = new SisDoDonacione();
		don_aporte = new SisDoAporte();
		don_descripcion = new SisDoDescripcione();

		//Caso 1: obtener la hora y salida por pantalla con formato:
		DateFormat hourFormat = new SimpleDateFormat("HH:mm");
		horaactual.setValue(hourFormat.format(date));

		//Caso 2: obtener la fecha y salida por pantalla con formato:
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		fechaactual.setValue(dateFormat.format(date));

		lvalor.setVisible(false);
		ldescripcion.setVisible(false);
		lcantidad.setVisible(false);
		valor.setVisible(false);
		descripcion.setVisible(false);
		cantidad.setVisible(false);
		
		buscar();

	}


	
	
	public void buscar(){
		//System.out.println("INGRESO");

		if (Usuarios != null) {
			Usuarios = null; 
		}

		Usuarios = usuarioDao.getListaPerfiles("");

		for(SegPeriodoEncargo det: Usuarios) {
			codigoPU = det.getIdPeriodo();
		}
		
	}

	

	@Listen("onClick=#radio_efectivo")
	public void radio_efectivo(){
		if(radio_efectivo.isChecked() == true){
			radio_bienes.setChecked(false);	


			lvalor.setVisible(true);

			valor.setVisible(true);
			ldescripcion.setVisible(false);
			lcantidad.setVisible(false);

			descripcion.setVisible(false);
			cantidad.setVisible(false);

		}		
	}


	@Listen("onClick=#radio_bienes")
	public void radio_bienes(){
		if(radio_bienes.isChecked() == true){
			radio_efectivo.setChecked(false);	

			lvalor.setVisible(false);

			valor.setVisible(false);
			ldescripcion.setVisible(true);
			lcantidad.setVisible(true);

			descripcion.setVisible(true);
			cantidad.setVisible(true);
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

						if(validarDatos() == true){
							
						}else{
							return;
						}
						
						//almacena la tabla donaciones
						Time hora =  new  Time (date.getTime());
						donacionesdao.getEntityManager().getTransaction().begin();
						//Almacena Datos									
						//campos
						donaciones.setFecha(date);
						donaciones.setCedulaRuc(cedula.getText());
						donaciones.setEstadoDonacion("A");
						donaciones.setNombre(institucion.getText());
						donaciones.setNombreAportante(aportante.getText());	
                        donaciones.setUsuario(codigoPU);

						donacionesdao.getEntityManager().persist(donaciones);
						//cierra la transaccion
						donacionesdao.getEntityManager().getTransaction().commit();

						//Clients.showNotification("Registro Almacenado");

						//almacena si es BIENES 

						if(radio_bienes.isChecked() == true) {
								
							dodescripciondao.getEntityManager().getTransaction().begin();
							//Almacena Datos									
							//campos
							don_descripcion.setCantidad(Integer.parseInt(cantidad.getText()));
							don_descripcion.setEstadoDescripcion("A");
							don_descripcion.setNombreDescripcion(descripcion.getText());
							
							don_descripcion.setSisDoDonacione(donaciones);
								

							
							dodescripciondao.getEntityManager().persist(don_descripcion);
							//cierra la transaccion
							dodescripciondao.getEntityManager().getTransaction().commit();

							Clients.showNotification("Registro Bienes Almacenado");
						}

						//almacena si es EFECTIVO

						if(radio_efectivo.isChecked() == true) {
							doaportedao.getEntityManager().getTransaction().begin();
							//Almacena Datos									
							//campos
							don_aporte.setEstadoAporte("A");
							don_aporte.setValor(Float.parseFloat(valor.getText()));
							don_aporte.setSisDoDonacione(donaciones);
								

							
							doaportedao.getEntityManager().persist(don_aporte);
							//cierra la transaccion
							doaportedao.getEntityManager().getTransaction().commit();

							Clients.showNotification("Registro de efectivo Almacenado");
							
							
							//actualizar el valor de ingreso del presidente 
							
							
							detalleServicio = detalleServicioDAO.getListaPerfiles("");
							
						
							for(SegPeriodoEncargo det : detalleServicio) {
								
								float acum = det.getValorIngreso();
								
								det.setValorIngreso(acum + Float.parseFloat(valor.getText()));
								
								detalleServicioDAO.getEntityManager().getTransaction().begin();
								detalleServicioDAO.getEntityManager().persist(det);
								detalleServicioDAO.getEntityManager().getTransaction().commit();
							}

							
							
						}


						
						//todo vuelve a la normalidad para ingresar nuevos datos 
						
						donaciones = new SisDoDonacione();
						don_aporte = new SisDoAporte();
						don_descripcion = new SisDoDescripcione();
						lvalor.setVisible(false);
						ldescripcion.setVisible(false);
						lcantidad.setVisible(false);
						valor.setVisible(false);
						descripcion.setVisible(false);
						cantidad.setVisible(false);
						radio_bienes.setChecked(false);
						radio_efectivo.setChecked(false);
						cedula.setText("");
						institucion.setText("");
						aportante.setText("");
						valor.setText("");
						descripcion.setText("");
						cantidad.setText("");
					

					} catch (Exception e) {
						//Si hay error, revierte la transaccion
						donacionesdao.getEntityManager().getTransaction().rollback();

					}
				}
			}
		});
	}

		
	private boolean validarDatos() {
		try {

						
			if(valida.validarDeCedula(cedula.getText()) == false) {
				Clients.showNotification("Cédula no valida");
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	@Listen("onOK=#cedula")
	public void RecuperaCedula(){
		
		//System.out.println("Esta adentro :v");	
		ListaRecuperaDatos = donacionesdao.getDonacionesG();	
		//System.out.println(ListaRecuperaDatos.size());
		
		if (ListaRecuperaDatos.size() > 0) {
			
			//recupero datos
			for(SisDoDonacione  ced : ListaRecuperaDatos) {

					
				if (ced.getCedulaRuc().equals(cedula.getText())) {
					//System.out.println("Encontro registro");
					aportante.setText(ced.getNombreAportante());				
					return ;
					
				}
				else {
					System.out.println("no ha entrado");
					
					aportante.setText("");
					
				}
			}
			
		}else {
			//limpia los datos
			
			Clients.showNotification("No existe registro");
			
		}
		
		
	}

}

