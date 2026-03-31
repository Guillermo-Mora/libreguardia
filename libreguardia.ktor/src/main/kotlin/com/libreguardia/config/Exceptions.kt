package com.libreguardia.config

import java.util.UUID

class UserRoleNotFoundException(val roleName: String) : RuntimeException(roleName)
class UserNotFoundException(val uuid: String) : RuntimeException(uuid)