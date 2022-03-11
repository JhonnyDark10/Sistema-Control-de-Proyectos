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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;

import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
import model.SisDoDonacioneDAO;
import util.PrintReport;

@SuppressWarnings({ "serial", "rawtypes" })
public class ReporteProyectos extends SelectorComposer{

	
	@Wire Radio radio_propuestas,radio_ejecutados,radio_terminados,radio_rechazados;
	
	// Instancia el objeto para acceso a datos.
	private SegPeriodoEncargoDAO usuarioDao = new SegPeriodoEncargoDAO();

    public Integer codigoP ;

	private List<SegPeriodoEncargo> Usuarios;
	private SisDoDonacioneDAO IngresoDao= new SisDoDonacioneDAO();	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);	
		 buscar();
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
		
		if(radio_propuestas.isChecked() == true){
			
				//aqui imprimo el reporte en pdf 
				//imprimir reporte   
				PrintReport objA = new PrintReport();
				Map<String,Object> parametrosA = new HashMap<String,Object>();
				parametrosA.put("usuario",codigoP);
				objA.ejecutaReporte(IngresoDao,"/A_Reportes/Proyectos/ReportePropuestas.jasper", parametrosA, "PDF", "Reporte");				
				Clients.showNotification("Reporte Generado");
				return;

		}
		
		if(radio_ejecutados.isChecked() == true){
			
			//aqui imprimo el reporte en pdf 
			//imprimir reporte   
			PrintReport objA = new PrintReport();
			Map<String,Object> parametrosA = new HashMap<String,Object>();
			parametrosA.put("usuario",codigoP);
			objA.ejecutaReporte(IngresoDao,"/A_Reportes/Proyectos/ReporteEjecutados.jasper", parametrosA, "PDF", "Reporte");				
			Clients.showNotification("Reporte Generado");
			return;

	}
		
	if(radio_rechazados.isChecked() == true){
			
			//aqui imprimo el reporte en pdf 
			//imprimir reporte   
			PrintReport objA = new PrintReport();
			Map<String,Object> parametrosA = new HashMap<String,Object>();
			parametrosA.put("usuario",codigoP);
			objA.ejecutaReporte(IngresoDao,"/A_Reportes/Proyectos/ReporteRechazo.jasper", parametrosA, "PDF", "Reporte");				
			Clients.showNotification("Reporte Generado");
			return;

	}
		
		if(radio_terminados.isChecked() == true){
			
			//aqui imprimo el reporte en pdf 
			//imprimir reporte   
			PrintReport objA = new PrintReport();
			Map<String,Object> parametrosA = new HashMap<String,Object>();
			parametrosA.put("usuario",codigoP);
			objA.ejecutaReporte(IngresoDao,"/A_Reportes/Proyectos/ReporteTerminados.jasper", parametrosA, "PDF", "Reporte");				
			Clients.showNotification("Reporte Generado");
			return;			

	}
		
		
		
		
	}


	
	@Listen("onClick=#radio_terminados")
	public void radio_terminados(){
		if(radio_terminados.isChecked() == true){
			radio_ejecutados.setChecked(false);
			radio_propuestas.setChecked(false);
			radio_rechazados.setChecked(false);
			
		}		
	}
	
	
	@Listen("onClick=#radio_ejecutados")
	public void radio_ejecutadas(){
		if(radio_ejecutados.isChecked() == true){
			radio_terminados.setChecked(false);
			radio_propuestas.setChecked(false);
			radio_rechazados.setChecked(false);
			
		}		
	}
	
	@Listen("onClick=#radio_propuestas")
	public void radio_propuestas(){
		if(radio_propuestas.isChecked() == true){
			radio_terminados.setChecked(false);
			radio_ejecutados.setChecked(false);
			radio_rechazados.setChecked(false);
			
		}		
	}
	
	
	@Listen("onClick=#radio_rechazados")
	public void radio_rechazados(){
		if(radio_rechazados.isChecked() == true){
			radio_terminados.setChecked(false);
			radio_ejecutados.setChecked(false);
			radio_propuestas.setChecked(false);
			
		}		
	}
	
	
	
}
