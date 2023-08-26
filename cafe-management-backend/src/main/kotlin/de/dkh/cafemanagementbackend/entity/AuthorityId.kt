package de.dkh.cafemanagementbackend.entity

import java.io.Serializable

data class AuthorityId(private val userId: Long, private val authority: String) : Serializable {

}
