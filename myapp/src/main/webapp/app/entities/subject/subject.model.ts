import { ITeacher } from 'app/entities/teacher/teacher.model';
import { IStudent } from 'app/entities/student/student.model';

export interface ISubject {
  id: number;
  name?: string | null;
  description?: string | null;
  teachers?: ITeacher[] | null;
  students?: IStudent[] | null;
}

export type NewSubject = Omit<ISubject, 'id'> & { id: null };
