package net.bldgos.commons.conf;

import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

public class XmlResourceBundleControlProvider implements ResourceBundleControlProvider {
	private ResourceBundle.Control XRB_CONTROL=new XmlResourceBundle.Control();
	@Override
	public ResourceBundle.Control getControl(String baseName) {
		if(baseName.startsWith("locales.")) {
			return XRB_CONTROL;
		}
		return null;
	}
}
