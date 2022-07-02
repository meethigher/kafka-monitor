package top.meethigher.kafkamonitor.service;

/**
 * kafka监控
 *
 * @author chenchuancheng
 * @since 2022/6/30 11:11
 */
public interface KafkaMonitor<K, V> {

    /**
     * 分区为最小单位的消费情况查询
     *
     * @param k 连接信息
     * @return
     */
    V describeConsumerPartition(K k);

    /**
     * 分区为最小单位的消费情况查询
     *
     * @return
     */
    V describeConsumerPartition();
}
