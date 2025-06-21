package top.meethigher.kafkamonitor.service.impl;


import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import top.meethigher.kafkamonitor.model.DescribeConsumerPartition;
import top.meethigher.kafkamonitor.model.DescribeTopicPartition;
import top.meethigher.kafkamonitor.service.KafkaMonitor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认实现
 */
public class DefaultKafkaMonitor implements KafkaMonitor {

    private final AdminClient adminClient;

    public DefaultKafkaMonitor(String bootstrapServers) {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        adminClient = AdminClient.create(properties);
    }

    public DefaultKafkaMonitor(Properties properties) {
        adminClient = AdminClient.create(properties);
    }

    @Override
    public List<DescribeConsumerPartition> describeConsumerPartition() {
        List<DescribeConsumerPartition> list = new LinkedList<>();
        try {
            //拿到所有的消费者组id集合
            Collection<ConsumerGroupListing> consumerGroupListings = adminClient.listConsumerGroups().all().get();
            Set<String> groupIdSet = consumerGroupListings.stream().map(ConsumerGroupListing::groupId).collect(Collectors.toSet());
            //拿到对消费者的详细情况描述
            Map<String, ConsumerGroupDescription> descriptionMap = adminClient.describeConsumerGroups(groupIdSet).all().get();
            //对描述组，进行遍历，拿到具体某个分区的消费信息
            descriptionMap.forEach((k, v) -> {
                try {
                    String groupId = v.groupId();
                    //元数据，包含消费组、topic、分区、实际偏移、终端偏移、消费者信息(消费id、消费ip、客户端id)
                    Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get();
                    topicPartitionOffsetAndMetadataMap.forEach((kk, vv) -> {
                        try {
                            DescribeConsumerPartition describeConsumerPartition = new DescribeConsumerPartition();
                            describeConsumerPartition.setGroup(groupId);
                            describeConsumerPartition.setTopic(kk.topic());
                            describeConsumerPartition.setPartition(String.valueOf(kk.partition()));
                            describeConsumerPartition.setCurrentOffset(vv.offset());
                            ListOffsetsResult.ListOffsetsResultInfo listOffsetsResultInfo = adminClient.listOffsets(Collections.singletonMap(kk, OffsetSpec.latest())).all().get().get(kk);
                            describeConsumerPartition.setLogEndOffset(listOffsetsResultInfo.offset());
                            describeConsumerPartition.setLag(describeConsumerPartition.getLogEndOffset() - describeConsumerPartition.getCurrentOffset());
                            Collection<MemberDescription> members = v.members();
                            for (MemberDescription member : members) {
                                Set<TopicPartition> topicPartitions = member.assignment().topicPartitions();
                                if (topicPartitions.contains(kk)) {
                                    describeConsumerPartition.setClientId(member.clientId());
                                    describeConsumerPartition.setConsumerId(member.consumerId());
                                    describeConsumerPartition.setHost(member.host());
                                }
                            }
                            list.add(describeConsumerPartition);
                        } catch (Exception ignored) {

                        }
                    });
                } catch (Exception ignored) {
                }
            });
        } catch (Exception ignore) {
        }
        return list;
    }

    @Override
    public List<DescribeTopicPartition> describeTopic(String topic) {
        List<DescribeTopicPartition> list = new LinkedList<>();
        try {
            List<String> topicList = new LinkedList<>();
            topicList.add(topic);
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(topicList);
            Map<String, KafkaFuture<TopicDescription>> topicNameMap = describeTopicsResult.values();
            if (!topicNameMap.containsKey(topic)) {
                return null;
            }
            KafkaFuture<TopicDescription> topicDescriptionKafkaFuture = topicNameMap.get(topic);
            TopicDescription topicDescription = topicDescriptionKafkaFuture.get();
            List<TopicPartitionInfo> topicPartitionInfos = topicDescription.partitions();

            for (TopicPartitionInfo partitionInfo : topicPartitionInfos) {
                DescribeTopicPartition describeTopicPartition = new DescribeTopicPartition();
                describeTopicPartition.setTopic(topic);
                describeTopicPartition.setPartition(String.valueOf(partitionInfo.partition()));
                describeTopicPartition.setLeader(partitionInfo.leader().idString());
                describeTopicPartition.setReplicas(partitionInfo.replicas().stream().map(Node::idString).collect(Collectors.toList()));
                describeTopicPartition.setIsr(partitionInfo.isr().stream().map(Node::idString).collect(Collectors.toList()));
                list.add(describeTopicPartition);
            }
        } catch (Exception ignore) {

        }
        return list;
    }
}
