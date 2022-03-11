package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Radio;

import model.SegUsuario;
import model.SegUsuarioDAO;
import model.SisProyectoPropuesta;
import util.PrintReport;



	@SuppressWarnings({ "serial", "rawtypes" })
	public class ReporteListadEncargados extends SelectorComposer {
	
		@Wire  Combobox cbo_encargado;
	     @Wire Radio radio_usuarios;
	private SegUsuario TipoSelecionada;
	private SegUsuarioDAO usuariodao = new SegUsuarioDAO();
	

	//CLick derecho source crear implementmetodo,  do after compose
	//Levante la ventana crear un objeto persona vacio para ingresar dato
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		radio_usuarios.setChecked(true);
		radio_usuarios.setDisabled(true);
		radio_usuarios.setVisible(false);
		
	}

	
	
	
	
	@Listen("onClick=#imprimir")
	public void imprimir(){
		
		if(radio_usuarios.isChecked() == true){
			
				//aqui imprimo el reporte en pdf 
				//imprimir reporte   
				PrintReport objA = new PrintReport();
				Map<String,Object> parametrosA = new HashMap<String,Object>();
				parametrosA.put("usuariof",TipoSelecionada.getIdUsuario());
				objA.ejecutaReporte(usuariodao,"/A_Reportes/Listado Encargado/ListadoEncargado.jasper", parametrosA, "PDF", "Reporte");				
				Clients.showNotification("Reporte Generado");
				return;

		}
		
	
	}


	public List<SegUsuario> getLlenarUsuarios(){

		return usuariodao.getSinAmd();

	}


	public SegUsuario getTipoSelecionada() {
		return TipoSelecionada;
	}


	public void setTipoSelecionada(SegUsuario tipoSelecionada) {
		TipoSelecionada = tipoSelecionada;
	}

	
}
