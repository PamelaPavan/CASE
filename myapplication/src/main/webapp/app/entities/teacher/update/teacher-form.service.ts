import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITeacher, NewTeacher } from '../teacher.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITeacher for edit and NewTeacherFormGroupInput for create.
 */
type TeacherFormGroupInput = ITeacher | PartialWithRequiredKeyOf<NewTeacher>;

type TeacherFormDefaults = Pick<NewTeacher, 'id' | 'subjects'>;

type TeacherFormGroupContent = {
  id: FormControl<ITeacher['id'] | NewTeacher['id']>;
  firstName: FormControl<ITeacher['firstName']>;
  lastName: FormControl<ITeacher['lastName']>;
  specialization: FormControl<ITeacher['specialization']>;
  street: FormControl<ITeacher['street']>;
  city: FormControl<ITeacher['city']>;
  state: FormControl<ITeacher['state']>;
  zipCode: FormControl<ITeacher['zipCode']>;
  subjects: FormControl<ITeacher['subjects']>;
};

export type TeacherFormGroup = FormGroup<TeacherFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TeacherFormService {
  createTeacherFormGroup(teacher: TeacherFormGroupInput = { id: null }): TeacherFormGroup {
    const teacherRawValue = {
      ...this.getFormDefaults(),
      ...teacher,
    };
    return new FormGroup<TeacherFormGroupContent>({
      id: new FormControl(
        { value: teacherRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(teacherRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(teacherRawValue.lastName, {
        validators: [Validators.required],
      }),
      specialization: new FormControl(teacherRawValue.specialization, {
        validators: [Validators.required],
      }),
      street: new FormControl(teacherRawValue.street, {
        validators: [Validators.required],
      }),
      city: new FormControl(teacherRawValue.city, {
        validators: [Validators.required],
      }),
      state: new FormControl(teacherRawValue.state, {
        validators: [Validators.required],
      }),
      zipCode: new FormControl(teacherRawValue.zipCode, {
        validators: [Validators.required],
      }),
      subjects: new FormControl(teacherRawValue.subjects ?? []),
    });
  }

  getTeacher(form: TeacherFormGroup): ITeacher | NewTeacher {
    return form.getRawValue() as ITeacher | NewTeacher;
  }

  resetForm(form: TeacherFormGroup, teacher: TeacherFormGroupInput): void {
    const teacherRawValue = { ...this.getFormDefaults(), ...teacher };
    form.reset(
      {
        ...teacherRawValue,
        id: { value: teacherRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TeacherFormDefaults {
    return {
      id: null,
      subjects: [],
    };
  }
}
