package Test.testsimple;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import net.tomp2p.connection.SendBehavior.SendMethod;
//import it.isislab.p2p.chat.MessageListener;
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
	  private String answer;
	    public ExampleSimple(int peerId ,String master, final MessageListener _listener) throws Exception {
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
	    	 peer= new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT+peerId).start();
	 		_dht = new PeerBuilderDHT(peer).start();	
	 		
	 		System.out.println("AAAAA"+master);
	 		FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(master)).ports(DEFAULT_MASTER_PORT).start();
	 		fb.awaitUninterruptibly();
	 		if(fb.isSuccess()) {
	 			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
	 		}else {
	 			throw new Exception("Error in master peer bootstrap.");
	 		}
	 		/*
	 		peer.objectDataReply(new ObjectDataReply() {
	 			
	 			public Object reply(PeerAddress sender, Object request) throws Exception {
	 				return _listener.parseMessage(request);
	 			}
	 		});*/
		       
	    }

	    public static void main(String[] args) throws NumberFormatException, Exception {
	    	 System.out.println("twst");
	    	/* class MessageListenerImpl implements MessageListener{
	 			int peerid;
	 		
	 			public MessageListenerImpl(int peerid)
	 			{
	 				this.peerid=peerid;

	 			}
	 			public Object parseMessage(Object obj) {
	 				
	 				TextIO textIO = TextIoFactory.getTextIO();
	 				TextTerminal terminal = textIO.getTextTerminal();
	 				terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
	 				return "success";
	 			}

	 		}*/
	    	 TextIO textIO = TextIoFactory.getTextIO();
		     TextTerminal terminal = textIO.getTextTerminal();
	    	 SemanticHarmonySocialNetworkImpl ex= new SemanticHarmonySocialNetworkImpl(Integer.parseInt(args[0]), args[1]);
	    	List<String> question= ex.getUserProfileQuestions();
	    	List <Integer> answer =new ArrayList<>();
	    	for (int i=0; i<question.size();i++) {
	    		answer.add(Integer.parseInt(question.get(i)));
	    	}
	    	String key= ex.createAuserProfileKey(answer);
	    	ex.join(key, textIO.newStringInputReader().withDefaultValue("default").read("inserisci nick"));
	    	System.out.println(ex.con.peerId);
	    	while(true) {
				if(textIO.newBooleanInputReader().withDefaultValue(false).read("exit?")) {
					System.exit(0);
				}
				}
	    	 // BasicConfigurator.configure();
	        /*ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]),args[2],new MessageListenerImpl(Integer.parseInt(args[0])));
	        TextIO textIO = TextIoFactory.getTextIO();
	        TextTerminal terminal = textIO.getTextTerminal();;
	        dns.setAnswer(textIO.newStringInputReader().withDefaultValue("default").read("risposte"));
	        if (args.length == 4) {
	            dns.store(args[1], args[2]);
	        }
	        if (args.length == 3) {
	            System.out.println("Name:" + args[1] + " IP:" + dns.get(args[1]));
	        }
	        while(true) {
				terminal.printf("wait");
				if(textIO.newBooleanInputReader().withDefaultValue(false).read("exit?")) {
					System.exit(0);
				}else {
				
					if(textIO.newBooleanInputReader().withDefaultValue(false).read("cerca?")) {
						dns.searchFriends(args[1], args[0]);
					}
					else{
						if(textIO.newBooleanInputReader().withDefaultValue(false).read("eccetta?")) {
							//dns.getFriends(args[0], textIO.newStringInputReader().withDefaultValue("default").read("Messaggio da inviare"));
							dns.FriendsbyId(args[0], textIO.newStringInputReader().withDefaultValue("default").read("profilo"), textIO.newStringInputReader().withDefaultValue("default").read("risposte"));
						}
						else {
							if(textIO.newBooleanInputReader().withDefaultValue(false).read("vuoi inviare un messaggio?")) {
								String message=textIO.newStringInputReader().withDefaultValue("default").read("Messaggio da inviare");
								String name=textIO.newStringInputReader().withDefaultValue("default").read("a chi?");
								dns.sendMessage(name, message);
					}
					}
					}
				}
				}*/
	    }
	    private boolean sendMessage(String name, Object message) {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
	        futureGet.awaitUninterruptibly();
	        try {
		        if (futureGet.isSuccess()) {
		        	HashSet<PeerAddress> peers_on_topic;
					peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
					for(PeerAddress peer:peers_on_topic)
					{
						FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(message).start();
						futureDirect.awaitUninterruptibly();
					}
					return true;
		        }}catch (Exception e) {
					// TODO: handle exception
				}
	    	return false;
	    }
	    private String get(String name) throws ClassNotFoundException, IOException {
	        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
	        futureGet.awaitUninterruptibly();
	        try {
	        if (futureGet.isSuccess()) {
	        	/*HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>)  futureGet.dataMap().values().iterator().next().object();
				PeerDHT.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				PeerDHT.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly().toString();
				return "x";*/
	        	/*
	        	 * FutureGet futureGet = _dht.get(Number160.createHash(_topic_name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				for(PeerAddress peer:peers_on_topic)
				{
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(_obj).start();
					futureDirect.awaitUninterruptibly();
				}
				
				return true;*/
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				//peers_on_topic.add(peer.peerAddress());
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
	    	try {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				HashSet<PeerAddress> peers_on_topic;
	        _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
	        peers_on_topic.add(_dht.peer().peerAddress());
	        _dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
	        System.out.print("put test");
			}
	    } catch (Exception e) {
			e.printStackTrace();
		}
	    }  private void searchFriends(String name, String profile) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			try {
			if (futureGet.isSuccess()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(profile)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				profile="il mio id "+profile+"le mie risposte "+answer;
				for(PeerAddress peer:peers_on_topic){
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(profile).start();
					futureDirect.awaitUninterruptibly();
				}
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    }
	    
	  
	    private void getFriends(String name, String profile) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(profile)).start();
			futureGet.awaitUninterruptibly();
			try {
			if (futureGet.isSuccess()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				//_dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(profile)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				
				for(PeerAddress peer:peers_on_topic){
					String message=name+"ha accettato";
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(name).start();
					futureDirect.awaitUninterruptibly();
				}
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    }
	    private void FriendsbyId(String name, String profile, String hisanswer) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(profile)).start();
			futureGet.awaitUninterruptibly();
			try {
			if (futureGet.isSuccess()&&hammingDistance(answer, hisanswer)<3) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				//_dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(profile)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				
				for(PeerAddress peer:peers_on_topic){
					String message=name+"ha accettato";
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(message).start();
					futureDirect.awaitUninterruptibly();
				}
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
			}
	    public int hammingDistance(String a, String b) {
			int count=0;
			for (int i=0; i<a.length();i++) {
				if(a.charAt(i)!=b.charAt(i))
					count++;
			}
			return count;
		}
	    public void setAnswer(String a) {
	    	answer=a;
	    }
	    
}
