package com.libreguardia.frontend.component.main.create

import com.libreguardia.module.shared.database.Role
import com.libreguardia.module.user.UserCreateDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class UserCreateField(
    override val key: String,
) : FormField {
    NAME("name"),
    SURNAME("surname"),
    EMAIL("email"),
    PHONE_NUMBER("phone-number"),
    NEW_PASSWORD("new-password"),
    ROLE("role"),
    ENABLED("enabled");
}


fun FlowContent.userCreate(
    user: UserCreateDTO = UserCreateDTO(),
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "create-user",
        previousPagePath = "/user",
        operationPath = "/user",
        operationType = OperationType.Post,
        errors = errors,
        formFields = mapOf(
            UserCreateField.NAME to FormFieldData(
                text = "name",
                recievedValue = user.name,
                required = true,
                inputType = InputType.text
            ),
            UserCreateField.SURNAME to FormFieldData(
                text = "surname",
                recievedValue = user.surname,
                required = true,
                inputType = InputType.text
            ),
            UserCreateField.EMAIL to FormFieldData(
                text = "email",
                recievedValue = user.email,
                required = true,
                inputType = InputType.email,
            ),
            UserCreateField.PHONE_NUMBER to FormFieldData(
                text = "phone number",
                recievedValue = user.phoneNumber,
                required = true,
                inputType = InputType.tel,
            ),
            UserCreateField.NEW_PASSWORD to FormFieldData(
                text = "new password",
                recievedValue = user.password,
                required = true,
                inputType = InputType.password,
            ),
            UserCreateField.ROLE to FormFieldData(
                text = "role",
                required = true,
                selectOptions =
                    Role.entries.map { role ->
                        SelectOption(
                            text = role.name,
                            selected = role == user.role
                        )
                    }
            ),
            UserCreateField.ENABLED to FormFieldData(
                text = "enabled",
                inputType = InputType.checkBox,
                checkedValue = user.isEnabled,
                required = true
            )
        ),
    )
}