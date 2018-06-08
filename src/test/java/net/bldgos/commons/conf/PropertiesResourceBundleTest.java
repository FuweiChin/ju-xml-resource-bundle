package net.bldgos.commons.conf;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;

public class PropertiesResourceBundleTest {
	@Test
	public void getBundle() {
		ResourceBundle bundle=ResourceBundle.getBundle("locales.LocalizedStrings",Locale.CHINA);
		String localizedWelome=bundle.getString("welcome");
		Assert.assertEquals("欢迎", localizedWelome);
	}
}
