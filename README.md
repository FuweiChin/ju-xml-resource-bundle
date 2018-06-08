# ju-xml-resource-bundle
A Java extension to enhance `java.util.ResourceBundle` with extra formats(XML,JSON) support.

### Why ju-xml-resource-bundle?
When using ResourceBundle, XML and JSON have their advantage comparing to PROPERTIES, since they are nearly WYSIWYG, especially for languages those contains non-ASCII characters.

The Spring framework supports loading XML resource with spring-i18n, so we are trying to add XML and JSON support for ResourceBundle.

Thanks to the Java 8 SPI `java.util.spi.ResourceBundleControlProvider`, developers could enhance ResourceBundle through Java extension, such that this project came into life.

### Usage
1. Checkout the proejct, build it with command `mvn package`.
2. Copy `target/ju-xml-resource-bundle.jar` to one of the `java.ext.dirs`, like `$JAVA_HOME/jre/lib/ext`.  
2. In your java project create a folder "locales" under source folder, put your XML, like `LocaleStrings_zh_CN.xml`, into "locales".
    The xml format should be exactly what `Properties#storeToXML()` outputs, see example below:  
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
    <properties>
    	<comment>简体中文</comment>
    	<entry key="welcome">欢迎</entry>
    	<entry key="goodbye">再见</entry>
    </properties>
```
3. Use `ResourceBundle.getBundle()` to load extra format bundle as usual.
```
ResourceBundle bundle=ResourceBundle.getBundle("locales.LocaleStrings");
String localizedWelome=bundle.getString("welcome");
```

**Note**:
1. Built-in supported format `.properties` takes priority over extended format `.xml`
2. Ensure that your bundle basename is prefixed with "locales."

Currently version of ju-xml-resource-bundle is 0.1  

### Features and Roadmap
**0.1**  
+ [x] Add XML resource support for ResourceBundle

**0.2**  
+ [ ] Add JSON resource support for ResourceBundle

