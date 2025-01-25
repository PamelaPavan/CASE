import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'schoolApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'student',
    data: { pageTitle: 'schoolApp.student.home.title' },
    loadChildren: () => import('./student/student.routes'),
  },
  {
    path: 'teacher',
    data: { pageTitle: 'schoolApp.teacher.home.title' },
    loadChildren: () => import('./teacher/teacher.routes'),
  },
  {
    path: 'subject',
    data: { pageTitle: 'schoolApp.subject.home.title' },
    loadChildren: () => import('./subject/subject.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
