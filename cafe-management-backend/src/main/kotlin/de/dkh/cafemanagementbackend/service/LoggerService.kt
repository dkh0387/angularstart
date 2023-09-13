package de.dkh.cafemanagementbackend.service

import org.slf4j.Logger

interface LoggerService {

    fun logAndThrow(
        logger: Logger,
        wentWrongMessage: String,
        e: Exception,
        throwable: Throwable
    ) {
        logger.error(wentWrongMessage + " MESSAGE : ${e.localizedMessage}}")
        throw throwable
    }

}
