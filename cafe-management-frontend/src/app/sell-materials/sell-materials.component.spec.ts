import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellMaterialsComponent } from './sell-materials.component';

describe('SellMaterialsComponent', () => {
  let component: SellMaterialsComponent;
  let fixture: ComponentFixture<SellMaterialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SellMaterialsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellMaterialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
