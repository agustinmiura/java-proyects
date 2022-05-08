package ar.com.miura.aws.kinesis

import ar.com.miura.aws.kinesis.models.StockTrade
import com.google.gson.Gson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import software.amazon.kinesis.exceptions.InvalidStateException
import software.amazon.kinesis.exceptions.ShutdownException
import software.amazon.kinesis.exceptions.ThrottlingException
import software.amazon.kinesis.lifecycle.events.*
import software.amazon.kinesis.processor.RecordProcessorCheckpointer
import software.amazon.kinesis.processor.ShardRecordProcessor
import software.amazon.kinesis.retrieval.KinesisClientRecord
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
@Component
class CustomShardProcessor : ShardRecordProcessor {

    val gson = Gson()

    val logger = LoggerFactory.getLogger(javaClass)

    private var kinesisShardId: String? = null

    override fun initialize(initializationInput: InitializationInput?) {
        kinesisShardId = initializationInput!!.shardId()
        logger.debug("Initializing record processor for shard: " + kinesisShardId);
        logger.debug("Initializing @ Sequence: " + initializationInput!!.extendedSequenceNumber().toString());
        logger.debug(" Initialize ")
    }

    override fun processRecords(processRecordsInput: ProcessRecordsInput?) {
        logger.debug("Processing " + processRecordsInput!!.records().size + " record(s)");
        processRecordsInput.records().forEach({ record -> processRecord(record) })
        checkpoint(processRecordsInput.checkpointer());
    }

    override fun leaseLost(leaseLostInput: LeaseLostInput?) {
        logger.debug("Lost lease, so terminating.");
    }

    override fun shardEnded(shardEndedInput: ShardEndedInput?) {
        try {
            // Important to checkpoint after reaching end of shard, so we can start processing data from child shards.
            logger.debug("Reached shard end checkpointing.")
            shardEndedInput!!.checkpointer().checkpoint()
        } catch (e: ShutdownException) {
            throw e;
        } catch (e: InvalidStateException) {
            throw e;
        }
    }

    override fun shutdownRequested(shutdownRequestedInput: ShutdownRequestedInput?) {
        logger.debug("Scheduler is shutting down, checkpointing.");
        checkpoint(shutdownRequestedInput!!.checkpointer())
    }

    private fun processRecord(record: KinesisClientRecord) {
        try {
            val charBuffer: CharBuffer = StandardCharsets.US_ASCII.decode(record.data())
            val text: String = charBuffer.toString()
            val stockTrade = gson.fromJson(text, StockTrade::class.java)
            logger.debug(" Record data: " + stockTrade)
        } catch (e: Exception) {
            throw e;
        }
    }

    private fun checkpoint(checkpointer: RecordProcessorCheckpointer) {
        try {
            checkpointer.checkpoint()
        } catch (se: ShutdownException) {
            // Ignore checkpoint if the processor instance has been shutdown (fail over).
            //logger.info("Caught shutdown exception, skipping checkpoint.", se)
            throw se;
        } catch (e: ThrottlingException) {
            // Skip checkpoint when throttled. In practice, consider a backoff and retry policy.
            //logger.error("Caught throttling exception, skipping checkpoint.", e)
            throw e;
        } catch (e: InvalidStateException) {
            // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
            //logger.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e)
            throw e;
        }
    }
}