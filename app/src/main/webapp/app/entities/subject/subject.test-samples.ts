import { ISubject, NewSubject } from './subject.model';

export const sampleWithRequiredData: ISubject = {
  id: 18046,
  name: 'perfumed whoever',
};

export const sampleWithPartialData: ISubject = {
  id: 31257,
  name: 'saloon strong',
};

export const sampleWithFullData: ISubject = {
  id: 6439,
  name: 'supportive satirize gripping',
  description: 'meh uh-huh',
};

export const sampleWithNewData: NewSubject = {
  name: 'aboard fairly pike',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
