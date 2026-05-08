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
    operationPath: String,
    deletePath: String? = null,
    operationType: OperationType,
    errors: Map<FormField, String?>? = null,
    formFields: Map<FormField, FormFieldData>,
    ) {
    val formId = formName.lowercase().replace(" ", "")
    val formTarget = "#$formId"
    div {
        this.id = formId
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
            deletePath?.let {
                button {
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

@OptIn(ExperimentalKtorApi::class)
fun FlowContent.customField(
    data: FormFieldData,
    error: String?,
    id: String,
    formTarget: String,
    errorId: String,
    errorTarget: String
) {
    div {
        label {
            htmlFor = id
            text(data.labelText)
        }
        div {
            if (data.selectOptions != null) {
                select {
                    name = id
                    required = data.required
                    for (selectOption in data.selectOptions)
                        option {
                            selected = selectOption.selected
                            value = selectOption.id?.toString() ?: selectOption.value
                            text(selectOption.text)
                        }
                }
            } else {
                input {
                    //I implemented this for doing live server-side validations on each field. Just to avoid
                    // writing JS. However, I don't think it's a good practice to use this for simple data format
                    // validations. So I'm switching to HTML5 Validation API. I will let it here in case I
                    // need it in the future.
                    //
                    if (
                        data.validationType != null &&
                        data.triggerType != null
                    ) {
                        attributes.hx {
                            //Replaced to get as it's not really a post, and this way we only send
                            // the necessary data.
                            get = "/validation/${errorId}/${id}/${data.required}/${data.validationType}"
                            trigger = data.triggerType.value
                            this.target = errorTarget

                            //To only send for validation this input value.
                            // Maybe I should consider doing this more permissive for easier
                            // custom validations required before sending, so I can validate more fields
                            // with a relation in a single action automatically.
                            include = "#${id}"

                            //This works to filer the data sent, but only if the request is a POST
                            //params = data.inputId

                            swap = "outerHTML"
                            sync = "closest form:abort"

                        }
                    }
                    //

                    //Validations using the HTML5 Validation API
                    //Still to be implemented live validations with HTML5 + JS. To prevent the previous
                    // implementation that constantly threw requests to the server.
                    //onKeyUp = "this.setCustomValidity('')"

                    //Could be used for regex validations with specific patterns
                    //pattern = "foo"
                    //Still to implement
                    /*
                    onInput = """
                        const error = document.getElementById('${errorId}')
                        
                        if (this.value !== 'foo') {
                        error.textContent = 'Please enter the value foo'
                        } else {
                        error.textContent = ''
                        }
                    """.trimIndent()
                    attributes.hx {
                        validate = true
                    }
                     */

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
            span {
                this.id = errorId
                error?.let { text(it) }
            }
        }
    }
}
data class FormFieldData(
    //First letter of the "text" should be lowercase
    val text: String,
    //
    val value: String = "",
    val checkedValue: Boolean? = null,
    val required: Boolean = true,
    val inputType: InputType = InputType.text,
    val selectOptions: List<SelectOption>? = null,
    val rangeConfig: RangeConfig? = null,
    //
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