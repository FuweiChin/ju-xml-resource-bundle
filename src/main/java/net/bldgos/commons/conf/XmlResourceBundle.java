package net.bldgos.commons.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class XmlResourceBundle extends ResourceBundle {

	private Properties lookup;

	public XmlResourceBundle(InputStream stream,String suffix) throws IOException {
		Properties properties = new Properties();
		if(suffix.equals("properties")) {
			properties.load(stream);
		}else if(suffix.equals("xml")) {
			properties.loadFromXML(stream);
		}
		lookup = properties;
	}

	public Object handleGetObject(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return lookup.get(key);
	}

	public Enumeration<String> getKeys() {
		ResourceBundle parent = this.parent;
		return new ResourceBundleEnumeration(lookup.stringPropertyNames(), (parent != null) ? parent.getKeys() : null);
	}

	protected Set<String> handleKeySet() {
		return lookup.stringPropertyNames();
	}

	public static class Control extends ResourceBundle.Control {
		public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList("java.properties","java.xml"));
		public static final List<String> FORMAT_PROPERTIES = Collections.unmodifiableList(Arrays.asList("java.properties"));
		public static final List<String> FORMAT_XML = Collections.unmodifiableList(Arrays.asList("java.xml"));

		@Override
		public List<String> getFormats(String baseName) {
			if (baseName == null) {
				throw new NullPointerException();
			}
			return FORMAT_DEFAULT;
		}

		@Override
		public Locale getFallbackLocale(String baseName, Locale locale) {
			return Locale.US;
		}

		@Override
		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
				throws IllegalAccessException, InstantiationException, IOException {
			String bundleName = toBundleName(baseName, locale);
			ResourceBundle bundle = null;
			if (format.equals("java.xml")) {
				String resourceName = toResourceName(bundleName, "xml");
				if (resourceName == null) {
					return bundle;
				}
				final ClassLoader classLoader = loader;
				final boolean reloadFlag = reload;
				InputStream stream = null;
				try {
					stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
						public InputStream run() throws IOException {
							InputStream is = null;
							if (reloadFlag) {
								URL url = classLoader.getResource(resourceName);
								if (url != null) {
									URLConnection connection = url.openConnection();
									if (connection != null) {
										// Disable caches to get fresh data for reloading.
										connection.setUseCaches(false);
										is = connection.getInputStream();
									}
								}
							} else {
								is = classLoader.getResourceAsStream(resourceName);
							}
							return is;
						}
					});
				} catch (PrivilegedActionException e) {
					throw (IOException) e.getException();
				}
				if (stream != null) {
					try {
						bundle = new XmlResourceBundle(stream,"xml");
					} finally {
						stream.close();
					}
				}
				return bundle;
			} else if (format.equals("java.properties")) {
				final String resourceName = toResourceName(bundleName, "properties");
				if (resourceName == null) {
					return bundle;
				}
				final ClassLoader classLoader = loader;
				final boolean reloadFlag = reload;
				InputStream stream = null;
				try {
					stream = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
						public InputStream run() throws IOException {
							InputStream is = null;
							if (reloadFlag) {
								URL url = classLoader.getResource(resourceName);
								if (url != null) {
									URLConnection connection = url.openConnection();
									if (connection != null) {
										connection.setUseCaches(false);
										is = connection.getInputStream();
									}
								}
							} else {
								is = classLoader.getResourceAsStream(resourceName);
							}
							return is;
						}
					});
				} catch (PrivilegedActionException e) {
					throw (IOException) e.getException();
				}
				if (stream != null) {
					try {
						bundle = new XmlResourceBundle(stream,"properties");
					} finally {
						stream.close();
					}
				}
			} else {
				throw new IllegalArgumentException("unknown format: " + format);
			}
			return bundle;
		}
		@Override
		public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
			if (bundle == null) {
				throw new NullPointerException();
			}
			String suffix="";
			if (format.startsWith("java.")) {
				suffix = format.substring(5);
			}
			boolean result = false;
			try {
				String resourceName = toResourceName(toBundleName(baseName, locale), suffix);
				if (resourceName == null) {
					return result;
				}
				URL url = loader.getResource(resourceName);
				if (url != null) {
					long lastModified = 0;
					URLConnection connection = url.openConnection();
					if (connection != null) {
						connection.setUseCaches(false);
						lastModified = connection.getLastModified();
					}
					result = lastModified >= loadTime;
				}
			} catch (NullPointerException npe) {
				throw npe;
			} catch (Exception e) {
			}
			return result;
		}

	}
}
