package com.libreguardia.frontend.component.main.edit

import com.libreguardia.config.database.Role
import com.libreguardia.module.user.UserEditDTO
import com.libreguardia.frontend.component.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*


// I think it would be better to use the actual DTO for doing this, instead of having
// to manually define the fields. But for now, this does the work.
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
    uuid: UUID
) {
    customForm(
        formName = "edit-user",
        previousPagePath = "/user",
        operationType = OperationType.Patch,
        operationPath = "/user/${uuid}",
        deletePath = "/user/${uuid}",
        errors = errors,
        //All of this is implemented in order to also make easier to implement different languages in the future.
        // So the enum key and the text from the formFieldData have to be separated.
        formFields = mapOf(
            UserEditField.NAME to FormFieldData(
                text = "name",
                recievedValue = user.name,
                required = true,
                inputType = InputType.text
            ),
            UserEditField.SURNAME to FormFieldData(
                text = "surname",
                recievedValue = user.surname,
                required = true,
                inputType = InputType.text
            ),
            UserEditField.EMAIL to FormFieldData(
                text = "email",
                recievedValue = user.email,
                required = true,
                inputType = InputType.email,
            ),
            UserEditField.PHONE_NUMBER to FormFieldData(
                text = "phone number",
                recievedValue = user.phoneNumber,
                required = true,
                inputType = InputType.tel,
            ),
            UserEditField.NEW_PASSWORD to FormFieldData(
                text = "new password",
                recievedValue = user.password,
                required = false,
                inputType = InputType.password,
            ),
            UserEditField.ROLE to FormFieldData(
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
            UserEditField.ENABLED to FormFieldData(
                text = "enabled",
                inputType = InputType.checkBox,
                checkedValue = user.isEnabled,
                required = true
            )
        ),
    )
}