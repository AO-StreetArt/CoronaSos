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

package ao.corona.coronasos.controller;

import ao.corona.coronasos.repo.MedicalSupplyPostRepository;
import ao.corona.coronasos.entity.MedicalSupplyPost;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* Rest Controller defining the Project API.
* Responsible for handling and responding to all Project API Requests.
*/
@Controller
public class MedicalSupplyPostController {

  // Spring Data Mongo Repository allowing access to standard Mongo operations
  @Autowired
  MedicalSupplyPostRepository postsRepository;

  // Asset Controller Logger
  private static final Logger logger =
      LogManager.getLogger("coronasos.MedicalSupplyPostController");

  /**
  * Get a Post.
  */
  @GetMapping("/v1/posts/{id}")
  @ResponseBody
  public ResponseEntity<MedicalSupplyPost> getPost(@PathVariable String id) {
    logger.info("Responding to Get Request");
    HttpStatus returnCode = HttpStatus.OK;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");
    MedicalSupplyPost returnPost = null;
    Optional<MedicalSupplyPost> existingPost = postsRepository.findById(id);
    if (existingPost.isPresent()) {
      returnPost = existingPost.get();
    } else {
      returnCode = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
      logger.debug("No Project found");
      returnPost = new MedicalSupplyPost();
    }

    // Create and return the new HTTP Response
    return new ResponseEntity<MedicalSupplyPost>(returnPost, responseHeaders, returnCode);
  }

  /**
  * Query for Posts.
  */
  @GetMapping("/v1/posts")
  @ResponseBody
  public ResponseEntity<List<MedicalSupplyPost>> getPosts(
      @RequestParam(value = "num_records", defaultValue = "10") int recordsInPage,
      @RequestParam(value = "page", defaultValue = "0") int pageNum) {
    logger.info("Responding to Get All Request");
    HttpStatus returnCode = HttpStatus.OK;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");
    List<MedicalSupplyPost> returnPosts = new ArrayList<MedicalSupplyPost>();
    Pageable pageable = PageRequest.of(pageNum, recordsInPage, Sort.by("testInt"));

    Iterable<MedicalSupplyPost> existingPosts = postsRepository.findAll(pageable);
    for (MedicalSupplyPost post : existingPosts) {
      returnPosts.add(post);
    }

    // Create and return the new HTTP Response
    return new ResponseEntity<List<MedicalSupplyPost>>(returnPosts, responseHeaders, returnCode);
  }

  /**
  * Delete a Project.
  */
  @DeleteMapping("/v1/posts/{id}")
  @ResponseBody public ResponseEntity<String> deletePost(
      @PathVariable String id) {
    logger.info("Responding to Delete Request");
    HttpStatus returnCode = HttpStatus.OK;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    postsRepository.deleteById(id);

    return new ResponseEntity<String>(id, responseHeaders, returnCode);
  }

  /**
   * Create a new Project.
   */
  @PostMapping("/v1/posts")
  @ResponseBody
  public ResponseEntity<MedicalSupplyPost> createPost(
      @RequestBody MedicalSupplyPost inpPost) {
    logger.info("Responding to Create Request");
    HttpStatus returnCode = HttpStatus.OK;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    MedicalSupplyPost responsePost = postsRepository.save(inpPost);

    return new ResponseEntity<MedicalSupplyPost>(responsePost, responseHeaders, returnCode);
  }
}
