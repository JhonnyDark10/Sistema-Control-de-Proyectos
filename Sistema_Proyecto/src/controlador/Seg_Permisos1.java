package controlador;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

@SuppressWarnings({ "serial", "rawtypes" })
public class Seg_Permisos1 extends SelectorComposer{

	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	  
		
	}

	@Command("subscribe")
	public void deleteOrder() {
	    System.out.print("");
	}
		
}
