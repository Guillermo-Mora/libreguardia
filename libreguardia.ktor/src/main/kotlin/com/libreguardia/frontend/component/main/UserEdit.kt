package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import com.libreguardia.dto.UserEditDTO
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.OperationType
import com.libreguardia.frontend.component.SelectOption
import com.libreguardia.frontend.component.TriggerType
import com.libreguardia.frontend.component.customForm
import com.libreguardia.model.UserModel
import com.libreguardia.validation.ValidationType
import kotlinx.html.FlowContent
import kotlinx.html.InputType

fun FlowContent.userEdit(
    user: UserEditDTO,
    errors: List<String?>? = null
) {
    //The errors list is used when the form is sent, so all has to be validated. This way, I can store errors in orders,
    // putting String or null in each case and sending it to the customForm function. So I can track and send
    // errors in a much more easy and straight up way.

    //To add for validation of email: That the email being put is not being used by another user in the system.
    customForm(
        formName = "edit-user",
        previousPagePath = "/user",
        operationType = OperationType.Patch,
        validationPath = "/user/${user.id}",
        errors = errors,
        formFieldsData = listOf(
            FormFieldData(
                text = "name",
                value = user.name.toString(),
                required = true,
                inputType = InputType.text
            ),
            FormFieldData(
                text = "surname",
                value = user.surname.toString(),
                required = true,
                inputType = InputType.text
            ),
            FormFieldData(
                text = "email",
                value = user.email.toString(),
                required = true,
                inputType = InputType.email
            ),
            FormFieldData(
                text = "phone number",
                value = user.phoneNumber.toString(),
                required = true,
                inputType = InputType.tel,
                validationType = ValidationType.PhoneNumber,
                triggerType = TriggerType.OnChange
            ),
            FormFieldData(
                text = "new password",
                required = false,
                inputType = InputType.password,
                validationType = ValidationType.NewPassword,
                triggerType = TriggerType.OnChange
            ),
            FormFieldData(
                text = "role",
                value = user.role.toString(),
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