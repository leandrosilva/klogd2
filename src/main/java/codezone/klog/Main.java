package codezone.klog;

import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;

import codezone.klog.event.KafkaEventHandler;

public class Main {

	public static void main(String[] args) {
		SyslogServerIF syslogServer = SyslogServer.getThreadedInstance("udp");
		
		SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();
		syslogServerConfig.setHost("127.0.0.1");
		syslogServerConfig.setPort(1514);

		SyslogServerEventHandlerIF kafkaEventHandler = KafkaEventHandler.create();
		syslogServerConfig.addEventHandler(kafkaEventHandler);
		
		while (true) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// nothing!
			}
		}
	}
}
