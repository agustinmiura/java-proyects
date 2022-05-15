package ar.com.miura.aws.quartz.job

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.random.RandomGenerator

@Component
class MockJob {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun writeFile() {
        try {
            val path = Paths.get("/tmp/job/${getFileName()}");
            Files.createFile(path);
            val content = " random content : " + LocalDateTime.now();
            Files.write(path, content.toByteArray())
        } catch (e: Exception) {
            logger.error(" Error ", e);
        }
    }

    private fun getFileName(): String {
        val id = RandomGenerator.getDefault().nextLong()
        return "quartz-$id.txt";
    }


}