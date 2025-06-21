package top.meethigher.kafkamonitor.service;



import top.meethigher.kafkamonitor.model.DescribeConsumerPartition;
import top.meethigher.kafkamonitor.model.DescribeTopicPartition;

import java.util.List;

/**
 * kafka监控
 *
 * @author chenchuancheng
 * @since 2022/6/30 11:11
 */
public interface KafkaMonitor {


    /**
     * 分区为最小单位的消费情况查询
     *
     * @return
     */
    List<DescribeConsumerPartition> describeConsumerPartition();


    /**
     * topic详情
     *
     * @return
     */
    List<DescribeTopicPartition> describeTopic(String topic);
}
