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
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Radio;

import model.SegPeriodoEncargo;
import model.SegPeriodoEncargoDAO;
import model.SisDoDonacioneDAO;
import util.PrintReport;

@SuppressWarnings({ "serial", "rawtypes" })
public class ReporteGeneralEgresos extends SelectorComposer{

	
	@Wire Radio radio_ingresos,radio_egresos;
	@Wire Datebox fecha_inicioB,fecha_finB;
	@Wire Label fi,ff;
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
		 fecha_inicioB.setVisible(false);
			fecha_finB.setVisible(false);
			ff.setVisible(false);
			fi.setVisible(false);
	}
	
	
	@Listen("onClick=#radio_ingresos")
	public void radioingresos(){
		if(radio_ingresos.isChecked() == true){
			
			radio_egresos.setChecked(false);
		
			fecha_inicioB.setVisible(false);
			fecha_finB.setVisible(false);
		
			ff.setVisible(false);
			fi.setVisible(false);
			
		}		
	}
	
	
	@Listen("onClick=#radio_egresos")
	public void radioegresos(){
		if(radio_egresos.isChecked() == true){
			
			radio_ingresos.setChecked(false);
		
			fecha_inicioB.setVisible(true);
			fecha_finB.setVisible(true);
		
			ff.setVisible(true);
			fi.setVisible(true);
			
		}		
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
				objA.ejecutaReporte(IngresoDao,"/A_Reportes/EgresosProyectos/ReporteEgresos.jasper", parametrosA, "PDF", "Reporte");				
				Clients.showNotification("Reporte Generado");
				return;

		}
		
		
		//
		if(radio_egresos.isChecked() == true){
			
			//aqui imprimo el reporte en pdf 
			//imprimir reporte   
			PrintReport objA = new PrintReport();
			Map<String,Object> parametrosA = new HashMap<String,Object>();
			parametrosA.put("usuario",codigoP);
			parametrosA.put("fechaini",fecha_inicioB.getValue());
			parametrosA.put("fechafin",fecha_finB.getValue());
			objA.ejecutaReporte(IngresoDao,"/A_Reportes/GastoPorMeses/GastosPorMeses.jasper", parametrosA, "PDF", "Reporte");				
			Clients.showNotification("Reporte Generado");
			return;

	}
	}


	
}

