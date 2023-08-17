import { TestBed } from '@angular/core/testing';

import { FriendsAddingServiceService } from './friends-adding-service.service';

describe('FriendsAddingServiceService', () => {
  let service: FriendsAddingServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FriendsAddingServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
