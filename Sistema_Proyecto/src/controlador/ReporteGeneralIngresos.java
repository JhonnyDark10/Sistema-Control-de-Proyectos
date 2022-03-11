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
public class ReporteGeneralIngresos extends SelectorComposer{

	
	@Wire Radio radio_ingresos, radio_ingresosEconomico;
	
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
		
		if(radio_ingresos.isChecked() == true){
			
				//aqui imprimo el reporte en pdf 
				//imprimir reporte   
				PrintReport objA = new PrintReport();
				Map<String,Object> parametrosA = new HashMap<String,Object>();
				parametrosA.put("usuario",codigoP);
				objA.ejecutaReporte(IngresoDao,"/A_Reportes/ReporteIngresos.jasper", parametrosA, "PDF", "Reporte");				
				Clients.showNotification("Reporte Generado");
				return;

		}
		
		//
		if(radio_ingresosEconomico.isChecked() == true){
			
			//aqui imprimo el reporte en pdf 
			//imprimir reporte   
			PrintReport objA = new PrintReport();
			Map<String,Object> parametrosA = new HashMap<String,Object>();
			parametrosA.put("usuario",codigoP);
			objA.ejecutaReporte(IngresoDao,"/A_Reportes/ReporteIngresosEconomico.jasper", parametrosA, "PDF", "Reporte");				
			Clients.showNotification("Reporte Generado");
			return;

	}
		
		
	}


	@Listen("onClick=#radio_ingresos")
	public void radio_ingresos(){
		if(radio_ingresos.isChecked() == true){
			radio_ingresosEconomico.setChecked(false);
	
			
		}		
	}
	
	
	@Listen("onClick=#radio_ingresosEconomico")
	public void radio_ingresosEconomico(){
		if(radio_ingresosEconomico.isChecked() == true){
			radio_ingresos.setChecked(false);
			
			
		}		
	}
	
	
}
