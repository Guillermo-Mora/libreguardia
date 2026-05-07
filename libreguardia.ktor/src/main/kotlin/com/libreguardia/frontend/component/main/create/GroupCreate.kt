package com.libreguardia.frontend.component.main.create

import com.libreguardia.dto.module.GroupCreateDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.frontend.component.main.edit.GroupEditField
import com.libreguardia.model.CourseModel
import kotlinx.html.FlowContent
import kotlinx.html.InputType

enum class GroupCreateField(override val key: String) : FormField {
    CODE("code"),
    COURSE("course"),
    POINTS_MULTIPLIER("points_multiplier")
}
fun FlowContent.groupCreate(
    dto: GroupCreateDTO = GroupCreateDTO(),
    courses: List<CourseModel>,
    errors: Map<FormField, String?>? = null,
) {
    customForm(
        formName = "edit-group",
        previousPagePath = "/group",
        operationType = OperationType.Post,
        operationPath = "/group",
        errors = errors,
        formFields = mapOf(
            GroupCreateField.CODE to FormFieldData(
                text = "code",
                value = dto.code,
                required = true,
                inputType = InputType.text
            ),
            GroupEditField.POINTS_MULTIPLIER to FormFieldData(
                text = "Difficulty",
                value = dto.pointsMultiplier,
                required = true,
                inputType = InputType.range,
                rangeConfig = RangeConfig(
                    min = 0.0F,
                    max = 2.0F,
                    step = 0.1F
                )
            ),
            GroupCreateField.COURSE to FormFieldData(
                text = "course",
                required = true,
                selectOptions =
                    courses.map { course ->
                        SelectOption(
                            text = course.name,
                            id = course.id,
                            selected = course.id == dto.courseId
                        )
                    }
            ),
        ),
    )
}