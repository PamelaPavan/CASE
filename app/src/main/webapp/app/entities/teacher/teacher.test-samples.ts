import { ITeacher, NewTeacher } from './teacher.model';

export const sampleWithRequiredData: ITeacher = {
  id: 9510,
  firstName: 'Márcia',
  lastName: 'Carvalho',
  specialization: 'kindly seafood',
};

export const sampleWithPartialData: ITeacher = {
  id: 28138,
  firstName: 'Paulo',
  lastName: 'Xavier',
  specialization: 'whereas',
};

export const sampleWithFullData: ITeacher = {
  id: 30957,
  firstName: 'Karla',
  lastName: 'Reis',
  specialization: 'incidentally',
};

export const sampleWithNewData: NewTeacher = {
  firstName: 'Samuel',
  lastName: 'Franco',
  specialization: 'throughout place harmful',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
