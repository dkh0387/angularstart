import { TestBed } from '@angular/core/testing';

import { GoogledriveApiService } from './googledrive-api.service';

describe('GoogledriveapiService', () => {
  let service: GoogledriveApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GoogledriveApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
