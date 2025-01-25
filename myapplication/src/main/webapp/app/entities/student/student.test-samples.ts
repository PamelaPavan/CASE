import dayjs from 'dayjs/esm';

import { IStudent, NewStudent } from './student.model';

export const sampleWithRequiredData: IStudent = {
  id: 15380,
  firstName: 'Yango',
  lastName: 'Souza',
  email: 'Paula64@bol.com.br',
  enrollmentDate: dayjs('2025-01-25T05:44'),
  street: 'Célia Marginal',
  city: 'Elisa do Sul',
  state: 'nor per replacement',
  zipCode: '25050-914',
};

export const sampleWithPartialData: IStudent = {
  id: 12091,
  firstName: 'Sarah',
  lastName: 'Macedo',
  email: 'Fabricio.Oliveira34@live.com',
  enrollmentDate: dayjs('2025-01-25T07:51'),
  street: 'Albuquerque Rodovia',
  city: 'Emanuelly do Norte',
  state: 'violently',
  zipCode: '51340-155',
};

export const sampleWithFullData: IStudent = {
  id: 6617,
  firstName: 'Mércia',
  lastName: 'Martins',
  email: 'Isaac_Santos@bol.com.br',
  enrollmentDate: dayjs('2025-01-25T04:43'),
  street: 'Vicente Avenida',
  city: 'Nogueira do Norte',
  state: 'eek against',
  zipCode: '65935-551',
};

export const sampleWithNewData: NewStudent = {
  firstName: 'Pablo',
  lastName: 'Nogueira',
  email: 'Daniel.Braga3@live.com',
  enrollmentDate: dayjs('2025-01-24T22:51'),
  street: 'Albuquerque Avenida',
  city: 'Fábio do Descoberto',
  state: 'oof collaborate yahoo',
  zipCode: '16875-026',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
