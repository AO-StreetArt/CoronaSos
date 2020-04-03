package ao.corona.coronasos.service

import ao.corona.coronasos.entity.MedicalSupplyPost
import ao.corona.coronasos.repo.MedicalSupplyPostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class MedicalSupplyPostService {
    @Autowired
    private lateinit var repo: MedicalSupplyPostRepository

    fun createPost(medicalSupplyPost: MedicalSupplyPost) = repo.save(medicalSupplyPost)

    fun getPostById(id: String): Optional<MedicalSupplyPost> = repo.findById(id)

    fun getAllPosts(recordsInPage: Int, pageNum: Int): List<MedicalSupplyPost> {
        val pageable: Pageable = PageRequest.of(pageNum, recordsInPage, Sort.by("testInt"))
        return repo.findAll(pageable).content
    }

    fun deletePostById(id: String) = repo.deleteById(id)

}