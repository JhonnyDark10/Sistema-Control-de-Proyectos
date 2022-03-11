package controlador;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import model.SegPerfil;
import model.SegPerfilDAO;
import model.SegUsuario;
import model.SegUsuarioDAO;

import util.RecuperarClave;
import util.Validar;

@SuppressWarnings({ "serial", "rawtypes" })
public class Ctrl_EditarCuentas extends SelectorComposer {

	@Wire
	Textbox nombre, apellido_paterno, apellido_materno, telefono, email, celular;
	@Wire
	Textbox txt_cedula;
	@Wire
	Textbox usuario, clave, recupera_nombre;
	@Wire
	Checkbox visualizar;
	@Wire
	Label numero;
	@Wire
	Combobox combo_empleado, combo_perfil;
	@Wire
	Label nombres;
	RecuperarClave recupera = new RecuperarClave();
	Menu controlador;
	// private DepartamentoDao paisDao = new DepartamentoDao();
	Validar valida = new Validar();
	private SegPerfilDAO perfildao = new SegPerfilDAO();

	/*
	 * Recupera datos de usuario
	 */

	private List<SegUsuario> ListaRecuperaDatos;

	/*
	 *
	 */

	// Para poder Grabar
	SegUsuarioDAO personaDao = new SegUsuarioDAO();
	private SegUsuario persona;

	// COntiene la ventana padre para invocar a la actualizacion
	private Ctrl_ListaCuentas personaLista;

	private List<SegUsuario> UsuarioR;
	Date date = new Date();

	// CLick derecho source crear implementmetodo, do after compose
	// Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);

		visualizar.setChecked(true);
		// Reecupera la Ventana Padre
		personaLista = (Ctrl_ListaCuentas) Executions.getCurrent().getArg().get("VentanaPadre");

		persona = null;
		if (Executions.getCurrent().getArg().get("SegUsuario") != null) {
			// Recupera Persona selecionada

			persona = (SegUsuario) Executions.getCurrent().getArg().get("SegUsuario");
			// recupera();
			// clave.setValue();
		} else {
			// Persona Nueva
			persona = new SegUsuario();
		}
	}

	@Listen("onClick=#visualizar")
	public void visualiza() {

		if (visualizar.isChecked() == true) {
			clave.setType("text");

		} else {
			clave.setType("password");
		}
	}

	@Listen("onClick=#grabar")
	public void grabar() {
		Messagebox.show("Desea Guardar la Información", "Mensaje", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onYes")) {
							try {

								if (validarDatos() == true) {

								} else {
									return;
								}

								Time hora = new Time(date.getTime());
								personaDao.getEntityManager().getTransaction().begin();
								// Almacena Datos

								if (persona.getIdUsuario() == 0) {
									// Si es nuevo "persist"

									UsuarioR = personaDao.getTodos(txt_cedula.getText());
									if (UsuarioR.size() > 0) {
									Clients.showNotification("Empleado con cedula ya Registrado");
										
										/*
										 * txt_cedula.setText(" "); usuario.setText(" "); nombre.setText(" ");
										 * clave.setText(" "); apellido_materno.setText(" ");
										 * apellido_paterno.setText(" "); telefono.setText(" "); celular.setText(" ");
										 * 
										 * combo_perfil.setSelectedIndex(-1); email.setText(" ");
										 */

											
										return;
									}

									// campos de auditoria
									persona.setFecha(date);
									persona.setHora(hora);
									// persona.setControlador(1);

									persona.setControlador(controlador.variable_controlador);

									persona.setClave(recupera.encriptar(clave.getText()));
									persona.setEstado("A");
									persona.setVerifica(0);
									personaDao.getEntityManager().persist(persona);
								} else {
									if (clave.getText() == "") {

									} else {
										if (visualizar.isChecked() == true) {
											persona.setClave(recupera.encriptar(clave.getText()));
											persona.setVerifica(1);
										} else {
											Clients.showNotification("Marque la casilla");
											return;
										}

									}

									persona = (SegUsuario) personaDao.getEntityManager().merge(persona);
								}

								// cierra la transaccion
								personaDao.getEntityManager().getTransaction().commit();

								salir();
								personaLista.buscar();
								Clients.showNotification("Registro Almacenado");

							} catch (Exception e) {
								// Si hay error, revierte la transaccion
								personaDao.getEntityManager().getTransaction().rollback();

							}
						}
					}
				});
	}
//

	@Listen("onClick=#txt_cedula;onOK=#txt_cedula")
	public void RecuperaCedula() {

		ListaRecuperaDatos = personaDao.getRecuperaCedulaTabla(txt_cedula.getText());

		if (ListaRecuperaDatos.size() > 0) {

			// recupero datos
			for (SegUsuario ced : ListaRecuperaDatos) {

				
				nombre.setText(ced.getNombre());
				email.setText(ced.getEmail());
				telefono.setText(ced.getTelefono());
				apellido_materno.setText(ced.getApellidoMaterno());
				apellido_paterno.setText(ced.getApellidoPaterno());
				celular.setText(ced.getCelular());
				usuario.setText(ced.getUsuario());
				combo_perfil.setValue(ced.getSegPerfil().getDescripcion());
				

				Clients.showNotification("Datos recuperados");
				return;

			}

		} else {
			// limpia los datos

			Clients.showNotification("Usuario no registrado");

		}

	}
	//

	// Boton salir
	@Wire
	Window winCuentas;

	@Listen("onClick=#salir")
	public void salir() {
		winCuentas.detach();

	}

	public SegUsuario getPersona() {
		return persona;
	}

	public void setPersona(SegUsuario persona) {
		this.persona = persona;
	}

	public List<SegPerfil> getListaPerfiles() {

		return perfildao.getListaPerfiles("");

	}

	private boolean validarDatos() {
		try {

			if (usuario.getText() == "") {
				Clients.showNotification("Debe registrar Usuario");
				return false;
			}
			/*
			 * if(clave.getText() == "") { Clients.showNotification("Debe registrar Clave");
			 * return false; }
			 */

			if (combo_perfil.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Perfil");
				return false;
			}

			if (valida.validarDeCedula(txt_cedula.getText()) == false) {
				Clients.showNotification("Cédula no valida");
				return false;
			}
			return true;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

}
