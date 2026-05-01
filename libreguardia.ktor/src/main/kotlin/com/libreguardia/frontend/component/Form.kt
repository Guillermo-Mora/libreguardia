package com.libreguardia.frontend.component

import com.libreguardia.validation.ValidationType
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.ButtonType
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.option
import kotlinx.html.select
import kotlinx.html.span

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.customForm(
    formName: String,
    previousPagePath: String,
    formFieldsData: List<FormFieldData>
) {
    val formId = formName.lowercase().replace(" ", "")
    val formTarget = "#$formId"
    div {
        id = formId
        form {
            attributes.hx {
                target = formTarget
                swap = "outerHTML"
                validate = true
            }
            for (formFieldData in formFieldsData)
                customField(
                    data = formFieldData
                )
            button {
                type = ButtonType.submit
                text("Save")
            }
            button {
                attributes.hx {
                    trigger = "click"
                    get = previousPagePath
                    target = "#main-content"
                    swap = "innerHTML"
                }
                text("Cancel")
            }
        }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.customField(
    data: FormFieldData
) {
    div {
        label {
            htmlFor = data.inputId
            text(data.labelText)
        }
        div {
            if (data.selectOptions != null) {
                select {
                    name = data.inputId
                    required = data.required
                    for (selectOption in data.selectOptions)
                        option {
                            selected = selectOption.selected
                            value = selectOption.value
                            text(selectOption.text)
                        }
                }
            } else {
                input {
                    if (
                        data.validationType != null &&
                        data.triggerType != null
                    ) {
                        attributes.hx {
                            //Replaced to get as it's not really a post, and this way we only send
                            // the necessary data.
                            get = "/validation/${data.id}/${data.inputId}/${data.required}/${data.validationType}"
                            trigger = data.triggerType.value
                            target = data.target

                            //To only send for validation this input value.
                            // Maybe I should consider doing this more permissive for easier
                            // custom validations required before sending, so I can validate more fields
                            // with a relation in a single action automatically.
                            include = "#${data.inputId}"
                            //This works to filer the data sent, but only if the request is a POST
                            //params = data.inputId

                            swap = "outerHTML"
                            sync = "closest form:abort"

                        }
                    }
                    if (
                        data.inputType == InputType.checkBox &&
                        data.checkedValue != null
                    ) {
                        checked = data.checkedValue
                        value = "checked"
                        required = false
                    } else {
                        value = data.value
                        required = data.required
                    }
                    type = data.inputType
                    name = data.inputId
                    id = data.inputId
                    placeholder = data.placeHolder
                }
            }
            span {
                id = data.id
                data.error?.let { text(it) }
            }
        }
    }
}
data class FormFieldData(
    //First letter of the "text" should be lowercase
    val text: String,
    //
    val error: String? = null,
    val value: String = "",
    val checkedValue: Boolean? = null,
    val required: Boolean = true,
    val inputType: InputType = InputType.text,
    val selectOptions: List<SelectOption>? = null,
    //
    val validationType: ValidationType? = null,
    val triggerType: TriggerType? = null,
) {
    val labelText: String = text.replaceFirstChar { it.uppercase() }
    val id: String = text.lowercase().replace(" ", "-")
    val inputId: String = text.replace(" ", "")
    val placeHolder: String = "Input $text"
    val target: String = "#$id"
}

enum class TriggerType(val value: String) {
    OnChange("input changed delay:600ms")
}

data class SelectOption(
    val text: String,
    val selected: Boolean = false
) {
    val value: String = text.lowercase().replace(" ", "")
}