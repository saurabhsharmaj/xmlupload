package com.example.xml.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.example.xml.exception.FileStorageException;
import com.example.xml.exception.MyFileNotFoundException;
import com.example.xml.vo.EpaperRequest;

@Service
public class FileStorageService {

	private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
	private final Path fileStorageLocation;

	@Value("${file.uploadDir}")
	private String uploadDir;

	@Autowired
	public FileStorageService() {
		this.fileStorageLocation = Paths.get("/temp").toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public String storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			File uploadFile = convertToXml(file);
			if (validateXMLSchema(uploadFile)) {
				System.out.println("vaild XML");
				EpaperRequest data = parseXml(file);
				System.out.println(data);
			} else {
				throw new FileStorageException("Could not store file " + fileName + ". Invalid XML!");
			}
			return fileName;
		} catch (Exception ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	private EpaperRequest parseXml(MultipartFile file) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(EpaperRequest.class);

		javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		EpaperRequest data = (EpaperRequest) jaxbUnmarshaller.unmarshal(convertToXml(file));
		return data;
	}

	private File convertToXml(MultipartFile file) {
		try {
			// create a temporary file with a unique name
			File tempFile = File.createTempFile("temp", ".xml");

			// write the contents of the MultipartFile to the temporary file
			file.transferTo(tempFile);

			// return the temporary file
			return tempFile;
		} catch (IOException e) {
			// handle the exception
			return null;
		}
	}

	private boolean validateXMLSchema(File xmlFile) {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new File(getResource("schema.xsd")));

			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			return true;
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getResource(String filename) throws FileNotFoundException {
		URL resource = getClass().getClassLoader().getResource(filename);
		Objects.requireNonNull(resource);

		return resource.getFile();
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}
}
