package ar.com.miura.aws.dynamo.controller

import ar.com.miura.aws.dynamo.dto.MovieDto
import ar.com.miura.aws.dynamo.service.MovieService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class DynamoController(
    @Autowired val movieService: MovieService
) {
    val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("movies/{id}")
    fun getById(@PathVariable("id") id:String):ResponseEntity<Map<String,Any>> {
        val found = movieService.getItem(id)
        if (found!=null) {
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping("movies")
    fun create(@RequestBody movie: MovieDto):ResponseEntity<String> {
        logger.info(" Create the movie ${movie} ")
        movieService.create(movie);
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("movies/{id}")
    fun delete(@PathVariable("id") id:String):ResponseEntity<String> {
        movieService.delete(id);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("movies/{id}")
    fun modify(@RequestBody movie: MovieDto, @PathVariable("id") id:String):ResponseEntity<String> {
        movieService.update(id, movie);
        return ResponseEntity.ok("ok");
    }

}
