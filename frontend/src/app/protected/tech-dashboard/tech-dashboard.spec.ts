import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TechDashboard } from './tech-dashboard';

describe('TechDashboard', () => {
  let component: TechDashboard;
  let fixture: ComponentFixture<TechDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TechDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
