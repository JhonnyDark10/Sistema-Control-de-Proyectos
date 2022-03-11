package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;

import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
import model.SisDoDonacioneDAO;
import util.PrintReport;

@SuppressWarnings({ "serial", "rawtypes" })
public class ReportePrueba extends SelectorComposer{

	
	@Wire Radio radio_ingresos;
	@Wire Datebox fechaini,fechafin;
	
	@Wire Combobox cbo_proyectos;
	// Instancia el objeto para acceso a datos.
	private SegPeriodoEncargoDAO usuarioDao = new SegPeriodoEncargoDAO();

   private String EstadoEscogido;

	private List<SegPeriodoEncargo> Usuarios;
	private SisDoDonacioneDAO IngresoDao= new SisDoDonacioneDAO();	
	
	
	

    public Integer codigoP ;

	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);	
		buscar();
		
		radio_ingresos.setChecked(true);
		radio_ingresos.setDisabled(true);
		radio_ingresos.setVisible(false);
		
		
	}
	
	public void buscar(){
		//System.out.println("INGRESO");

		if (Usuarios != null) {
			Usuarios = null; 
		}

		Usuarios = usuarioDao.getListaPerfiles("");

		for(SegPeriodoEncargo det: Usuarios) {
			codigoP = det.getIdPeriodo();
		}
		
	}

	
	
	@Listen("onClick=#imprimir")
	public void imprimir(){
		
		if(radio_ingresos.isChecked() == true){
			     
			//AQUI VALIDO Q PROEYCTOS QUIERO VER 
			
			if(cbo_proyectos.getText()== "") {
				Clients.showNotification("Escoja un tipo");
				return;
			}
			
			if(cbo_proyectos.getText().equals("Propuestas")) {
				EstadoEscogido = "A";
				
			}
			if(cbo_proyectos.getText().equals("Ejecutados")) {
				EstadoEscogido = "E";
				
			}
			if(cbo_proyectos.getText().equals("Terminados")) {
				EstadoEscogido = "T";
				
			}
			if(cbo_proyectos.getText().equals("Rechazados")) {
				EstadoEscogido = "R";
				
			}
			
			
			//aqui valido para q solo me salga los del periodo actual 
			
			
			//jajaja mejor al inicio e lo mismo
			
			System.out.print("este este escogi : " + EstadoEscogido);
			System.out.print("este este escogi : " + codigoP);
			
				//aqui imprimo el reporte en pdf 
				//imprimir reporte   
				PrintReport objA = new PrintReport();
				Map<String,Object> parametrosA = new HashMap<String,Object>();
				parametrosA.put("estadof",EstadoEscogido);
				parametrosA.put("usuariof",codigoP);
				parametrosA.put("fechaini",fechaini.getValue());
				parametrosA.put("fechafin",fechafin.getValue());
				objA.ejecutaReporte(IngresoDao,"/A_Reportes/Prueva05.jasper", parametrosA, "PDF", "Reporte");				
				Clients.showNotification("Reporte Generado");
				return;

		}
		
		
		
		
	}


	
}
