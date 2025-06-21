package top.meethigher.kafkamonitor.model;

import java.util.List;

/**
 * topic消费
 *
 * @author chenchuancheng
 * @since 2022/12/22 10:03
 */
public class DescribeTopicPartition {

    /**
     * topic名称
     */
    private String topic;

    /**
     * 分区编号
     */
    private String partition;

    /**
     * leader所属节点编号
     */
    private String leader;

    /**
     * 该分区备份所在节点编号
     */
    private List<String> replicas;

    /**
     * 具备成为领导节点的备选节点
     */
    private List<String> isr;

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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public List<String> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<String> replicas) {
        this.replicas = replicas;
    }

    public List<String> getIsr() {
        return isr;
    }

    public void setIsr(List<String> isr) {
        this.isr = isr;
    }
}
