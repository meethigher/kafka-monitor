package top.meethigher.kafkamonitor;

import org.junit.Test;
import top.meethigher.kafkamonitor.service.impl.DefaultKafkaMonitor;

public class KafkaMonitorTest {

    @Test
    public void name() {
        DefaultKafkaMonitor monitor = new DefaultKafkaMonitor("192.168.110.67:9092");
        String s = monitor.describeConsumerPartition();
        System.out.println(s);
    }
}
