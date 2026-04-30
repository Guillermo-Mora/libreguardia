package com.libreguardia.frontend.component.main

import com.libreguardia.db.Role
import com.libreguardia.frontend.component.FormFieldData
import com.libreguardia.frontend.component.SelectOption
import com.libreguardia.frontend.component.TriggerType
import com.libreguardia.frontend.component.customForm
import com.libreguardia.model.UserModel
import com.libreguardia.validation.ValidationType
import kotlinx.html.FlowContent
import kotlinx.html.InputType

fun FlowContent.userEdit(
    user: UserModel
) {
    customForm(
        formName = "edit-user",
        previousPagePath = "/user",
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
                required = false,
                inputType = InputType.password,
                validationType = ValidationType.NewPassword,
                triggerType = TriggerType.OnChange
            ),
            FormFieldData(
                text = "role",
                value = user.role,
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