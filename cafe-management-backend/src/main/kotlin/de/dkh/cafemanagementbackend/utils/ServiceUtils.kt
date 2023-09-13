package de.dkh.cafemanagementbackend.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Strings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.dkh.cafemanagementbackend.utils.mapper.BillMapper
import de.dkh.cafemanagementbackend.utils.mapper.KeyMapper
import java.lang.reflect.Type

class ServiceUtils {

    companion object {
        val objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        /**
         * Maps a given request map to the corresponding mapper class.
         */
        fun getMapperFromRequestStringMap(requestMap: Map<String, String>, clazz: Class<out KeyMapper>): KeyMapper {
            val json = objectMapper.writeValueAsString(requestMap)
            return objectMapper.readValue(json, clazz)
        }

        fun getMapperFromRequestObjectMap(requestMap: Map<String, Any>, clazz: Class<BillMapper>): KeyMapper {
            val json = objectMapper.writeValueAsString(requestMap)
            return objectMapper.readValue(json, clazz)
        }

        /**
         * Simple builds a map from a JSON, without object mapping!
         */
        fun getMapFromJSON(data: String): Map<String, Any> {
            val type: Type = object : TypeToken<Map<String, Any>>() {}.type
            return if (!Strings.isNullOrEmpty(data)) {
                Gson().fromJson(data, type)
            } else {
                emptyMap()
            }
        }
    }
}