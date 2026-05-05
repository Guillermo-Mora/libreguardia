package com.libreguardia.validation

enum class ValidationType(
    val validationFun: (String?, Boolean) -> String?
) {
    PhoneNumber({ field, required -> validatePhoneNumber(field, required) }),
    NewPassword({ field, required -> validateNewPassword(field, required) }),
    Email({ field, required -> validateEmail(field, required) })
}