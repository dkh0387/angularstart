import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LenaServicesOverviewComponent } from './lena-services-overview.component';

describe('LenaServicesOverviewComponent', () => {
  let component: LenaServicesOverviewComponent;
  let fixture: ComponentFixture<LenaServicesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LenaServicesOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LenaServicesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
