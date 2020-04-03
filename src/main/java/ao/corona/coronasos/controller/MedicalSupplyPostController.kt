/*
Apache2 License Notice
Copyright 2018 Alex Barry

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ao.corona.coronasos.controller

import ao.corona.coronasos.common.asResponseEntity
import ao.corona.coronasos.entity.MedicalSupplyPost
import ao.corona.coronasos.repo.MedicalSupplyPostRepository
import ao.corona.coronasos.service.MedicalSupplyPostService
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Rest Controller defining the Project API.
 * Responsible for handling and responding to all Project API Requests.
 * Injects Spring Data Mongo Repository allowing access to standard Mongo operations
 */
@RestController
@RequestMapping("/v1/posts", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
class MedicalSupplyPostController {

    @Autowired
    private lateinit var medicalSupplyPostService: MedicalSupplyPostService

    /**
     * Get a Post.
     */
    @GetMapping("/{id}")
    fun getPost(@PathVariable id: String): ResponseEntity<MedicalSupplyPost> =
            medicalSupplyPostService.getPostById(id).map { it.asResponseEntity() }
                    .orElse(ResponseEntity.notFound().build())

    /**
     * Query for Posts.
     */
    @GetMapping("/allPosts")
    fun getPosts(
            @RequestParam(value = "num_records", defaultValue = "10") recordsInPage: Int,
            @RequestParam(value = "page", defaultValue = "0") pageNum: Int): ResponseEntity<List<MedicalSupplyPost>> {
        return medicalSupplyPostService.getAllPosts(recordsInPage, pageNum).asResponseEntity()
    }

    /**
     * Delete a Project.
     */
    @DeleteMapping("/v1/posts/{id}")
    @ResponseStatus(OK)
    fun deletePost(@PathVariable id: String) = medicalSupplyPostService.deletePostById(id)

    /**
     * Create a new Project.
     */
    @PostMapping
    fun createPost(@RequestBody inpPost: MedicalSupplyPost) = medicalSupplyPostService.createPost(inpPost).asResponseEntity()

    companion object {
        // Medical Supply Post Controller Logger
        private val logger = LogManager.getLogger("coronasos.MedicalSupplyPostController")
    }
}