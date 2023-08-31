package de.dkh.cafemanagementbackend.constants

class CafeConstants {

    companion object {

        const val BASE_URL = "https://localhost:8081"

        const val SOMETHING_WENT_WRONG = "Something went wrong!"
        const val INVALID_DATA = "Invalid data!"
        const val EMAIL_ALREADY_EXISTS = "Email already exists!"
        const val USER_SUCCESSFULLY_REGISTERED = "User successfully registered!"
        const val INVALID_EMAIL = "Invalid email!"
        const val USER_COULD_NOT_FOUND_BY_EMAIL = "User could not be found for provided email!"
        const val WAIT_FOR_ADMIN_APPROVAL = "Wait for admin approval!"
        const val BAD_CREDENTIALS = "Bad credentials!"
        const val LOGIN_WENT_WRONG = "Log in went wrong!"
        const val LOAD_USERS_WENT_WRONG = "Could not load users from the database!"
        const val UNAUTHORIZED_ACCESS = "Unauthorized access!"
        const val NO_USER_FOR_ID = "There is no user for the given id in the database!"
        const val USER_STATUS_UPDATED = "User status successfully updated!"
        const val NO_STATUS_REQUESTED_FOR_UPDATE = "There is no status key provided in the request map!"
        const val USER_STATUS_UPDATE_WENT_WRONG = "User status update went wrong!"
        const val SUBJECT_USER_SET_APPROVED = "User approved"
        const val TEXT_USER_SET_APPROVED = "User is approved by admin"
        const val SUBJECT_USER_SET_DISABLED = "User disabled"
        const val TEXT_USER_SET_DISABLED = "User is disabled by admin"
        const val CHANGE_PASSWORD_WENT_WRONG = "Change password went wrong!"
        const val NO_USER_FOR_EMAIL = "There is no user for the given email in the database!"
        const val CURRENT_USER_IS_NULL = "Current user is NULL, status mail will not be sending!"
        const val NO_STATUS_FOR_UPDATE = "There is no user status for update, status mail will not be sending!"
        const val OLD_VS_EXISTING_PASSWORD_MISMATCH =
            "Old password from request and existing password do not match or at least one of them is NULL!"
        const val PASSWORD_SUCCESSFULLY_CHANGED = "Password successfully changed!"
        const val TRUE = "true"
        const val TOKEN_KEY_WORD = "token"
        const val MESSAGE_KEY_WORD = "message"
    }
}