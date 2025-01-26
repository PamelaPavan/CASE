import { ISubject } from 'app/entities/subject/subject.model';

export interface ITeacher {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  specialization?: string | null;
  subjects?: ISubject[] | null;
}

export type NewTeacher = Omit<ITeacher, 'id'> & { id: null };
