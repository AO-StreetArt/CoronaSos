package ao.corona.coronasos.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> T.asResponseEntity(status: HttpStatus = HttpStatus.OK): ResponseEntity<T> = ResponseEntity(this, status)