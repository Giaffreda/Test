package Test.testsimple;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

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

public class Connector {
	private Peer peer;
	 private PeerDHT _dht;
	 final private int DEFAULT_MASTER_PORT=4000;
	 public int peerId;
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
	 				return _listener.parseMessage(request);
	 			}
	 		});
	}
	  public void searchFriends(String name, String nickName, String answer) throws IOException {
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
				_dht.put(Number160.createHash(nickName)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(nickName)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				//nickName="il mio id "+nickName+"le mie risposte "+answer;
				for(PeerAddress peer:peers_on_topic){
					FutureDirect futureDirect = _dht.peer().sendDirect(peer).object(nickName).start();
					futureDirect.awaitUninterruptibly();
				}
			}
			}catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    }
	  public void connection(String name, String nickName) {
		  FutureGet futureGet = _dht.get(Number160.createHash(name)).start();
			futureGet.awaitUninterruptibly();
			nickName="test";
			try {
			if (futureGet.isSuccess()) {
				HashSet<PeerAddress> peers_on_topic;
				peers_on_topic = (HashSet<PeerAddress>) futureGet.dataMap().values().iterator().next().object();
				//_dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
				peers_on_topic.add(_dht.peer().peerAddress());
				_dht.put(Number160.createHash(nickName)).data(new Data(peers_on_topic)).start().awaitUninterruptibly();
				System.out.println("future connection succes");
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
	        _dht.put(Number160.createHash(name)).data(new Data( peers_on_topic=(new HashSet<PeerAddress>()))).start().awaitUninterruptibly();
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
}
