package de.dkh.cafemanagementbackend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class UserRESTExceptionHandler {

    /**
     * Exception handling for the error showing up.
     * If the exception occurs, it will be propagated automatically to the handler, since it is annotated with [ExceptionHandler].
     * NOTE: Jackson will automatically pick up the entity returned here and convert it to the JSON for showing in the browser.
     */
    @ExceptionHandler
    fun handleException(e: SignUpException): ResponseEntity<SignUpErrorResponce> {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        val error = SignUpErrorResponce(e.httpStatus.name, e.message, System.currentTimeMillis())
        return ResponseEntity<SignUpErrorResponce>(error, httpStatus)
    }
}