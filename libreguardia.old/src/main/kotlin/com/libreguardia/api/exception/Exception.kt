package com.libreguardia.api.exception

class UserRoleNotFoundException(val roleName: String) : RuntimeException(roleName)

class EmailDuplicatedException(val email: String) : RuntimeException(email)