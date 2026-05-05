package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.frontend.component.FormField
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.OperationType
import com.libreguardia.frontend.component.SelectOption
import com.libreguardia.frontend.component.TriggerType
import com.libreguardia.frontend.component.customForm
import com.libreguardia.validation.ValidationType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.UUID

enum class UserEditField(override val key: String) : FormField {
    NAME("name"),
    SURNAME("surname"),
    EMAIL("email"),
    PHONE_NUMBER("phone-number"),
    NEW_PASSWORD("new-password"),
    ROLE("role"),
    ENABLED("enabled");
}

fun FlowContent.userEdit(
    user: UserEditDTO,
    errors: Map<FormField, String?>? = null,
    userUuid: UUID
) {
    customForm(
        formName = "edit-user",
        previousPagePath = "/user",
        operationType = OperationType.Patch,
        operationPath = "/user/${userUuid}",
        deletePath = "/user/${userUuid}",
        errors = errors,
        //All of this is implemented in order to also make easier to implement different languages in the future.
        // So the enum key and the text from the formFieldData have to be separated.
        formFields = mapOf(
            UserEditField.NAME to FormFieldData(
                text = "name",
                value = user.name,
                required = true,
                inputType = InputType.text
            ),
            UserEditField.SURNAME to FormFieldData(
                text = "surname",
                value = user.surname,
                required = true,
                inputType = InputType.text
            ),
            UserEditField.EMAIL to FormFieldData(
                text = "email",
                value = user.email,
                required = true,
                inputType = InputType.email,
                //validationType = ValidationType.Email,
                //triggerType = TriggerType.OnChange
            ),
            UserEditField.PHONE_NUMBER to FormFieldData(
                text = "phone number",
                value = user.phoneNumber,
                required = true,
                inputType = InputType.tel,
                //validationType = ValidationType.PhoneNumber,
                //triggerType = TriggerType.OnChange
            ),
            UserEditField.NEW_PASSWORD to FormFieldData(
                text = "new password",
                value = user.password,
                required = false,
                inputType = InputType.password,
                //validationType = ValidationType.NewPassword,
                //triggerType = TriggerType.OnChange
            ),
            UserEditField.ROLE to FormFieldData(
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
            UserEditField.ENABLED to FormFieldData(
                text = "enabled",
                inputType = InputType.checkBox,
                checkedValue = user.isEnabled,
                required = true
            )
        ),
    )
}