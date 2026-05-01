package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import com.libreguardia.dto.UserCreateDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.validation.ValidationType
import kotlinx.html.FlowContent
import kotlinx.html.InputType

fun FlowContent.userCreate(
    user: UserCreateDTO = UserCreateDTO(),
    errors: List<String?>? = null
) {
    customForm(
        formName = "create-user",
        previousPagePath = "/user",
        operationType = OperationType.Post,
        operationPath = "/user",
        errors = errors,
        formFieldsData = listOf(
            FormFieldData(
                text = "name",
                value = user.name,
                required = true,
                inputType = InputType.text
            ),
            FormFieldData(
                text = "surname",
                value = user.surname,
                required = true,
                inputType = InputType.text
            ),
            FormFieldData(
                text = "email",
                value = user.email,
                required = true,
                inputType = InputType.email
            ),
            FormFieldData(
                text = "phone number",
                value = user.phoneNumber,
                required = true,
                inputType = InputType.tel,
                validationType = ValidationType.PhoneNumber,
                triggerType = TriggerType.OnChange
            ),
            FormFieldData(
                text = "new password",
                value = user.password,
                required = true,
                inputType = InputType.password,
                validationType = ValidationType.NewPassword,
                triggerType = TriggerType.OnChange
            ),
            FormFieldData(
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
            FormFieldData(
                text = "enabled",
                inputType = InputType.checkBox,
                checkedValue = user.isEnabled,
                required = true
            )
        )
    )
}