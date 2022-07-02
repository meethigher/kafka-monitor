package top.meethigher.kafkamonitor.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import top.meethigher.kafkamonitor.model.DescribeConsumerPartition;
import top.meethigher.kafkamonitor.service.KafkaMonitor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认实现
 */
public class DefaultKafkaMonitor implements KafkaMonitor<String, String> {

    private final String bootstrapServers;

    public DefaultKafkaMonitor(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    @Override
    public String describeConsumerPartition(String bootstrapServers) {
        try {
            List<DescribeConsumerPartition> list = new LinkedList<>();
            Properties properties = new Properties();
            properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            AdminClient adminClient = AdminClient.create(properties);
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
            adminClient.close();
            return JSON.toJSONString(list);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String describeConsumerPartition() {
        return describeConsumerPartition(this.bootstrapServers);
    }
}
