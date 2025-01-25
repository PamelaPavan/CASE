import { ITeacher, NewTeacher } from './teacher.model';

export const sampleWithRequiredData: ITeacher = {
  id: 9510,
  firstName: 'Márcia',
  lastName: 'Carvalho',
  specialization: 'kindly seafood',
  street: 'Maria Júlia Rodovia',
  city: 'Gúbio do Sul',
  state: 'orderly',
  zipCode: '60995-777',
};

export const sampleWithPartialData: ITeacher = {
  id: 28138,
  firstName: 'Paulo',
  lastName: 'Xavier',
  specialization: 'whereas',
  street: 'João Miguel Travessa',
  city: 'Maria Júlia do Norte',
  state: 'which',
  zipCode: '53695-138',
};

export const sampleWithFullData: ITeacher = {
  id: 30957,
  firstName: 'Karla',
  lastName: 'Reis',
  specialization: 'incidentally',
  street: 'Helena Travessa',
  city: 'Helena do Sul',
  state: 'provided',
  zipCode: '82080-305',
};

export const sampleWithNewData: NewTeacher = {
  firstName: 'Samuel',
  lastName: 'Franco',
  specialization: 'throughout place harmful',
  street: 'Frederico Rodovia',
  city: 'Maria Cecília do Sul',
  state: 'woot',
  zipCode: '31831-274',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
