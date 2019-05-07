package discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Discovery {

	private static Logger Log = Logger.getLogger(Discovery.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
	}
	
	
	static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);
	static final int DISCOVERY_PERIOD = 1000;
	static final int DISCOVERY_TIMEOUT = 30000;

	private static final String DELIMITER = "\t";

	/**
	 * 
	 * Announces periodically a service in a separate thread .
	 * 
	 * @param serviceName the name of the service being announced.
	 * @param serviceURI the location of the service
	 */
	public static void announce(String serviceName, String serviceURI) {
		Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s", DISCOVERY_ADDR, serviceName, serviceURI));
		
		byte[] pktBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();

		DatagramPacket pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);
		new Thread(() -> {
			try (DatagramSocket ms = new DatagramSocket()) {
				for (;;) {
					ms.send(pkt);
					Thread.sleep(DISCOVERY_PERIOD);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}


	/**
	 * Performs discovery of instances of the service with the given name.
	 * 
	 * @param  serviceName the name of the service being discovered
	 * @param  minRepliesNeeded the required number of service replicas to find. 
	 * @return an array of URI with the service instances discovered. Returns an empty, 0-length, array if the service is not found within the alloted time.
	 * 
	 */
	public static URI[] findUrisOf(String serviceName, int minRepliesNeeded) {
		Set<URI> replies = new HashSet<URI>();
		try ( MulticastSocket socket = new MulticastSocket(DISCOVERY_ADDR.getPort()) ) {
			long startTime = System.currentTimeMillis();
			socket.joinGroup( DISCOVERY_ADDR.getAddress() );
			socket.setSoTimeout(DISCOVERY_TIMEOUT);
			try {				
				while( true ) {
					byte[] buffer = new byte[65536];
					DatagramPacket packet = new DatagramPacket( buffer, buffer.length );
					socket.receive( packet );
					long arriveTime = System.currentTimeMillis();
					String s = new String( packet.getData(), 0, packet.getLength() );
					String[] something = s.split( DELIMITER );
					URI u = URI.create(something[1]);
					if( !replies.contains(u) && something[0].equals(serviceName) ) {						
						replies.add(u);
						socket.setSoTimeout(DISCOVERY_TIMEOUT);
					} else
						socket.setSoTimeout((int) (DISCOVERY_TIMEOUT - (arriveTime - startTime)));
					
					if( replies.size() >= minRepliesNeeded )
						break;
				}
			} catch( SocketTimeoutException s ) {
				;				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return replies.toArray(new URI[replies.size()]);
	}	
}
