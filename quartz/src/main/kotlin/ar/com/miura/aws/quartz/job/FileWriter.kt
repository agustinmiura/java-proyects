package ar.com.miura.aws.quartz.job

import ar.com.miura.aws.quartz.config.Config
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.random.RandomGenerator
import javax.annotation.PostConstruct
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

@Component
class FileWriter(
    @Autowired val config:Config
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun postconstruct() {
        try {
            val path = Paths.get("${config.appDirectory}");
            val validPath = (path.exists() && path.isDirectory());
            if (!validPath) {
                path.createFile();
            }
        }catch(e:Exception) {
            logger.error(" Error ", e);
        }
    }

    fun writeFile() {
        try {
            val path = Paths.get("${config.appDirectory}/${getFileName()}");
            Files.createFile(path);
            Files.write(path, getFillerContent().toByteArray())
        } catch (e: Exception) {
            logger.error(" Error ", e);
        }
    }

    private fun getFileName(): String {
        val id = RandomGenerator.getDefault().nextLong()
        return "quartz-$id.txt";
    }

    private fun getFillerContent():String {
        return " random content : " + LocalDateTime.now();
    }

}