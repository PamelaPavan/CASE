import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { SubjectService } from '../service/subject.service';
import { ISubject } from '../subject.model';
import { SubjectFormGroup, SubjectFormService } from './subject-form.service';

@Component({
  selector: 'jhi-subject-update',
  templateUrl: './subject-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubjectUpdateComponent implements OnInit {
  isSaving = false;
  subject: ISubject | null = null;

  teachersSharedCollection: ITeacher[] = [];
  studentsSharedCollection: IStudent[] = [];

  protected subjectService = inject(SubjectService);
  protected subjectFormService = inject(SubjectFormService);
  protected teacherService = inject(TeacherService);
  protected studentService = inject(StudentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubjectFormGroup = this.subjectFormService.createSubjectFormGroup();

  compareTeacher = (o1: ITeacher | null, o2: ITeacher | null): boolean => this.teacherService.compareTeacher(o1, o2);

  compareStudent = (o1: IStudent | null, o2: IStudent | null): boolean => this.studentService.compareStudent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subject }) => {
      this.subject = subject;
      if (subject) {
        this.updateForm(subject);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subject = this.subjectFormService.getSubject(this.editForm);
    if (subject.id !== null) {
      this.subscribeToSaveResponse(this.subjectService.update(subject));
    } else {
      this.subscribeToSaveResponse(this.subjectService.create(subject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subject: ISubject): void {
    this.subject = subject;
    this.subjectFormService.resetForm(this.editForm, subject);

    this.teachersSharedCollection = this.teacherService.addTeacherToCollectionIfMissing<ITeacher>(
      this.teachersSharedCollection,
      ...(subject.teachers ?? []),
    );
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing<IStudent>(
      this.studentsSharedCollection,
      ...(subject.students ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teacherService
      .query()
      .pipe(map((res: HttpResponse<ITeacher[]>) => res.body ?? []))
      .pipe(
        map((teachers: ITeacher[]) =>
          this.teacherService.addTeacherToCollectionIfMissing<ITeacher>(teachers, ...(this.subject?.teachers ?? [])),
        ),
      )
      .subscribe((teachers: ITeacher[]) => (this.teachersSharedCollection = teachers));

    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) =>
          this.studentService.addStudentToCollectionIfMissing<IStudent>(students, ...(this.subject?.students ?? [])),
        ),
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }
}
