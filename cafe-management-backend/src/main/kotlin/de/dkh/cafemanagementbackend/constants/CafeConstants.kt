package de.dkh.cafemanagementbackend.constants

class CafeConstants {

    companion object {

        const val BASE_URL = "https://localhost:8081"

        const val SIGN_UP_WENT_WRONG = "Sign up went wrong!"
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
        const val USER_STATUS_UPDATE_WENT_WRONG = "Update user status went wrong!"
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
        const val FORGOT_PASSWORD_WENT_WRONG = "Forgot password went wrong!"
        const val MESSAGE_TYPE_TEXT_HTML = "text/html"
        const val FORGOT_PASSWORD_SUBJECT = "Credentials by Cafe Management System"
        const val RANDOM_PASSWORD_LENGTH = 33
        const val FORGOT_PASSWORD_SUCCESSFULLY = "Password has been successfully reset!"

        const val ADD_CATEGORY_WENT_WRONG = "Add category went wrong!"
        const val ADD_CATEGORY_SUCCESSFULLY = "New category successfully added!"
        const val GET_ALL_CATEGORIES_WENT_WRONG = "Get all categories went wrong!"
        const val UPDATE_CATEGORY_SUCCESSFULLY = "Successfully updated the category!"
        const val UPDATE_CATEGORY_WENT_WRONG = "Update category went wrong!"
        const val DELETE_CATEGORY_SUCCESSFULLY = "Successfully deleted the category!"
        const val DELETE_CATEGORY_WENT_WRONG = "Delete category went wrong!"

        const val GET_DETAILS_WENT_WRONG = "Get dashboard details went wrong!"


        const val ADD_PRODUCT_WENT_WRONG = "Add product went wrong!"
        const val ADD_PRODUCT_SUCCESSFULLY = "New product successfully added!"
        const val DEFAULT_PRODUCT_CATEGORY_NAME = "Default category"
        const val GET_ALL_PRODUCT_WENT_WRONG = "Get all products went wrong!"
        const val UPDATE_PRODUCT_WENT_WRONG = "Update product went wrong!"
        const val UPDATE_PRODUCT_SUCCESSFULLY = "Product successfully updated!"
        const val DELETE_PRODUCT_WENT_WRONG = "Delete product went wrong!"
        const val DELETE_PRODUCT_SUCCESSFULLY = "Successfully deleted the product!"
        const val UPDATE_PRODUCT_STATUS_WENT_WRONG = "Update product status went wrong!"
        const val UPDATE_PRODUCT_STATUS_SUCCESSFULLY = "Product status successfully updated!"
        const val GET_ALL_PRODUCT_BY_CATEGORY_STATUS_WENT_WRONG = "Get all products by category went wrong!"
        const val GET_PRODUCT_BY_ID_WENT_WRONG = "Get product by id went wrong!"

        const val GENERATE_BILL_WENT_WRONG = "Generate report went wrong!"
        const val STORE_LOCATION =
            "/Users/deniskhaskin/IdeaProjects/angularstart/cafe-management-backend/src/main/resources/bills/"
        const val PDF_FILE_EXTENSION = ".pdf"
        const val BILL_TITLE = "Cafe Management System"
        const val TITLE_TYPE = "Title"
        const val HEADER_FOOTER_TYPE = "Header"
        const val BILL_FOOTER_TEXT = "Thank you for using our service! Please visit again!"
        const val PARSE_JSON_ARRAY_FROM_PRODUCT_DETAILS_FOR_BILL_WENT_WRONG =
            "Parse JSON array from product details for bill went wrong!"
        const val GET_BILLS_WENT_WRONG = "Get all bills went wrong!"
        const val GET_BILL_DOCUMENT_WENT_WRONG = "Get bill document went wrong!"
        const val DELETE_BILL_WENT_WRONG = "Delete bill went wrong!"
        const val DELETE_BILL_SUCCESSFULLY = "Successfully deleted the bill!"

    }
}