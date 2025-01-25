import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStudent, NewStudent } from '../student.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStudent for edit and NewStudentFormGroupInput for create.
 */
type StudentFormGroupInput = IStudent | PartialWithRequiredKeyOf<NewStudent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStudent | NewStudent> = Omit<T, 'enrollmentDate'> & {
  enrollmentDate?: string | null;
};

type StudentFormRawValue = FormValueOf<IStudent>;

type NewStudentFormRawValue = FormValueOf<NewStudent>;

type StudentFormDefaults = Pick<NewStudent, 'id' | 'enrollmentDate' | 'subjects'>;

type StudentFormGroupContent = {
  id: FormControl<StudentFormRawValue['id'] | NewStudent['id']>;
  firstName: FormControl<StudentFormRawValue['firstName']>;
  lastName: FormControl<StudentFormRawValue['lastName']>;
  email: FormControl<StudentFormRawValue['email']>;
  enrollmentDate: FormControl<StudentFormRawValue['enrollmentDate']>;
  street: FormControl<StudentFormRawValue['street']>;
  city: FormControl<StudentFormRawValue['city']>;
  state: FormControl<StudentFormRawValue['state']>;
  zipCode: FormControl<StudentFormRawValue['zipCode']>;
  subjects: FormControl<StudentFormRawValue['subjects']>;
};

export type StudentFormGroup = FormGroup<StudentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StudentFormService {
  createStudentFormGroup(student: StudentFormGroupInput = { id: null }): StudentFormGroup {
    const studentRawValue = this.convertStudentToStudentRawValue({
      ...this.getFormDefaults(),
      ...student,
    });
    return new FormGroup<StudentFormGroupContent>({
      id: new FormControl(
        { value: studentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(studentRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(studentRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(studentRawValue.email, {
        validators: [Validators.required],
      }),
      enrollmentDate: new FormControl(studentRawValue.enrollmentDate, {
        validators: [Validators.required],
      }),
      street: new FormControl(studentRawValue.street, {
        validators: [Validators.required],
      }),
      city: new FormControl(studentRawValue.city, {
        validators: [Validators.required],
      }),
      state: new FormControl(studentRawValue.state, {
        validators: [Validators.required],
      }),
      zipCode: new FormControl(studentRawValue.zipCode, {
        validators: [Validators.required],
      }),
      subjects: new FormControl(studentRawValue.subjects ?? []),
    });
  }

  getStudent(form: StudentFormGroup): IStudent | NewStudent {
    return this.convertStudentRawValueToStudent(form.getRawValue() as StudentFormRawValue | NewStudentFormRawValue);
  }

  resetForm(form: StudentFormGroup, student: StudentFormGroupInput): void {
    const studentRawValue = this.convertStudentToStudentRawValue({ ...this.getFormDefaults(), ...student });
    form.reset(
      {
        ...studentRawValue,
        id: { value: studentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StudentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enrollmentDate: currentTime,
      subjects: [],
    };
  }

  private convertStudentRawValueToStudent(rawStudent: StudentFormRawValue | NewStudentFormRawValue): IStudent | NewStudent {
    return {
      ...rawStudent,
      enrollmentDate: dayjs(rawStudent.enrollmentDate, DATE_TIME_FORMAT),
    };
  }

  private convertStudentToStudentRawValue(
    student: IStudent | (Partial<NewStudent> & StudentFormDefaults),
  ): StudentFormRawValue | PartialWithRequiredKeyOf<NewStudentFormRawValue> {
    return {
      ...student,
      enrollmentDate: student.enrollmentDate ? student.enrollmentDate.format(DATE_TIME_FORMAT) : undefined,
      subjects: student.subjects ?? [],
    };
  }
}
