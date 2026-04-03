package com.libreguardia.config

class UserRoleNotFoundException(val uuid: String) : RuntimeException(uuid)
class UserNotFoundException(val uuid: String) : RuntimeException(uuid)
class UserAlreadyDeletedException(val uuid: String) : RuntimeException(uuid)