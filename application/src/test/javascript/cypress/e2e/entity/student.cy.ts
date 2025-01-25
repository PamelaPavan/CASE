import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Student e2e test', () => {
  const studentPageUrl = '/student';
  const studentPageUrlPattern = new RegExp('/student(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const studentSample = {
    firstName: 'Núbia',
    lastName: 'Silva',
    email: 'Isaac53@yahoo.com',
    enrollmentDate: '2025-01-25T09:38:06.004Z',
    street: 'Batista Alameda',
    city: 'Lucas de Nossa Senhora',
    state: 'accidentally by pro',
    zipCode: '34998-895',
  };

  let student;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/students+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/students').as('postEntityRequest');
    cy.intercept('DELETE', '/api/students/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (student) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/students/${student.id}`,
      }).then(() => {
        student = undefined;
      });
    }
  });

  it('Students menu should load Students page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('student');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Student').should('exist');
    cy.url().should('match', studentPageUrlPattern);
  });

  describe('Student page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(studentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Student page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/student/new$'));
        cy.getEntityCreateUpdateHeading('Student');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', studentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/students',
          body: studentSample,
        }).then(({ body }) => {
          student = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/students+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [student],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(studentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Student page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('student');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', studentPageUrlPattern);
      });

      it('edit button click should load edit Student page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Student');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', studentPageUrlPattern);
      });

      it('edit button click should load edit Student page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Student');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', studentPageUrlPattern);
      });

      it('last delete button click should delete instance of Student', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('student').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', studentPageUrlPattern);

        student = undefined;
      });
    });
  });

  describe('new Student page', () => {
    beforeEach(() => {
      cy.visit(`${studentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Student');
    });

    it('should create an instance of Student', () => {
      cy.get(`[data-cy="firstName"]`).type('Danilo');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Danilo');

      cy.get(`[data-cy="lastName"]`).type('Moraes');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Moraes');

      cy.get(`[data-cy="email"]`).type('Miguel_Xavier48@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Miguel_Xavier48@hotmail.com');

      cy.get(`[data-cy="enrollmentDate"]`).type('2025-01-25T05:39');
      cy.get(`[data-cy="enrollmentDate"]`).blur();
      cy.get(`[data-cy="enrollmentDate"]`).should('have.value', '2025-01-25T05:39');

      cy.get(`[data-cy="street"]`).type('Pereira Alameda');
      cy.get(`[data-cy="street"]`).should('have.value', 'Pereira Alameda');

      cy.get(`[data-cy="city"]`).type('Silas do Sul');
      cy.get(`[data-cy="city"]`).should('have.value', 'Silas do Sul');

      cy.get(`[data-cy="state"]`).type('lasting');
      cy.get(`[data-cy="state"]`).should('have.value', 'lasting');

      cy.get(`[data-cy="zipCode"]`).type('56273-538');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '56273-538');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        student = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', studentPageUrlPattern);
    });
  });
});
