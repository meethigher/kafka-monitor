package top.meethigher.kafkamonitor;

import org.junit.Test;
import top.meethigher.kafkamonitor.model.DescribeConsumerPartition;
import top.meethigher.kafkamonitor.model.DescribeTopicPartition;
import top.meethigher.kafkamonitor.service.impl.DefaultKafkaMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

public class KafkaMonitorTest {

    @Test
    public void name() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(new File(System.getProperty("user.dir").replace("\\", "/"), "kafka.properties"))) {
            properties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        DefaultKafkaMonitor monitor = new DefaultKafkaMonitor(properties);
        List<DescribeConsumerPartition> describeConsumerPartitions = monitor.describeConsumerPartition();
        System.out.println(describeConsumerPartitions);
        List<DescribeTopicPartition> testTopic = monitor.describeTopic("test_topic");
        System.out.println(testTopic);
    }
}
