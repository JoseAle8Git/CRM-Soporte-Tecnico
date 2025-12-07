import { TestBed } from '@angular/core/testing';

import { ResportService } from './resport.service';

describe('ResportService', () => {
  let service: ResportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
