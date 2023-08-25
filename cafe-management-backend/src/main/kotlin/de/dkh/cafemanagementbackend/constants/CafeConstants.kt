package de.dkh.cafemanagementbackend.constants

class CafeConstants {

    companion object {

        val BASE_URL = "https://localhost:8081"

        val SOMETHING_WENT_WRONG = "Something went wrong!"
        val INVALID_DATA = "Invalid data!"
        val EMAIL_ALREADY_EXISTS = "Email already exists!"
        val USER_SUCCESSFULLY_REGISTERED = "User successfully registered!"
        val INVALID_EMAIL = "Invalid email!"
        val USER_COULD_NOT_FOUND_BY_EMAIL = "User could not be found for provided email!"
        val WAIT_FOR_ADMIN_APPROVAL = "Wait for admin approval!"
        val BAD_CREDENTIALS = "Bad credentials!"
        val LOGIN_WENT_WRONG = "Log in went wrong!"
        val LOAD_USERS_WENT_WRONG = "Could not load users from the database!"
        val UNAUTHORIZED_ACCESS = "Unauthorized access!"
        val UPDATE_USER_STATUS_WENT_WRONG = "Could not update user(s)!"
        val NO_USER_FOR_ID = "There is no user for the given id in the database!"
        val USER_STATUS_UPDATED = "User status successfully updated!"
        val NO_STATUS_REQUESTED_FOR_UPDATE = "There is no status key provided in the request map!"
        val USER_STATUS_UPDATE_WENT_WRONG = "User status update went wrong!"
        val SUBJECT_USER_SET_APPROVED = "User approved"
        val TEXT_USER_SET_APPROVED = "User is approved by admin"
        val SUBJECT_USER_SET_DISABLED = "User disabled"
        val TEXT_USER_SET_DISABLED = "User is disabled by admin"
    }
}