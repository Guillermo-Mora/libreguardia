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
import kotlinx.html.h2
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
    operationPath: String,
    deletePath: String? = null,
    operationType: OperationType,
    errors: Map<FormField, String?>? = null,
    formFields: Map<FormField, FormFieldData>,
    ) {
    val formId = formName.lowercase().replace(" ", "")
    val formTarget = "#$formId"
    div("form-card") {
        this.id = formId
        div("form-card-header") {
            h2("form-card-title") { text(formName) }
        }
        form {
            attributes.hx {
                when (operationType) {
                    OperationType.Put -> put = operationPath
                    OperationType.Post -> post = operationPath
                    OperationType.Patch -> patch = operationPath
                    OperationType.Delete -> delete = operationPath
                    OperationType.Get -> get = operationPath
                }
                target = formTarget
                swap = "outerHTML"
                validate = true
            }
            for ((field, data) in formFields) {
                val id = field.id
                val errorId = "$id-error"
                val errorTarget = "#$errorId"
                customField(
                    data = data,
                    id = id,
                    formTarget = formTarget,
                    errorId = errorId,
                    errorTarget = errorTarget,
                    error = errors?.get(field)
                )
            }
            div("form-actions") {
                button {
                    attributes["class"] = "btn btn-primary"
                    type = ButtonType.submit
                    text("Save")
                }
                button {
                    attributes["class"] = "btn btn-ghost"
                    attributes.hx {
                        trigger = "click"
                        get = previousPagePath
                        target = "#main-content"
                        swap = "innerHTML"
                    }
                    text("Cancel")
                }
                deletePath?.let {
                    button {
                        attributes["class"] = "btn btn-danger"
                        attributes.hx {
                            trigger = "click"
                            delete = deletePath
                        }
                        text("Delete")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.customField(
    data: FormFieldData,
    error: String?,
    id: String,
    formTarget: String,
    errorId: String,
    errorTarget: String
) {
    div("form-field") {
        label {
            htmlFor = id
            text(data.labelText)
        }
        if (data.selectOptions != null) {
            select {
                name = id
                required = data.required
                for (selectOption in data.selectOptions)
                    option {
                        selected = selectOption.selected
                        value = selectOption.id ?: selectOption.value
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
                        get = "/validation/${errorId}/${id}/${data.required}/${data.validationType}"
                        trigger = data.triggerType.value
                        this.target = errorTarget
                        include = "#${id}"
                        swap = "outerHTML"
                        sync = "closest form:abort"
                    }
                }

                if (data.inputType == InputType.range &&
                    data.rangeConfig != null) {
                    min = data.rangeConfig.min.toString()
                    max = data.rangeConfig.max.toString()
                    step = data.rangeConfig.step.toString()
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
                name = id
                this.id = id
                placeholder = data.placeHolder
            }
        }
        div("form-error") {
            this.id = errorId
            error?.let { text(it) }
        }
    }
}

data class FormFieldData(
    val text: String,
    val value: String = "",
    val checkedValue: Boolean? = null,
    val required: Boolean = true,
    val inputType: InputType = InputType.text,
    val selectOptions: List<SelectOption>? = null,
    val rangeConfig: RangeConfig? = null,
    val validationType: ValidationType? = null,
    val triggerType: TriggerType? = null,
) {
    val labelText: String = text.replaceFirstChar { it.uppercase() }
    val placeHolder: String = "Input $text"
}

enum class TriggerType(val value: String) {
    OnChange("input changed delay:600ms")
}

enum class OperationType {
    Get,
    Post,
    Put,
    Patch,
    Delete
}

data class SelectOption(
    val text: String,
    val selected: Boolean = false,
    val id: String? = null
) {
    val value: String = text.lowercase().replace(" ", "")
}

data class RangeConfig(
    val min: Float,
    val max: Float,
    val step: Float
)

interface FormField {
    val key: String
    val id get() = "$key-id"
}
