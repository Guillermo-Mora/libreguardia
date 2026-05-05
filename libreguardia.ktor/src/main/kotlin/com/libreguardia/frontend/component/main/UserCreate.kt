package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.validation.ValidationType
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class UserCreateField(override val key: String) : FormField {
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
                value = user.name,
                required = true,
                inputType = InputType.text
            ),
            UserCreateField.SURNAME to FormFieldData(
                text = "surname",
                value = user.surname,
                required = true,
                inputType = InputType.text
            ),
            UserCreateField.EMAIL to FormFieldData(
                text = "email",
                value = user.email,
                required = true,
                inputType = InputType.email,
                //validationType = ValidationType.Email,
                //triggerType = TriggerType.OnChange
            ),
            UserCreateField.PHONE_NUMBER to FormFieldData(
                text = "phone number",
                value = user.phoneNumber,
                required = true,
                inputType = InputType.tel,
                //validationType = ValidationType.PhoneNumber,
                //triggerType = TriggerType.OnChange
            ),
            UserCreateField.NEW_PASSWORD to FormFieldData(
                text = "new password",
                value = user.password,
                required = true,
                inputType = InputType.password,
                //validationType = ValidationType.NewPassword,
                //triggerType = TriggerType.OnChange
            ),
            UserCreateField.ROLE to FormFieldData(
                text = "role",
                required = true,
                selectOptions =
                    Role.entries.map { role ->
                        SelectOption(
                            text = role.name,
                            selected = role.name == user.role
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