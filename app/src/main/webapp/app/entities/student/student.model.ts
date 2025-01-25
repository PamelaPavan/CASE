import dayjs from 'dayjs/esm';
import { ISubject } from 'app/entities/subject/subject.model';

export interface IStudent {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  enrollmentDate?: dayjs.Dayjs | null;
  street?: string | null;
  city?: string | null;
  state?: string | null;
  zipCode?: string | null;
  subjects?: ISubject[] | null;
}

export type NewStudent = Omit<IStudent, 'id'> & { id: null };
