import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HamsterCardComponent } from './hamster-card.component';

describe('HamsterCardComponent', () => {
  let component: HamsterCardComponent;
  let fixture: ComponentFixture<HamsterCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HamsterCardComponent]
    });
    fixture = TestBed.createComponent(HamsterCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
