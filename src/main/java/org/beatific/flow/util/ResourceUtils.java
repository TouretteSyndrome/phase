package org.beatific.flow.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class ResourceUtils {

	public static InputStream getInputStream(String pathLocation)
			throws IOException {
		Assert.notNull(pathLocation, "Classpath location must not be null");
		if (pathLocation.startsWith("classpath:")) {
			String path = pathLocation.substring("classpath:".length());
			String description = "class path resource [" + path + "]";
			ClassLoader cl = ClassUtils.getDefaultClassLoader();
			URL url = (cl != null) ? cl.getResource(path) : ClassLoader
					.getSystemResource(path);
			if (url == null) {
				throw new FileNotFoundException(
						description
								+ " cannot be resolved to absolute file path because it does not exist");
			}

			return getInputStream(url);
		} else if(pathLocation.startsWith("file:")) {
			
			String path = pathLocation.substring("file:".length());
			File file = new File(path);
			return getInputStream(file);
			
	    } else {

			File file = new File(pathLocation);
			return getInputStream(file);
		}

	}
	
	public static InputStream getInputStream(File file) throws IOException {
		return FileUtils.openInputStream(file);
	}

	public static InputStream getInputStream(URL resourceUrl)
			throws IOException {

		return resourceUrl.openStream();
	}
}
