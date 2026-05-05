package com.libreguardia.routing.module

import com.libreguardia.config.AUTH_SESSION
import com.libreguardia.config.authorized
import com.libreguardia.db.Role
import com.libreguardia.frontend.component.newPasswordField
import com.libreguardia.frontend.component.phoneNumberField
import com.libreguardia.validation.ValidationType
import com.libreguardia.validation.validateNewPassword
import com.libreguardia.validation.validatePhoneNumber
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.routing.Route
import kotlinx.html.id
import kotlinx.html.span

@Resource("/validation")
class ValidationAPI {

    @Resource("{contentId}/{fieldId}/{required}/{validate}")
    class Validate(
        val parent: ValidationAPI = ValidationAPI(),
        val contentId: String,
        val fieldId: String,
        val required: Boolean,
        val validate: ValidationType
    )

    @Resource("phone-number")
    class PhoneNumber(val parent: ValidationAPI = ValidationAPI())

    @Resource("new-password")
    class NewPassword(val parent: ValidationAPI = ValidationAPI())
}

fun Route.validationRouting() {
    authenticate(AUTH_SESSION) {
        authorized(Role.ADMIN, Role.USER, Role.VISUALIZER) {
            get<ValidationAPI.Validate> { validation ->
                val contentId = validation.contentId
                val fieldId = validation.fieldId
                //This works for POST, that sends HTML forms
                //val field = call.receiveParameters().let { it[fieldId] }

                //This is the way to get the data from a GET, with queryParams in the header
                val field = call.request.queryParameters[fieldId]

                val required = validation.required
                val validationFun = validation.validate.validationFun
                val error = validationFun(field, required)
                call.respondHtmlFragment {
                    span {
                        id = contentId
                        error?.let { text(it) }
                    }
                }
            }


            post<ValidationAPI.PhoneNumber> {
                val phoneNumber = call.receiveParameters().let { it["phoneNumber"] }
                call.respondHtmlFragment {
                    //I still don't know how to handle client-side validations
                    //Currently, I was doing it using HTMX backend validation with requests to
                    // endpoints. But it seems that doing this is overengineering everything,
                    // and it makes that I have to create a lot of different endpoints, and it
                    // is complicating the live validations. I think I should consider using
                    // JS or TypeScript for live frontend validations or something else, because
                    // validating individual fields like this is complicated and makes everything
                    // to be too much attached. I will still try to do this with HTMX, but seems
                    // hard right now. I want to solve this before proceeding, because finding
                    // the easy way to do this early will solve me a lot of future problems.
                    /*
                    customField(
                        data = FormFieldData(
                            text = TODO(),
                            error = TODO(),
                            value = TODO(),
                            checkedValue = TODO(),
                            required = TODO(),
                            inputType = TODO(),
                            selectOptions = TODO(),
                            postPath = TODO(),
                            triggerType = TODO()
                        )
                    )

                     */

                    phoneNumberField(
                        phoneNumber = phoneNumber ?: "",
                        error = validatePhoneNumber(phoneNumber, true)
                    )
                }
            }
            post<ValidationAPI.NewPassword> {
                val newPassword = call.receiveParameters().let { it["newPassword"] }
                call.respondHtmlFragment {
                    newPasswordField(
                        newPassword = newPassword ?: "",
                        error = validateNewPassword(
                            field = newPassword,
                            required = false
                        ),
                    )
                }
            }
        }
    }
}