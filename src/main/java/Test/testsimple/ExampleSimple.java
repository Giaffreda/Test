package Test.testsimple;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

import org.apache.log4j.BasicConfigurator;

/*
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
*/
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
public class ExampleSimple {
	  final private Peer peer;
	  final private PeerDHT _dht;
	  final private int DEFAULT_MASTER_PORT=4000;
	    public ExampleSimple(int peerId) throws Exception {
	    	/*
	        peer = new PeerBuilderDHT(new PeerBuilder(Number160.createHash(peerId)).ports(4000 + peerId).start()).start();
	        
	        FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(InetAddress.getByName("127.0.0.1")).ports(4001).start();
	        fb.awaitUninterruptibly();
	        if(fb.isSuccess()) {
	            peer.peer().discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
	        }
	       */ 
	    	/* peer = new PeerBuilderDHT(new PeerBuilder(Number160.createHash(peerId)).ports(4000 + peerId).start()).start();
		        String master="127.0.0."+peerId;
		        FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(InetAddress.getByName(master)).ports(4001).start();
		        fb.awaitUninterruptibly();
		        
				
		        if(fb.isSuccess()) {
		            peer.peer().discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		        }*/
	    	//MessageListenerImpl _listner=new MessageListenerImpl(peerId);
	    	 peer= new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT).start();
	 		_dht = new PeerBuilderDHT(peer).start();	
	 		String master="127.0.0."+peerId;
	 		System.out.println("AAAAA"+master);
	 		FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(master)).ports(DEFAULT_MASTER_PORT).start();
	 		fb.awaitUninterruptibly();
	 		if(fb.isSuccess()) {
	 			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
	 		}else {
	 			throw new Exception("Error in master peer bootstrap.");
	 		}
	 		
	 		/*peer.objectDataReply(new ObjectDataReply() {
	 			
	 			public Object reply(PeerAddress sender, Object request) throws Exception {
	 				return _listener.parseMessage(request);
	 			}
	 		});*/
		       
	    }

	    public static void main(String[] args) throws NumberFormatException, Exception {
	    	 System.out.println("twst");
	    	// BasicConfigurator.configure();
	        ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));
	        
	        if (args.length == 3) {
	            dns.store(args[1], args[2]);
	        }
	        if (args.length == 2) {
	            System.out.println("Name:" + args[1] + " IP:" + dns.get(args[0]));
	        }
	    }

	    private String get(String name) throws ClassNotFoundException, IOException {
	        FutureGet futureGet = _dht.get(Number160.createHash(Integer.parseInt(name))).start();
	        futureGet.awaitUninterruptibly();
	        try {
	        if (futureGet.isSuccess()) {
	        	/*HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>)  futureGet.dataMap().values().iterator().next().object();
				PeerDHT.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				PeerDHT.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly().toString();
				return "x";*/
	        	
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				System.out.print("teeest");
				for(PeerAddress peer:peers_on_topic)
				{
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(name).start();
					futureDirect.awaitUninterruptibly();
				}
				return "x";
	        }}catch (Exception e) {
				// TODO: handle exception
			}
	        return "not found";
	    }

	    private void store(String name, String ip) throws IOException {
	        _dht.put(Number160.createHash(name)).data(new Data(ip)).start().awaitUninterruptibly();
	    }
}
