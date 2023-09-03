package de.dkh.cafemanagementbackend.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import de.dkh.cafemanagementbackend.utils.mapper.KeyMapper

class ServiceUtils {

    companion object {
        val objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        /**
         * Maps a given request map to the corresponding mapper class.
         */
        fun getMapperFromRequestMap(requestMap: Map<String, String>, clazz: Class<out KeyMapper>): KeyMapper {
            val json = objectMapper.writeValueAsString(requestMap)
            return objectMapper.readValue(json, clazz)
        }
    }
}