package com.libreguardia.frontend.component.main.edit

import com.libreguardia.dto.module.GroupEditDTO
import com.libreguardia.frontend.component.*
import com.libreguardia.model.AcademicYearModel
import com.libreguardia.model.CourseModel
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import java.util.*

enum class GroupEditField(override val key: String) : FormField {
    CODE("code"),
    COURSE("course"),
    ACADEMIC_YEAR("academic_year"),
    POINTS_MULTIPLIER("points_multiplier");
}

fun FlowContent.groupEdit(
    dto: GroupEditDTO,
    courses: List<CourseModel>,
    academicYears: List<AcademicYearModel>,
    errors: Map<FormField, String?>? = null,
    uuid: UUID
) {
    customForm(
        formName = "edit-group",
        previousPagePath = "/group",
        operationType = OperationType.Patch,
        operationPath = "/group/${uuid}",
        deletePath = "/group/${uuid}",
        errors = errors,
        formFields = mapOf(
            GroupEditField.ACADEMIC_YEAR to FormFieldData(
                text = "academic year",
                required = true,
                selectOptions =
                    academicYears.map { academicYear ->
                        SelectOption(
                            text = academicYear.name,
                            id = academicYear.id,
                            selected = academicYear.id == dto.academicYearId
                        )
                    }
            ),
            GroupEditField.CODE to FormFieldData(
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
            GroupEditField.COURSE to FormFieldData(
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