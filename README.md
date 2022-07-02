# kafka-monitor
kafka monitor, used to monitor consumer consumption

```java
public class DescribeConsumerPartition {


    /**
     * 本分区所在消费topic
     */
    private String topic;

    /**
     * 本分区所在消费group
     */
    private String group;


    /**
     * 消费分区-最小单位
     */
    private String partition;

    /**
     * 本分区实际消费
     */
    private Long currentOffset;

    /**
     * 本分区应消费
     */
    private Long logEndOffset;

    /**
     * 本分区未消费
     */
    private Long lag;

    /**
     * 消费编号
     */
    private String consumerId;

    /**
     * 消费者ip
     */
    private String host;

    /**
     * 消费者客户端id
     */
    private String clientId;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public Long getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Long currentOffset) {
        this.currentOffset = currentOffset;
    }

    public Long getLogEndOffset() {
        return logEndOffset;
    }

    public void setLogEndOffset(Long logEndOffset) {
        this.logEndOffset = logEndOffset;
    }

    public Long getLag() {
        return lag;
    }

    public void setLag(Long lag) {
        this.lag = lag;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
```

