package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import model.ClaseDAO;

import org.zkoss.zk.ui.Executions;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class PrintReport {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";
@Wire 
Jasperreport report;

	public static void ejecutaReporte(ClaseDAO claseDAO, 
			                          String pathReporte, 
			                          Map<String, Object> parametros, 
			                          String formato,String tituloReporte) {
		// Almacena el nombre de archivo resultante a descargar.
		String nombreArchivo = null;

		// Obtiene la conexion con la base de datos para pasarsela a Jasper
		Connection cn = claseDAO.conectar();
		
		try {
			
			// Obtiene el path de la aplicacion.
			String pathAbsoluto = Executions.getCurrent()
					              .getDesktop().getWebApp()
					              .getRealPath("/");
			
			// Arma el path del reporte basado en el path de la aplicacion.
			String archivoReporte = pathAbsoluto + pathReporte;
			
			// Obtiene un nombre aleatorio para el reporte
			nombreArchivo = pathAbsoluto + "/temp/" + UUID.randomUUID().toString();
			System.out.println("Nombre: " + nombreArchivo);
			if (parametros == null) {
				parametros = new HashMap<String, Object>();
			}
			
			// Coloca los parametros por defecto
			//parametros.put("NOMBRE_INSTITUCION", "Curso de JAVA");
			//System.out.println(parametros + " " + formato);
			// Selecciona el formato.
			if (formato.equals(FORMATO_PDF)) {
				nombreArchivo += ".pdf";
				
				JasperPrint jasperprint=JasperFillManager.fillReport(archivoReporte,parametros,cn);
				
				JasperViewer visor = new JasperViewer(jasperprint,false);
				
				visor.setTitle(tituloReporte);
	            visor.setVisible(true);
	         
	            
			   
			}else{
				/* Si se genera en hoja electrónica.
				nombreArchivo += ".xls";
		        JasperPrint jasperPrint = JasperFillManager.fillReport(archivoReporte, parametros, cn);
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
				//System.out.println("ejecuto eporte");*/
			}
			
			// Descarga el archivo.
			//System.out.println("Descarga el archivo");
			Filedownload.save(new File(nombreArchivo), formato); 
			//System.out.println("Descargo el archivo");
		} catch (Exception ex) {
			System.out.println(ex.getMessage()); 
		}
		
	}

}