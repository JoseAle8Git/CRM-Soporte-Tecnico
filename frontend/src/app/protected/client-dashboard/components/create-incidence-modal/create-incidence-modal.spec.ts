import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateIncidenceModal } from './create-incidence-modal';

describe('CreateIncidenceModal', () => {
  let component: CreateIncidenceModal;
  let fixture: ComponentFixture<CreateIncidenceModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateIncidenceModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateIncidenceModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
