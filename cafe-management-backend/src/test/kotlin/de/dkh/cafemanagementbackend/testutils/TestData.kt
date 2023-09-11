package de.dkh.cafemanagementbackend.testutils

import de.dkh.cafemanagementbackend.entity.Authority
import de.dkh.cafemanagementbackend.entity.Category
import de.dkh.cafemanagementbackend.entity.Product
import de.dkh.cafemanagementbackend.entity.User
import de.dkh.cafemanagementbackend.utils.ServiceUtils
import de.dkh.cafemanagementbackend.wrapper.ProductWrapper

class TestData {

    companion object {

        fun getAdmin(): User = User(
            name = "Denis Khaskin",
            contactNumber = "+4915126227287",
            email = "deniskh87@gmail.com",
            password = "11235813",
            status = "true",
            role = User.UserRoles.ROLE_ADMIN.nameWithoutPrefix().lowercase()
        )

        fun getInactiveUser(): User = User(
            name = "Denis Khaskin",
            contactNumber = "+4915126227287",
            email = "deniskh87@gmail.com",
            password = "11235813",
            status = "false",
            role = User.UserRoles.ROLE_USER.nameWithoutPrefix().lowercase()
        )

        fun getAuthority(): Authority = Authority(User.UserRoles.ROLE_USER.name)

        fun getUserDetailWithoutPassword(): User =
            getInactiveUserWithAuthorities().copy(password = null, status = "true")

        fun getSpringUserDetails(): org.springframework.security.core.userdetails.User = getSpringUserDetails(
            getInactiveUser().email
        )

        fun getSpringUserDetails(username: String): org.springframework.security.core.userdetails.User =
            org.springframework.security.core.userdetails.User(
                username,
                getInactiveUser().password,
                ArrayList()
            )

        fun getSpringUserDetails(user: User): org.springframework.security.core.userdetails.User =
            org.springframework.security.core.userdetails.User(
                user.email,
                user.password,
                emptyList()
            )

        fun getInactiveUserWithAuthorities(): User = getInactiveUser().copy(authorities = listOf(getAuthority()))

        fun getCategory(name: String): Category = Category(name, emptyList())

        fun getProduct(name: String): Product {
            val category = getCategory("Testcategory")
            val product = Product(name, "Testdescription", 20.5, "true", category)
            //category.products = listOf(product)
            return product
        }

        fun getProductWrapperJson(id: Long): String {
            val product = ProductWrapper(
                id = id,
                name = "Small salat",
                description = "different veggies, with dressing",
                price = 6.5,
                status = "true",
                categoryId = 1,
                categoryName = "Starter"
            )
            return ServiceUtils.objectMapper.writeValueAsString(product)
        }
    }
}