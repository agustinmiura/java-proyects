package ar.com.miura.aws.kinesis

import org.springframework.stereotype.Component
import software.amazon.kinesis.processor.ShardRecordProcessor
import software.amazon.kinesis.processor.ShardRecordProcessorFactory

@Component
class CustomShardProcessorFactory: ShardRecordProcessorFactory {
    override fun shardRecordProcessor(): ShardRecordProcessor? {
        return CustomShardProcessor()
    }
}