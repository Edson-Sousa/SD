package microgram.impl.srv.rest;

import javax.ws.rs.WebApplicationException;

import microgram.api.java.Media;
import microgram.api.java.Result;
import microgram.api.rest.RestMediaStorage;
import microgram.impl.srv.shared.JavaMedia;

public class RestMediaResources extends RestResource implements RestMediaStorage {
	
	final Media impl;
	final String baseUri;
	
	public RestMediaResources(String baseUri ) {
		this.baseUri = baseUri + RestMediaStorage.PATH;
		this.impl = new JavaMedia();
	}
	
	@Override
	public String upload(byte[] bytes) {
		Result<String> result = impl.upload(bytes);
		if (result.isOK())
			return baseUri + "/" + result.value();
		else
			throw new WebApplicationException(super.statusCode(result));
	}

	@Override
	public byte[] download(String id) {
		Result<byte[]> result = impl.download( id );
		if( result.isOK() )
			return result.value();
		else
			throw new WebApplicationException( super.statusCode( result )) ;
}

	@Override
	public void delete(String id) {
		Result<Void> result = impl.delete(id);
		if( !result.isOK())
			throw new WebApplicationException( super.statusCode(result));
	}

	@Override
	public void update(String id, byte[] bytes) {
		Result<Void> result = impl.update(id, bytes);
		if( !result.isOK())
			throw new WebApplicationException( super.statusCode(result));
	}

/*	private static final String MEDIA_EXTENSION = ".jpg";
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
*/
}
