package microgram.impl.srv.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import microgram.api.rest.RestMediaStorage;
import utils.Hash;

public class RestMediaResources implements RestMediaStorage {

	private static final String MEDIA_EXTENSION = ".jpg";
	private static final String ROOT_DIR = "/tmp/microgram/";

	final String baseUri;

	public RestMediaResources(String baseUri ) {
		this.baseUri = baseUri + RestMediaStorage.PATH;
		new File( ROOT_DIR ).mkdirs();
	}

	@Override
	public String upload(byte[] bytes) {
		try {
			String id = Hash.of(bytes); // Compute an quasi-unique hash of the contents of the data and use it as its id/filename
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
			if( filename.exists() )
				throw new WebApplicationException( Status.CONFLICT);

			Files.write(filename.toPath(), bytes);
			return baseUri + "/" + id;
		} catch( IOException x  ) { 
			x.printStackTrace();
			throw new WebApplicationException( Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public byte[] download(String id) {
		try {
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
			if( !filename.exists() )
				throw new WebApplicationException(Status.NOT_FOUND );

			return Files.readAllBytes(filename.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException( Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void delete(String id) {
		try {
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
//			if( !filename.exists() )
//				throw new WebApplicationException(Status.NOT_FOUND );

			Files.deleteIfExists(filename.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException( Status.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void update(String id, byte[] bytes) {
		try {
			File filename = new File(ROOT_DIR + id + MEDIA_EXTENSION);
			if( !filename.exists() )
				throw new WebApplicationException(Status.NOT_FOUND );

			Files.write(filename.toPath(), bytes);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException( Status.INTERNAL_SERVER_ERROR);
		}
	}

}
