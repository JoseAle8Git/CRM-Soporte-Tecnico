import { TestBed } from '@angular/core/testing';

import { PublicSevice } from './public-sevice';

describe('PublicSevice', () => {
  let service: PublicSevice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicSevice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
