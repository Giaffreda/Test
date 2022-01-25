package Test.testsimple;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class Connector {
	private Peer peer;
	 private PeerDHT _dht;
	 final private int DEFAULT_MASTER_PORT=4000;
	 public int peerId;
	 private App test;
	 public Connector(int id, String adress, final MessageListener _listener) throws Exception {
		 peerId=id;
		 peer= new PeerBuilder(Number160.createHash(peerId)).ports(DEFAULT_MASTER_PORT+peerId).start();
		 	_dht = new PeerBuilderDHT(peer).start();
		 	FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(adress)).ports(DEFAULT_MASTER_PORT).start();
	 		fb.awaitUninterruptibly();
	 		if(fb.isSuccess()) {
	 			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
	 		}else {
	 			throw new Exception("Error in master peer bootstrap.");
	 		}
	 		peer.objectDataReply(new ObjectDataReply() {
	 			
	 			public Object reply(PeerAddress sender, Object request) throws Exception {
	 				System.out.println("obj reply");
	 				return _listener.parseMessage(request);
	 			}
	 		});
	}
	  public void searchFriends(String name, String nickName, String profilekey) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			//nickName="test";
			try {
				/*
				 * FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				HashSet<PeerAddress> peers_on_topic;
	        _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
	        peers_on_topic.add(_dht.peer().peerAddress());
	        _dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();*/
			if (futureGet.isSuccess()) {
				System.out.println("future search friends succes");
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				test=new App(profilekey, peerId, nickName);
				//_dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				//peers_on_topic.add(_dht.peer().peerAddress());
				
				//_dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				/*
				 *  _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
	        peers_on_topic.add(_dht.peer().peerAddress());
	        _dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
	        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				HashSet<PeerAddress> peers_on_topic;
	        _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
	        peers_on_topic.add(_dht.peer().peerAddress());
	        _dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
	        System.out.print("put test");
	        */
				//nickName="il mio id "+nickName+"le mie risposte "+answer;
				System.out.println("nick name per send di test ="+test.getNickname());
				test.setMytype(App.type.friends);
				for(PeerAddress peer:peers_on_topic){
					//System.out.println("peer ="+peer);
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(test).start();
					futureDirect.awaitUninterruptibly();
				}
				_dht.put(Number160.createHash(nickName)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());

				//_dht.put(Number160.createHash(nickName)).data(new Data( peers_on_topic)).start().awaitUninterruptibly();
				
				//_dht.put(Number160.createHash(nickName)).data(new Data( peers_on_topic)).start().awaitUninterruptibly();
				//_dht.put(Number160.createHash(nickName)).data(new Data( new HashSet<PeerAddress>())).start().awaitUninterruptibly();
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    }
	  
	  public void searchFriends2(String name, String nickName, String profilekey) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			//nickName="test";
			try {
				if (futureGet.isSuccess()) {
				System.out.println("future search friends succes");
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				test=new App(profilekey, peerId, nickName);
				//_dht.put(Number160.createHash(nickName)).data(new Data(new HashSet<PeerAddress>())).start().awaitUninterruptibly();
				//peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(nickName)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				System.out.println("nick name per send di test ="+test.getNickname());
				test.setMytype(App.type.friends);
				for(PeerAddress peer:peers_on_topic){
					//System.out.println("peer ="+peer);
					if(peer!=_dht.peer().peerAddress()) {
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(test).start();
					futureDirect.awaitUninterruptibly();
					}
				}
				//_dht.put(Number160.createHash(nickName)).data(new Data(new HashSet<PeerAddress>())).start().awaitUninterruptibly();
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    }
	  public void connection(String name, String nickName) {
		  FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			//nickName="test";
			try {
			if (futureGet.isSuccess()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				//_dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(nickName)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				System.out.println("future connection succes");
				System.out.println("future connection "+nickName);

				for(PeerAddress peer:peers_on_topic){
					//String message=name+"ha accettato";
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(name).start();
					futureDirect.awaitUninterruptibly();
				}
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	  }
	  public void store(String name, String ip) throws IOException {
	    	try {
	    	FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				HashSet<PeerAddress> peers_on_topic;
				 peers_on_topic=new HashSet<PeerAddress>();
	       // _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
	        peers_on_topic.add(_dht.peer().peerAddress());
	        _dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
	        System.out.print("put test");
			}
	    } catch (Exception e) {
			e.printStackTrace();
		}
	    } 
	  public void getFriends(String name, String profile) throws IOException {
	    	FutureGet futureGet = _dht.get(Number160.createHash(profile)).start();
	    	System.out.println("future status"+futureGet.toString());
			futureGet.awaitUninterruptibly();
			try {
			if (futureGet.isSuccess()&&futureGet.isEmpty()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				//_dht.put(Number160.createHash(profile)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				//peers_on_topic.add(_dht.peer().peerAddress());
				//_dht.put(Number160.createHash(profile)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				System.out.println("future cgetfriends succes");
				System.out.println("future getfriends "+profile);
				test=new App("prova", peerId,name);
				test.setMytype(App.type.response);
				for(PeerAddress peer:peers_on_topic){
					String message=name+"ha accettato";
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(test).start();
			
					futureDirect.awaitUninterruptibly();
				}
			} else {
				System.out.println("error1");
			}
			}catch (Exception e) {
				System.out.println("error2");
				
			}
			}
	  public boolean getFriends2(String name, String profile) throws IOException {
		  try {
				FutureGet futureGet = _dht.get(Number160.createHash(profile)).start();
				futureGet.addListener(new BaseFutureAdapter<FutureGet>() {
					 @Override
					 public void operationComplete(FutureGet future) throws Exception {
					  if(future.isSuccess()) { // this flag indicates if the future was successful
					   System.out.println("success");
					   
					  } else {
					   System.out.println("failure");
					  }
					 }
					}).awaitListenersUninterruptibly();
				
				if (futureGet.isSuccess()&& (!profile.equals(name))) {
					if(futureGet.isEmpty() ) {
						System.out.println("is empty");
						return false;
					}
					HashSet<PeerAddress> peers_on_topic;
					peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
					//_dht.put(Number160.createHash(profile)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
					test=new App("prova", peerId,name);
					//peers_on_topic.add(_dht.peer().peerAddress());
					_dht.put(Number160.createHash(profile)).data(new Data(peers_on_topic)).start().awaitListenersUninterruptibly();
					test.setMytype(App.type.response);
					for(PeerAddress peer:peers_on_topic){
						String message=name+"ha accettato";
						System.out.println("send response from "+name+" to "+profile);
						FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(test).start();
				
						futureDirect.awaitListenersUninterruptibly();
					}
					return true;
					
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return false;
			}
	  public boolean sendMessage(String destination, String source,Object message) {
	    	FutureGet futureGet = _dht.get(Number160.createHash(destination)).start();
	        futureGet.awaitUninterruptibly();
	        try {
		        if (futureGet.isSuccess()) {
		        	HashSet<PeerAddress> peers_on_topic;
					peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
					App test= new App((String)message,peerId,source);
					test.setMytype(App.type.chat);
					for(PeerAddress peer:peers_on_topic)
					{
						FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(test).start();
						futureDirect.awaitUninterruptibly();
					}
					return true;
		        }}catch (Exception e) {
					// TODO: handle exception
				}
	    	return false;
	    }
	   public String get(String name) throws ClassNotFoundException, IOException {
	        FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
	        futureGet.awaitUninterruptibly();
	        try {
	        if (futureGet.isSuccess()) {
	        
				
				
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(name)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				//peers_on_topic.add(peer.peerAddress());
				/*System.out.print("teeest");
				for(PeerAddress peer:peers_on_topic)
				{
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(name).start();
					futureDirect.awaitUninterruptibly();
				}*/
				return "x";
	        }}catch (Exception e) {
				// TODO: handle exception
			}
	        return "not found";
	    }
}
