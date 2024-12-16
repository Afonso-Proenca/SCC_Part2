package srv;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import java.util.logging.Logger;
import cache.RedisCache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import utils.JSON;

public class RedisLayer {
	Map<String, Session> sessions = new ConcurrentHashMap<>();
	private final Jedis redisInstance = RedisCache.getCachePool().getResource();
	private static final String SESSION = "session:";
	private static final Logger Log = Logger.getLogger(RedisLayer.class.getName());

	private static RedisLayer instance;

	synchronized public static RedisLayer getInstance() {
		if(instance == null )
			instance = new RedisLayer();
		return instance;
	}
		public void putSession(Session s) {
		try {
			redisInstance.set(SESSION + s.uid(), JSON.encode(s));
		} catch(JedisException e) {
			Log.warning("Redis access failed.");
		}
	}
	
	public Session getSession(String uid) {
		try {
			return JSON.decode(redisInstance.get(SESSION + uid), Session.class);
		} catch(JedisException e) {
			Log.warning("Redis access failed.");
			return null;
		}
	}
	/*
	public void putSession(Session s) {
		sessions.put(s.uid(), s);
	}
	
	public Session getSession(String uid) {
		return sessions.get(uid);
	}*/
}
