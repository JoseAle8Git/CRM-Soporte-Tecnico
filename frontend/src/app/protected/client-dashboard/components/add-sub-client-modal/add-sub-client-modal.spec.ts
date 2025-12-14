import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSubClientModal } from './add-sub-client-modal';

describe('AddSubClientModal', () => {
  let component: AddSubClientModal;
  let fixture: ComponentFixture<AddSubClientModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSubClientModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSubClientModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
