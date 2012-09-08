package codezone.klog.event;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

import org.json.JSONObject;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

public class KafkaEventHandler implements SyslogServerEventHandlerIF {

	private static final long serialVersionUID = 8483062836459978581L;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyy HH:mm:ss:S");
	private ProducerConfig producerConfig;

	public static SyslogServerEventHandlerIF create() {
		return new KafkaEventHandler();
	}
	
	private KafkaEventHandler() {
		producerConfig = newProducerConfig();
	}
	
	private ProducerConfig newProducerConfig() {
		String zookeeperLocation = System.getenv("ZOOKEEPER") != null ? System.getenv("ZOOKEEPER") : "127.0.0.1:2181";
		
		Properties props = new Properties();
		props.put("zk.connect", zookeeperLocation);
		props.put("serializer.class", "kafka.serializer.StringEncoder");

		return new ProducerConfig(props);
	}
	
	public void event(SyslogServerIF server, SyslogServerEventIF event) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("timestamp", dateFormatter.format(System.currentTimeMillis()));
		message.put("host", event.getHost());
		message.put("raw", new String(event.getRaw()));
		
		String messageAsJson = new JSONObject(message).toString();
		
		send(messageAsJson);
	}
	
	private void send(String message) {
		Producer<String, String> producer = new Producer<String, String>(producerConfig);
		ProducerData<String, String> data = new ProducerData<String, String>("klog2", message);
		producer.send(data);
		producer.close();
	}
}
